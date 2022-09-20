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
    private SensorTypeId id;

    private String description;
    private Double min;
    private Double max;
    private String measureUnit;

    @Getter
    public enum SensorTypeId {
        AT("Temperatura Ambiente"),
        AH("Humedad Ambiente"),
        SH("Humedad de la tierra"),
        ST("Temperatura de la tierra"),
        LL("Nivel de Luz");

        private final String description;

        SensorTypeId(String description) {
            this.description = description;
        }
    }
}
