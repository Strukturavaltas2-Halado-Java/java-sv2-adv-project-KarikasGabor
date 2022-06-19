package tabletennis.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class PlayerListIsNotEmptyException extends AbstractThrowableProblem {

    public PlayerListIsNotEmptyException(long id) {
        super(URI.create("organization/organization-not-empty"),
                "Player list not empty",
                Status.PRECONDITION_FAILED,
                String.format("Player list is not empty in organization: %d", id));
    }
}
