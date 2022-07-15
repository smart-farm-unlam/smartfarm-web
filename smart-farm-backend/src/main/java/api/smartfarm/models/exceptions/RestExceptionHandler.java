package api.smartfarm.models.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ApiError handleAnyException(Exception ex, WebRequest request) {
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.log(ex, statusCode);
        return this.buildApiError(ex, statusCode, null, request);
    }

    @ExceptionHandler(value = ApiException.class)
    private ResponseEntity<ApiError> handleApiException(ApiException ex, WebRequest request) {
        int statusCode = ex.getCode();
        this.log(ex, statusCode);
        ApiError error = this.buildApiError(ex, statusCode, null, request);

        return new ResponseEntity<>(error, HttpStatus.valueOf(ex.getCode()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request
    ) {
        int statusCode = status.value();
        this.log(ex, statusCode);

        List<String> errors = ex.getBindingResult().getFieldErrors()
            .stream().map(e -> e.getField() + " " + e.getDefaultMessage())
            .collect(Collectors.toList());

        ApiError error = this.buildApiError(ex, statusCode, errors, request);

        return new ResponseEntity<>(error, HttpStatus.valueOf(statusCode));
    }

    private void log(Exception ex, int statusCode) {
        if (HttpStatus.valueOf(statusCode).is4xxClientError()) {
            LOG.warn(ex.getMessage(), ex);
        } else {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private ApiError buildApiError(
        Exception ex,
        int statusCode,
        List<String> errors,
        WebRequest request
    ) {
        return new ApiError(
            new Date(),
            statusCode,
            ex.getMessage(),
            request.getDescription(false),
            errors
        );
    }

}
