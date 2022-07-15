package api.smartfarm.models.dtos.farms;

import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.entities.Sector;
import api.smartfarm.models.entities.Sensor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FarmResponseDTO {

    private String id;
    private String name;
    private String userId;
    private Double length;
    private Double width;
    private List<Sensor> sensors;
    private List<Sector> sectors;

    public FarmResponseDTO(Farm farm) {
        this.id = farm.getId();
        this.name = farm.getName();
        this.userId = farm.getUserId();
        this.length = farm.getLength();
        this.width = farm.getWidth();
        this.sensors = farm.getSensors();
        this.sectors = farm.getSectors();
    }
}
