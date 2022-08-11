package api.smartfarm.models.dtos.weather;

import api.smartfarm.clients.weather.model.ForecastData;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ForecastDataDTO {
    private String weatherStatus;
    private int weatherIcon;
    private int precipitationProbability;
    private int thunderstormProbability;
    private int rainProbability;
    private String windSpeed;

    public ForecastDataDTO(ForecastData forecastData) {
        this.weatherStatus = forecastData.getIconPhrase();
        this.weatherIcon = forecastData.getIcon();
        this.precipitationProbability = forecastData.getPrecipitationProbability();
        this.thunderstormProbability = forecastData.getThunderstormProbability();
        this.rainProbability = forecastData.getRainProbability();
        this.windSpeed = forecastData.getWind().getSpeed().getCompleteValue();
    }
}
