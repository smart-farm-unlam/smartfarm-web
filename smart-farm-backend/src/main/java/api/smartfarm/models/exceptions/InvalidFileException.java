package api.smartfarm.models.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidFileException extends ApiException {

    private static final int HTTP_STATUS_CODE = HttpStatus.NOT_ACCEPTABLE.value();

    public InvalidFileException(String message) {
        super(message, HTTP_STATUS_CODE);
    }
        
}
