package api.smartfarm.models.dtos.weather;

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
    private String alertTemperature;

    public WeatherResponseDTO(WeatherData weatherData) {
        this.dateTime = new Date();
        this.temperature = weatherData.getTemperature().getMetric().getCelsiusValue();
        this.humidity = weatherData.getRelativeHumidity() + "%";
        this.weatherStatus = weatherData.getWeatherText();
        this.weatherIcon = weatherData.getWeatherIcon();
        this.windSpeed = weatherData.getWind().getSpeed().getMetric().getCompleteValue();
        this.uvIndex = weatherData.getUvIndex();
        this.precipitation = weatherData.getPrecipitation().getMetric().getCompleteValue();
        this.alertTemperature = AlertTemperature.buildTemperatureAlert(weatherData.getTemperature().getMetric().getValue());
    }

}
