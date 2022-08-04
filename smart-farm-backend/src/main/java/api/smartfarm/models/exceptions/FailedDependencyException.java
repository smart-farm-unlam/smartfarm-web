package api.smartfarm.models.exceptions;

import org.springframework.http.HttpStatus;

public class FailedDependencyException extends ApiException {

    private static final int HTTP_STATUS_CODE = HttpStatus.FAILED_DEPENDENCY.value();

    public FailedDependencyException(String message) {
        super(message, HTTP_STATUS_CODE);
    }
}
