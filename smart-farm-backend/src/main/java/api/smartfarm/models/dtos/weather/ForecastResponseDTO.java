package api.smartfarm.models.dtos.weather;

import api.smartfarm.clients.weather.model.DailyForecast;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ForecastResponseDTO {
    private Date date;
    private String minTemperature;
    private String maxTemperature;
    private ForecastDataDTO day;
    private ForecastDataDTO night;

    public ForecastResponseDTO(DailyForecast dailyForecast) {
        this.date = dailyForecast.getDate();
        this.minTemperature = dailyForecast.getTemperature().getMinimum().getCelsiusValue();
        this.maxTemperature = dailyForecast.getTemperature().getMaximum().getCelsiusValue();
        this.day = new ForecastDataDTO(dailyForecast.getDay());
        this.night = new ForecastDataDTO(dailyForecast.getNight());
    }
}
