package api.smartfarm.models.dtos;

import api.smartfarm.models.entities.Crop;
import api.smartfarm.models.entities.Sector;
import api.smartfarm.models.entities.Sensor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SectorDTO {
    private String id;
    private Integer row;
    private Integer column;
    private Crop crop;
    private List<Sensor> sensors;

    public SectorDTO(Sector sector) {
        this.id = sector.getId();
        this.row = sector.getRow();
        this.column = sector.getColumn();
        this.crop = sector.getCrop();
        this.sensors = sector.getSensors();
    }
}
