package api.smartfarm.models.dtos.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserRequestDTO {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;

    private String email;
}
