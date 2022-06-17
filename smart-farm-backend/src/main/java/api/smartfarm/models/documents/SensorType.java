package api.smartfarm.models.documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "sensors")
public class SensorType {
    @Id
    private String id;

    private String description;
    private Double min;
    private Double max;
    private String measureUnit;
}
