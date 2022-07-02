package api.smartfarm.models.exceptions;

import org.springframework.http.HttpStatus;

public class EventDTOParseException extends ApiException {

    private static final int HTTP_STATUS_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public EventDTOParseException(String message) {
        super(message, HTTP_STATUS_CODE);
    }
        
}
