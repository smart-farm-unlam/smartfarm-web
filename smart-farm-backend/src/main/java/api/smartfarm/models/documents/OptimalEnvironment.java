package api.smartfarm.models.documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OptimalEnvironment {
    public String germinationTemperature;
    public String optimalTemperature;
    public String irrigation;
    public String humidity;
    public String ph;
}
