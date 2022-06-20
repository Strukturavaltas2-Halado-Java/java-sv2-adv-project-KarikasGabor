package tabletennis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tabletennis.dto.CreateOrganizationCommand;
import tabletennis.dto.ModifyOrganizationCommand;
import tabletennis.dto.OrganizationDto;
import tabletennis.dto.OrganizationListDto;
import tabletennis.service.TableTennisService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/organizations")
@AllArgsConstructor
@Tag(name = "Organization's operations")
public class OrganizationController {

    private TableTennisService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new organization")
    public OrganizationDto createOrganization(@Valid @RequestBody CreateOrganizationCommand createOrganizationCommand) {
        return service.createOrganization(createOrganizationCommand);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Modification of organization's data")
    public OrganizationDto modifyOrganization(@Valid @PathVariable("id") long orgId, @Valid @RequestBody ModifyOrganizationCommand modifyOrganizationCommand) {
        return service.modifyOrganization(orgId, modifyOrganizationCommand);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List of organizations")
    public List<OrganizationListDto> getOrganizationList() {
        return service.getOrganizationList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get organization's data")
    public OrganizationDto getOrganizationById(@PathVariable("id") long orgId,
                                               @RequestParam(name = "tk") Optional<String> full,
                                               @RequestParam(name = "valid") Optional<String> valid) {
        return service.getOrganizationById(orgId, full, valid);
    }

    @DeleteMapping("/{orgId}/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a player from organization")
    public OrganizationDto deletePlayerFromOrganization(@PathVariable("orgId") long orgId, @PathVariable("playerId") long playerId) {
        return service.deletePlayerFromOrganization(orgId, playerId);
    }

    @DeleteMapping("/{orgId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete organization")
    public void deletePlayerFromOrganization(@PathVariable("orgId") long orgId) {
        service.deleteOrganization(orgId);
    }
}
