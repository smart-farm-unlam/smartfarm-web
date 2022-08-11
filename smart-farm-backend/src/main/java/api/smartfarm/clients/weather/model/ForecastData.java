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
public class ForecastData {
    @JsonProperty("Icon")
    private int icon;

    @JsonProperty("IconPhrase")
    private String iconPhrase;

    @JsonProperty("PrecipitationProbability")
    private int precipitationProbability;

    @JsonProperty("ThunderstormProbability")
    private int thunderstormProbability;

    @JsonProperty("RainProbability")
    private int rainProbability;

    @JsonProperty("Wind")
    private ForecastWind wind;
}
