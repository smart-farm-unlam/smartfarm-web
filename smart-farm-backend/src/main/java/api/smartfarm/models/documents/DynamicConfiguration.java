package api.smartfarm.models.documents;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "dynamic_config")
public class DynamicConfiguration {
    private CronType cronType;
    private String cronInterval;
    private long timeToReSendNotification;

    public enum CronType {
        MEASURES_CONTROL,
        WEATHER_EVENT_CONTROL
    }
}