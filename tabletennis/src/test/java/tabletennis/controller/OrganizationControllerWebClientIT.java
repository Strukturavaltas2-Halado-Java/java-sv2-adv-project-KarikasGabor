package tabletennis.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import tabletennis.dto.*;
import tabletennis.model.LicenseType;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from players", "delete from organizations"})
class OrganizationControllerWebClientIT {

    @Autowired
    WebTestClient webTestClient;

    OrganizationDto org;
    PlayerDto player;

    @BeforeEach
    void setUp() {
        player = webTestClient.post()
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
                .expectBody(PlayerDto.class).returnResult().getResponseBody();

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

        org = webTestClient.post()
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
    void testCreateOrganization() {
        webTestClient.post()
                .uri("api/organizations")
                .bodyValue(new CreateOrganizationCommand(
                        "ETO", "Győr", "Kertész Sándor", "ks@eto.hu", "2468123"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(OrganizationDto.class)
                .value(p -> assertThat(p.getOrgName()).isEqualTo("ETO"))
                .value(p -> assertThat(p.getContact()).isEqualTo("Kertész Sándor"))
                .value(p -> assertThat(p.getAddress()).isEqualTo("Győr"));
    }

    @Test
    void testGetOrganizationList() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/organizations").build())
                .exchange()
                .expectBodyList(OrganizationListDto.class)
                .value(list -> assertThat(list)
                        .hasSize(2)
                        .extracting(OrganizationListDto::getOrgName)
                        .containsOnly("BVSC-Zugló", "Postás SE"));
    }

    @Test
    void testGetOrganizationById() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/organizations/{orgId}").build(org.getOrgId()))
                .exchange()
                .expectBody(OrganizationDto.class)
                .value(org -> assertThat(org)
                        .extracting(OrganizationDto::getOrgName)
                        .isEqualTo("BVSC-Zugló"));
    }

    @Test
    void testDeleteOrganization() {
        long orgId = webTestClient.post()
                .uri("api/organizations")
                .bodyValue(new CreateOrganizationCommand(
                        "ETO", "Győr", "Kertész Sándor", "ks@eto.hu", "2468123"))
                .exchange()
                .expectBody(OrganizationDto.class).returnResult().getResponseBody().getOrgId();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/organizations/{orgId}").build(orgId))
                .exchange()
                .expectStatus().isOk();

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("api/organizations/{orgId}").build(orgId))
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/organizations/{orgId}").build(orgId))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testTestDeletePlayerFromOrganization() {
        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/players/{playerId}/{orgId}").build(player.getPlayerId(), org.getOrgId()))
                .exchange()
                .expectBody(PlayerDto.class)
                .value(p -> assertEquals(org.getOrgId(), p.getOrganization().getOrgId()));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/players/{id}").build(player.getPlayerId()))
                .exchange()
                .expectBody(PlayerDto.class)
                .value(p -> assertEquals(org.getOrgId(), p.getOrganization().getOrgId()));

        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("api/organizations/{orgId}/{playerId}").build(org.getOrgId(), player.getPlayerId()))
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/players/{id}").build(player.getPlayerId()))
                .exchange()
                .expectBody(PlayerDto.class)
                .value(p -> assertEquals(null, p.getOrganization()));
    }
}