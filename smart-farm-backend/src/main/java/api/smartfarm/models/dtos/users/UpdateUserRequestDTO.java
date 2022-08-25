package api.smartfarm.models.dtos.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateUserRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String deviceId;
}
