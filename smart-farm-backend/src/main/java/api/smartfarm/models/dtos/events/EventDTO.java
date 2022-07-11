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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "eventType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = IrrigationEventDTO.class, name = "IrrigationEvent"),
        @JsonSubTypes.Type(value = AntiFrostEventDTO.class, name = "AntiFrostEvent")
})
public abstract class EventDTO {
    private String eventType;
}
