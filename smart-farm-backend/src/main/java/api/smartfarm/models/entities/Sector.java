package api.smartfarm.models.entities;

import api.smartfarm.models.dtos.SectorDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sector {
    private String code;
    private Integer row;
    private Integer column;
    private Crop crop;
    private List<Sensor> sensors;

    public Sector(SectorDTO sectorDTO) {
        this.code = sectorDTO.getCode();
        this.row = sectorDTO.getRow();
        this.column = sectorDTO.getColumn();
        this.crop = sectorDTO.getCrop();
        this.sensors = sectorDTO.getSensors();
    }
}
