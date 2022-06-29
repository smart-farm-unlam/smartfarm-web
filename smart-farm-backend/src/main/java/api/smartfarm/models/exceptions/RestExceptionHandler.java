package api.smartfarm.models.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ApiError handleAnyException(Exception ex, WebRequest request) {
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.log(ex, statusCode);
        return this.buildApiError(ex, statusCode, request);
    }

    @ExceptionHandler(value = ApiException.class)
    private ResponseEntity<ApiError> handleApiException(ApiException ex, WebRequest request) {
        int statusCode = ex.getCode();
        this.log(ex, statusCode);
        ApiError error = this.buildApiError(ex, statusCode, request);

        return new ResponseEntity<>(error, HttpStatus.valueOf(ex.getCode()));
    }

    private void log(Exception ex, int statusCode) {
        if (HttpStatus.valueOf(statusCode).is4xxClientError()) {
            LOG.warn(ex.getMessage(), ex);
        } else {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private ApiError buildApiError(Exception ex, int statusCode, WebRequest request) {
        return new ApiError(
            new Date(),
            statusCode,
            ex.getMessage(),
            request.getDescription(false)
        );
    }

}
