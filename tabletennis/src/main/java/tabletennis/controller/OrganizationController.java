package tabletennis.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tabletennis.dto.CreateOrganizationCommand;
import tabletennis.dto.OrganizationDto;
import tabletennis.dto.OrganizationListDto;
import tabletennis.dto.PlayerDto;
import tabletennis.service.TableTennisService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/organizations")
@AllArgsConstructor
public class OrganizationController {

    private TableTennisService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizationDto createOrganization(@RequestBody CreateOrganizationCommand createOrganizationCommand) {
        return service.createOrganization(createOrganizationCommand);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizationDto createOrganization(@PathVariable ("id") long orgId, @RequestBody CreateOrganizationCommand createOrganizationCommand) {
        return service.modifyOrganization(orgId, createOrganizationCommand);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrganizationListDto> getOrganizationList() {
        return service.getOrganizationList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrganizationDto getOrganizationById(@PathVariable ("id") long orgId,
                                               @RequestParam (name = "tk") Optional<String> full,
                                               @RequestParam (name = "valid") Optional<String> valid) {
        return service.getOrganizationById(orgId, full, valid);
    }

    @DeleteMapping("/{orgId}/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public OrganizationDto deletePlayerFromOrganization(@PathVariable ("orgId") long orgId, @PathVariable ("playerId") long playerId){
        return service.deletePlayerFromOrganization(orgId, playerId);
    }

    @DeleteMapping("/{orgId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayerFromOrganization(@PathVariable ("orgId") long orgId){
        service.deleteOrganization(orgId);
    }
}
