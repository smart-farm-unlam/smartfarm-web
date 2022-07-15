package api.smartfarm.models.dtos.farms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateFarmRequestDTO {
    private String name;
    @NotEmpty
    private String userId;
}
