package api.smartfarm.models.dtos.sectors;

import api.smartfarm.models.documents.CropType;
import api.smartfarm.models.entities.Sector;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SectorCropTypesDTO {
    private String id;
    private Integer row;
    private Integer column;
    private CropType cropType;

    public SectorCropTypesDTO(Sector sector, CropType cropType) {
        this.id = sector.getCode();
        this.row = sector.getRow();
        this.column = sector.getColumn();
        this.cropType = cropType;
    }
}
