package api.smartfarm.models.entities;

import lombok.*;

import java.util.ArrayList;
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

    public Sector(String code, String cropType, int row, int column) {
        this.code = code;
        this.row = row;
        this.column = column;
        this.crop = new Crop(cropType);
        this.sensors = new ArrayList<>();
    }

}
