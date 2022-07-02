package api.smartfarm.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Crop {
    private String type;
    private List<Plant> plants;
}
