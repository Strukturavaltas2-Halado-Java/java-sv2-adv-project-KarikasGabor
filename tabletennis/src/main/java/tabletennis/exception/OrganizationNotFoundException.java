package tabletennis.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class OrganizationNotFoundException extends AbstractThrowableProblem {
    public OrganizationNotFoundException(long id) {
        super(URI.create("organization/organization-not-found"),
                "Organization not found",
                Status.NOT_FOUND,
                String.format("Organization not found with id: %d", id));
    }
}
