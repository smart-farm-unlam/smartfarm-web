package api.smartfarm.models.entities;

import api.smartfarm.models.documents.CropType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Crop {
    @JsonProperty("cropType")
    @DocumentReference
    private CropType id;
    private List<Plant> plants;
}
