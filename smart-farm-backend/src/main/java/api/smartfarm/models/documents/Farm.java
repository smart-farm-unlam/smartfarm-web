package api.smartfarm.models.documents;

import api.smartfarm.models.dtos.FarmDTO;
import api.smartfarm.models.entities.Crop;
import api.smartfarm.models.entities.Event;
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

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "farms")
public class Farm {
    private String id;
    private String name;
    private String userId;

    private List<Sensor> sensors;
    private List<Sector> sectors;
    private List<Event> events;

    public Farm(FarmDTO farmDTO) {
        this.name = farmDTO.getName();
        this.userId = farmDTO.getUserId();
        this.sensors = new ArrayList<>();
        this.sectors = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public Plant getPlantById(String plantId) {
        if(sectors!=null){
            for (Sector sector : sectors) {
                Crop crop = sector.getCrop();
                if(crop!=null && crop.getPlants()!=null){
                    for (Plant plant : crop.getPlants()) {
                        if(plant.getId().equalsIgnoreCase(plantId)){
                            return plant;
                        }
                    }
                }
            }   
        }
        return null;
    }
}
