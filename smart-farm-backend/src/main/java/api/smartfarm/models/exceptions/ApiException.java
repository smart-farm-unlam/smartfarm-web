package api.smartfarm.models.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiException extends RuntimeException {

    private final int code;

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ApiException(String message, Throwable throwable, int code) {
        super(message, throwable);
        this.code = code;
    }
}
