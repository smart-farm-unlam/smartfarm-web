package api.smartfarm.models.documents;

import api.smartfarm.models.dtos.users.CreateUserRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "users")
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> deviceIds;

    public User(CreateUserRequestDTO createUserRequest) {
        this.firstName = createUserRequest.getFirstName();
        this.lastName = createUserRequest.getLastName();
        this.email = createUserRequest.getEmail();
        this.deviceIds = new ArrayList<>();
    }

    public void addNewDevice(String deviceId) {
        if (deviceIds == null) {
            deviceIds = new ArrayList<>();
        }
        deviceIds.add(deviceId);
    }
}
