package api.smartfarm.clients.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DailyForecast {
    @JsonProperty("Date")
    private Date date;

    @JsonProperty("Temperature")
    private ForecastTemperature temperature;

    @JsonProperty("Day")
    private ForecastData day;

    @JsonProperty("Night")
    private ForecastData night;
}
