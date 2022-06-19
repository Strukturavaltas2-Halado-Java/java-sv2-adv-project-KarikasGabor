package tabletennis.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tabletennis.dto.CreatePlayerCommand;
import tabletennis.dto.PlayerDto;
import tabletennis.dto.PlayerListDto;
import tabletennis.model.Player;
import tabletennis.service.TableTennisService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/players")
@AllArgsConstructor
public class PlayerController {

    private TableTennisService service;

    @GetMapping
    public List<PlayerListDto> getPlayerList() {
        return service.getPlayerList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlayerDto getPlayerById(@PathVariable ("id") long playerId) {
        return service.getPlayerById(playerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerDto createPlayer(@RequestBody CreatePlayerCommand createPlayerCommand, @RequestParam (name = "org") Optional<Long> orgId) {
        return service.createPlayer(createPlayerCommand, orgId);
    }

    @PostMapping("/{id}")
    public PlayerDto validateLicense(@PathVariable ("id") long playerId) {
        return service.validateLicense(playerId);
    }


    @PostMapping("/{playerId}/{orgId}")
    @ResponseStatus(HttpStatus.OK)
    public PlayerDto transferPlayer(@PathVariable ("playerId") long playerId, @PathVariable ("orgId") long orgId) {
        return service.transferPlayer(playerId, orgId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable ("id") long playerId) {
        service.deletePlayer(playerId);
    }
}
