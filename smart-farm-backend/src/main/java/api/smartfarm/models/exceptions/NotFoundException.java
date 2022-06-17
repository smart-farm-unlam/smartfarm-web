package api.smartfarm.models.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {

    private static final int HTTP_STATUS_CODE = HttpStatus.NOT_FOUND.value();

    public NotFoundException(String message) {
        super(message, HTTP_STATUS_CODE);
    }

    public NotFoundException(String message, Throwable throwable) {
        super(message, throwable, HTTP_STATUS_CODE);
    }
}
