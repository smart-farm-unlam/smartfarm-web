package api.smartfarm.models.dtos.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property="eventType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = IrrigationEventDTO.class, name = "IrrigationEvent")
})
public abstract class EventDTO{

    private String eventType;

}
