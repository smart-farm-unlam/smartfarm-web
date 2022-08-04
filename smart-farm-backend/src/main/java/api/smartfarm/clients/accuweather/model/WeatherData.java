package api.smartfarm.clients.accuweather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class WeatherData {
    @JsonProperty("WeatherText")
    private String weatherText;

    @JsonProperty("Wind")
    private Wind wind;

    @JsonProperty("UVIndex")
    private int uvIndex;

    @JsonProperty("Precip1hr")
    private Precipitation precipitation;
}
