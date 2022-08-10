package api.smartfarm.models.dtos;

import api.smartfarm.clients.weather.model.WeatherData;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class WeatherResponseDTO {
    private Date dateTime;
    private String temperature;
    private String humidity;
    private String weatherStatus;
    private int weatherIcon;
    private String windSpeed;
    private int uvIndex;
    private String precipitation;

    public WeatherResponseDTO(WeatherData weatherData) {
        this.dateTime = new Date();
        this.temperature = weatherData.getTemperature().getMetric().getValue() + "°" + weatherData.getTemperature().getMetric().getUnit();
        this.humidity = weatherData.getRelativeHumidity() + "%";
        this.weatherStatus = weatherData.getWeatherText();
        this.weatherIcon = weatherData.getWeatherIcon();
        this.windSpeed = weatherData.getWind().getSpeed().getMetric().getCompleteValue();
        this.uvIndex = weatherData.getUvIndex();
        this.precipitation = weatherData.getPrecipitation().getMetric().getCompleteValue();
    }
}
