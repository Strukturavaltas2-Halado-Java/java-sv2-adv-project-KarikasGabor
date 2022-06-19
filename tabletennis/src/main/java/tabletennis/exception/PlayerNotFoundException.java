package tabletennis.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class PlayerNotFoundException extends AbstractThrowableProblem {
    public PlayerNotFoundException(long id) {
        super(URI.create("player/player-not-found"),
                "Player not found",
                Status.NOT_FOUND,
                String.format("Player not found with id: %d", id));
    }
}

