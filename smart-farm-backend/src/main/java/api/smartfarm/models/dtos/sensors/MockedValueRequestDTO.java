package api.smartfarm.models.dtos.sensors;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MockedValueRequestDTO {
    @NotNull
    private Double value;
    @NotEmpty
    private String status;
}
