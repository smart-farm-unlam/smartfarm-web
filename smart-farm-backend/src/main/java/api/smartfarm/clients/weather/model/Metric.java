package api.smartfarm.clients.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Metric {
    @JsonProperty("Value")
    private Double value;

    @JsonProperty("Unit")
    private String unit;

    public String getCompleteValue() {
        return value + " " + unit;
    }
}
