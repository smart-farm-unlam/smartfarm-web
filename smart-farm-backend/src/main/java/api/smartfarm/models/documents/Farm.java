package api.smartfarm.models.documents;

import api.smartfarm.models.entities.Crop;
import api.smartfarm.models.entities.Plant;
import api.smartfarm.models.entities.Sector;
import api.smartfarm.models.entities.Sensor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "farms")
public class Farm {
    private String id;
    private String name;
    private String userId;
    private Double length;
    private Double width;
    private List<Sensor> sensors;
    private List<Sector> sectors;
    private List<String> events;
    private Double latitude;
    private Double longitude;
    private String locationKey;

    public Farm(String name, String userId) {
        this.name = name;
        this.userId = userId;
        this.sensors = new ArrayList<>();
        this.sectors = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public Plant getPlantById(String plantId) {
        for (Sector sector : sectors) {
            Crop crop = sector.getCrop();
            for (Plant plant : crop.getPlants()) {
                if (plant.getId().equalsIgnoreCase(plantId)) {
                    return plant;
                }
            }
        }
        return null;
    }

    public Optional<Sector> getSectorBySensorCode(String sensorCode) {
        for (Sector sector : getSectors()) {
            Optional<Sensor> sensor = sector.getSensors().stream()
                .filter(ss -> sensorCode.equals(ss.getCode()))
                .findFirst();

            if (sensor.isPresent()) {
                return Optional.of(sector);
            }
        }
        return Optional.empty();
    }
}
