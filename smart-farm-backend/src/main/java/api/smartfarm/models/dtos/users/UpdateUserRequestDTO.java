package api.smartfarm.models.dtos.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateUserRequestDTO {
    private String username;
    private String email;
    private String deviceId;
}
