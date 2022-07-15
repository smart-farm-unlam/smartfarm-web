package api.smartfarm.models.dtos.farms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InitFarmRequestDTO {
    @NotNull
    private Double length;
    @NotNull
    private Double width;
}
