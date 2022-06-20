package tabletennis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.violations.Violation;
import tabletennis.dto.CreatePlayerCommand;
import tabletennis.dto.PlayerDto;
import tabletennis.dto.PlayerListDto;
import tabletennis.service.TableTennisService;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players")
@AllArgsConstructor
@Tag(name = "Player's operations")
public class PlayerController {

    private TableTennisService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of players")
    public List<PlayerListDto> getPlayerList() {
        return service.getPlayerList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get player's data")
    public PlayerDto getPlayerById(@PathVariable("id") long playerId) {
        return service.getPlayerById(playerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new player (with or without club membership)")
    public PlayerDto createPlayer(@Valid @RequestBody CreatePlayerCommand createPlayerCommand, @RequestParam(name = "org") Optional<Long> orgId) {
        return service.createPlayer(createPlayerCommand, orgId);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Validate player's license")
    public PlayerDto validateLicense(@PathVariable("id") long playerId) {
        return service.validateLicense(playerId);
    }


    @PostMapping("/{playerId}/{orgId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Transfer player")
    public PlayerDto transferPlayer(@PathVariable("playerId") long playerId, @PathVariable("orgId") long orgId) {
        return service.transferPlayer(playerId, orgId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete player")
    public void deletePlayer(@PathVariable("id") long playerId) {
        service.deletePlayer(playerId);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Problem> handleValidationError(MethodArgumentNotValidException e) {
        List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map((FieldError fe) -> new Violation(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        Problem problem = Problem.builder()
                .withType(URI.create("player/validation-error"))
                .withTitle("Validation error")
                .withStatus(Status.BAD_REQUEST)
                .withDetail(e.getMessage())
                .with("violations", violations)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}
