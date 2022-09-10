package api.smartfarm.models.documents;

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
    private String username;
    private String email;
    private List<String> deviceIds;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.deviceIds = new ArrayList<>();
    }

    public void addNewDevice(String deviceId) {
        if (deviceIds == null) {
            deviceIds = new ArrayList<>();
        }
        deviceIds.add(deviceId);
    }
}
