package api.smartfarm.models.dtos.users;

import api.smartfarm.models.documents.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String id;
    private String username;
    private String email;
    private List<String> deviceIds;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.deviceIds = user.getDeviceIds();
    }
}
