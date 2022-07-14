package api.smartfarm.models.dtos.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "eventType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = IrrigationEventDTO.class, name = "IrrigationEvent"),
        @JsonSubTypes.Type(value = AntiFrostEventDTO.class, name = "AntiFrostEvent")
})
public abstract class EventDTO {
    private String eventType;
    private Date date;
    private String status;
}
