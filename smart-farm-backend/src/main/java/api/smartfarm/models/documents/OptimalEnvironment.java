package api.smartfarm.models.documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OptimalEnvironment {
    private String germinationTemperature;
    private String optimalTemperature;
    private String irrigation;
    private String humidity;
    private String ph;
}
