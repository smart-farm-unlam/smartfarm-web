package api.smartfarm.models.dtos.sectors;

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
public class SectorResponseDTO {
    private String code;
    private Integer row;
    private Integer column;
    private Crop crop;
    private List<Sensor> sensors;

    public SectorResponseDTO(Sector sector) {
        this.code = sector.getCode();
        this.row = sector.getRow();
        this.column = sector.getColumn();
        this.crop = sector.getCrop();
        this.sensors = sector.getSensors();
    }
}
