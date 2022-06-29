package api.smartfarm.models.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {
    private Date timestamp;
    private int statusCode;
    private String message;
    private String description;
}
