package api.smartfarm.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CropDTO {
    private String type;
    private String sectorCode;
}
