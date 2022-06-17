package api.smartfarm.models.documents;

import api.smartfarm.models.dtos.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public User(UserDTO userDTO) {
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
        this.email = userDTO.getEmail();
    }
}
