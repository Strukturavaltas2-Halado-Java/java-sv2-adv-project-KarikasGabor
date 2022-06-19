package tabletennis.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import tabletennis.dto.*;
import tabletennis.model.LicenseType;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from players", "delete from organizations"})
class PlayerControllerWebClientIT {

    @Autowired
    WebTestClient webTestClient;

    long playerId;
    OrganizationDto organization;

    @BeforeEach
    void setUp() {
        playerId = webTestClient.post()
                .uri("api/players")
                .bodyValue(new CreatePlayerCommandForTest(
                        "Kiss Ádám",
                        LocalDate.of(1999, 7, 7),
                        "Nagy Éva",
                        null,

                        LocalDate.of(2020, 9, 2),
                        LocalDate.of(2021, 6, 30),
                        LicenseType.TELJESKÖRŰ))
                .exchange()
                .expectBody(PlayerDto.class).returnResult().getResponseBody().getPlayerId();

        webTestClient.post()
                .uri("api/players")
                .bodyValue(new CreatePlayerCommandForTest(
                        "Barna Bálint",
                        LocalDate.of(1985, 2, 2),
                        "Fekete Fruzsina",
                        null,
                        LocalDate.of(2021, 9, 2),
                        LocalDate.of(2022, 6, 30),
                        LicenseType.TELJESKÖRŰ))
                .exchange();

        webTestClient.post()
                .uri("api/players")
                .bodyValue(new CreatePlayerCommandForTest(
                        "Szőke Szilárd",
                        LocalDate.of(1956, 3, 3),
                        "Zöld Zsuzsa",
                        null,
                        LocalDate.of(2022, 3, 1),
                        LocalDate.of(2022, 6, 30),
                        LicenseType.EGYÉNI))
                .exchange();

        organization = webTestClient.post()
                .uri("api/organizations")
                .bodyValue(new CreateOrganizationCommand(
                        "BVSC-Zugló", "Budapest", "Kovács Géza", "kg@bvsc.hu", "1234567"))
                .exchange()
                .expectBody(OrganizationDto.class).returnResult().getResponseBody();

        webTestClient.post()
                .uri("api/organizations")
                .bodyValue(new CreateOrganizationCommand(
                        "Postás SE", "Budapest", "Lakatos Éva", "le@postas.hu", "7654321"))
                .exchange();
    }

    @Test
    void testGetPlayerList() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/players").build())
                .exchange()
                .expectBodyList(PlayerListDto.class)
                .value(list -> assertThat(list)
                        .hasSize(3)
                        .extracting(PlayerListDto::getPlayerName)
                        .containsOnly("Kiss Ádám", "Barna Bálint", "Szőke Szilárd"));
    }

    @Test
    void testGetPlayerById() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/players/{id}").build(playerId))
                .exchange()
                .expectBody(PlayerDto.class)
                .value(p -> assertEquals("Kiss Ádám", p.getPlayerName()));
    }

    @Test
    void testCreatePlayer() {
        webTestClient.post()
                .uri("api/players")
                .bodyValue(new CreatePlayerCommand(
                        "Nagy József",
                        LocalDate.of(2001, 7, 7),
                        "Kiss Ibolya"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PlayerDto.class)
                .value(p -> assertThat(p.getPlayerName()).isEqualTo("Nagy József"))
                .value(p -> assertThat(p.getBirthDate()).isEqualTo(LocalDate.of(2001, 7, 7)))
                .value(p -> assertThat(p.getMotherName()).isEqualTo("Kiss Ibolya"));
    }

    @Test
    void testCreatePlayerWithOrg() {
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/players").queryParam("org", organization.getOrgId()).build())
                .bodyValue(new CreatePlayerCommand(
                        "Nagy József",
                        LocalDate.of(2001, 7, 7),
                        "Kiss Ibolya"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PlayerDto.class)
                .value(p -> assertThat(p.getPlayerName()).isEqualTo("Nagy József"))
                .value(p -> assertThat(p.getBirthDate()).isEqualTo(LocalDate.of(2001, 7, 7)))
                .value(p -> assertThat(p.getMotherName()).isEqualTo("Kiss Ibolya"))
                .value(p -> assertThat(p.getOrganization().getOrgId()).isEqualTo(organization.getOrgId()));
    }

    @Test
    void testValidateLicense() {
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/players/{id}").build(playerId))
                .exchange()
                .expectBody(PlayerDto.class)
                .value(p -> assertEquals(LocalDate.of(2022, 6, 30), p.getLicenseValidityDate()));
    }

    @Test
    void testTransferPlayer() {
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/players/{playerId}/{orgId}").build(playerId, organization.getOrgId()))
                .exchange()
                .expectBody(PlayerDto.class)
                .value(p -> assertEquals(organization.getOrgId(), p.getOrganization().getOrgId()));
    }

    @Test
    void testDeletePlayer() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/players/{playerId}").build(playerId))
                .exchange()
                .expectStatus().isOk();

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("api/players/{playerId}").build(playerId))
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/players/{playerId}").build(playerId))
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void testValidationError() {
        webTestClient.post()
                .uri("api/players")
                .bodyValue(new CreatePlayerCommand(
                        "Nagy József",
                        LocalDate.of(2032, 7, 7),
                        "Kiss Éva"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody().jsonPath("$.title").isEqualTo("Validation error");
    }

//    @Test
//    void testValidateLicenseWithOlderDate() {
//        LocalDate currentLocalDate = LocalDate.of(2023, 2, 13);
//        LocalDate expected = LocalDate.of(2023, 6, 30);
//        try (MockedStatic<LocalDate> topDateTimeUtilMock = Mockito.mockStatic(LocalDate.class)) {
//            topDateTimeUtilMock.when(LocalDate::now).thenReturn(currentLocalDate);
//            System.out.println(LocalDate.now());
//            webTestClient.post()
//                    .uri(uriBuilder -> uriBuilder.path("api/players/{id}").build(id))
//                    .exchange()
//                    .expectBody(PlayerDto.class)
//                    .value(p -> System.out.println(service.now()))
//                    .value(p -> assertEquals(expected, p.getLicenseValidityDate()));
//
//        }
//    }
}