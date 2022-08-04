package api.smartfarm.models.exceptions;

import org.springframework.http.HttpStatus;

public class LimitReachException extends ApiException {

    private static final int HTTP_STATUS_CODE = HttpStatus.SERVICE_UNAVAILABLE.value();

    public LimitReachException(String message) {
        super(message, HTTP_STATUS_CODE);
    }
}
