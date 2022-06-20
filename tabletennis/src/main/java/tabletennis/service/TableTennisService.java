package tabletennis.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import tabletennis.dto.*;
import tabletennis.exception.OrganizationNotFoundException;
import tabletennis.exception.PlayerListIsNotEmptyException;
import tabletennis.exception.PlayerNotFoundException;
import tabletennis.model.LicenseType;
import tabletennis.model.Organization;
import tabletennis.dto.OrganizationListDto;
import tabletennis.model.Player;
import tabletennis.repository.OrganizationRepository;
import tabletennis.repository.PlayerRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class TableTennisService {

    private OrganizationRepository organizationRepository;
    private PlayerRepository playerRepository;
    private ModelMapper modelMapper;


    public PlayerDto createPlayer(CreatePlayerCommand createPlayerCommand, Optional<Long> orgId) {
        Player player = new Player(
                createPlayerCommand.getPlayerName(),
                createPlayerCommand.getBirthDate(),
                createPlayerCommand.getMotherName());
        if (orgId.isPresent()) {
            Organization org = organizationRepository.findById(orgId.get()).orElseThrow(() -> new OrganizationNotFoundException(orgId.get()));
            player.setOrganization(org);
        }
        player.setLicenseDate(LocalDate.now());
        player.setValidity();
        playerRepository.save(player);
        return modelMapper.map(player, PlayerDto.class);
    }

    public OrganizationDto createOrganization(CreateOrganizationCommand createOrganizationCommand) {
        Organization organization = new Organization(
                createOrganizationCommand.getOrgName(),
                createOrganizationCommand.getAddress(),
                createOrganizationCommand.getContact(),
                createOrganizationCommand.getEmail(),
                createOrganizationCommand.getTelNumber());
        organizationRepository.save(organization);
        return modelMapper.map(organization, OrganizationDto.class);
    }

    public List<PlayerListDto> getPlayerList() {
        return playerRepository.findAll()
                .stream()
                .map(player -> modelMapper.map(player, PlayerListDto.class))
                .collect(Collectors.toList());
    }

    public PlayerDto getPlayerById(long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        return modelMapper.map(player, PlayerDto.class);
    }

    public List<OrganizationListDto> getOrganizationList() {
        return organizationRepository.findAll().stream()
                .map(org -> modelMapper.map(org, OrganizationListDto.class))
                .collect(Collectors.toList());
    }

    public OrganizationDto getOrganizationById(long orgId, Optional<String> full, Optional<String> valid) {
        Organization organization = organizationRepository.findById(orgId).orElseThrow(() -> new OrganizationNotFoundException(orgId));
        OrganizationDto organizationDto = modelMapper.map(organization, OrganizationDto.class);
        organizationDto.setPlayers(organization.getPlayers().stream()
                .filter(p -> full.isEmpty() || !full.get().equalsIgnoreCase("true") || p.getLicenseType().equals(LicenseType.FULL))
                .filter(p -> valid.isEmpty() || !valid.get().equalsIgnoreCase("true") || p.getLicenseValidityDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList()));
        return organizationDto;
    }

    public PlayerDto validateLicense(long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        player.setValidity();
        return modelMapper.map(player, PlayerDto.class);
    }

    public void deletePlayer(long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        playerRepository.delete(player);
    }

    public PlayerDto transferPlayer(long playerId, long orgId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        Organization organization = organizationRepository.findById(orgId).orElseThrow(() -> new OrganizationNotFoundException(orgId));
        player.setOrganization(organization);
        player.setValidity();
        player.setLicenseDate(LocalDate.now());
        return modelMapper.map(player, PlayerDto.class);
    }

    public OrganizationDto deletePlayerFromOrganization(long orgId, long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));
        Organization organization = organizationRepository.findById(orgId).orElseThrow(() -> new OrganizationNotFoundException(orgId));
        organization.getPlayers().remove(player);
        player.setOrganization(null);
        return modelMapper.map(organization, OrganizationDto.class);
    }

    public void deleteOrganization(long orgId) {
        Organization organization = organizationRepository.findById(orgId).orElseThrow(() -> new OrganizationNotFoundException(orgId));
        if (organization.getPlayers().size() != 0) {
            throw new PlayerListIsNotEmptyException(orgId);
        } else {
            organizationRepository.delete(organization);
        }
    }

    public OrganizationDto modifyOrganization(long orgId, ModifyOrganizationCommand modifyOrganizationCommand) {
        Organization organization = organizationRepository.findById(orgId).orElseThrow(() -> new OrganizationNotFoundException(orgId));
        organization.setOrgName(modifyOrganizationCommand.getOrgName());
        organization.setAddress(modifyOrganizationCommand.getAddress());
        organization.setContact(modifyOrganizationCommand.getContact());
        organization.setEmail(modifyOrganizationCommand.getEmail());
        organization.setTelNumber(modifyOrganizationCommand.getTelNumber());
        return modelMapper.map(organization, OrganizationDto.class);
    }

}
