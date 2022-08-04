package api.smartfarm.clients.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class LocationData {
    @JsonProperty("Key")
    private String key;

    @JsonProperty("LocalizedName")
    private String localizedName;
}
