package api.smartfarm.models.dtos.sectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddSectorRequestDTO {
    @NotEmpty
    private String cropType;

    private int plantsCount;
}
