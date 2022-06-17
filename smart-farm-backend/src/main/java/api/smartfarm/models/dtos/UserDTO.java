package api.smartfarm.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    @Null
    private String id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;

    private String email;
}
