package api.smartfarm.models.dtos.farms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFarmRequestDTO {
    private Double latitude;
    private Double longitude;
}
