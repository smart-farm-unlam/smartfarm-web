package api.smartfarm.models.dtos;

import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.entities.Event;
import api.smartfarm.models.entities.Sector;
import api.smartfarm.models.entities.Sensor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FarmDTO {
    @Null
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String userId;

    private List<Sensor> sensors;
    private List<Sector> sectors;
    private List<Event> events;

    public FarmDTO(Farm farm) {
        this.id = farm.getId();
        this.name = farm.getName();
        this.userId = farm.getUserId();
        this.sensors = farm.getSensors();
        this.sectors = farm.getSectors();
        this.events = farm.getEvents();
    }
}
