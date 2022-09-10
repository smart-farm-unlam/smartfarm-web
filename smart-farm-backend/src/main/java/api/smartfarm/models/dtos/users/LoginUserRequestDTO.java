package api.smartfarm.models.dtos.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class LoginUserRequestDTO {
    @NotEmpty
    private String username;

    @NotEmpty
    private String email;
}
