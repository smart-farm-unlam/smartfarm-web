package api.smartfarm.models.documents;

import api.smartfarm.models.dtos.FarmDTO;
import api.smartfarm.models.entities.Event;
import api.smartfarm.models.entities.Sector;
import api.smartfarm.models.entities.Sensor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "farms")
public class Farm {
    @Id
    private String id;
    private String name;
    private String userId;

    private List<Sensor> sensors;
    private List<Sector> sectors;
    private List<Event> events;

    public Farm(FarmDTO farmDTO) {
        this.id = UUID.randomUUID().toString();
        this.name = farmDTO.getName();
        this.userId = farmDTO.getUserId();
        this.sensors = new ArrayList<>();
        this.sectors = new ArrayList<>();
        this.events = new ArrayList<>();
    }
}
