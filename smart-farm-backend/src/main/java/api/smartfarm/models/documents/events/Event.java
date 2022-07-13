package api.smartfarm.models.documents.events;

import api.smartfarm.models.documents.EventType;
import api.smartfarm.models.exceptions.EventDTOParseException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "events")
public abstract class Event {

    @Id
    private String id;

    @JsonProperty("eventType")
    @DocumentReference
    private EventType eventType;

    private Date startDate;
    private Date endDate;
    private String farmId;

    protected Event(EventType eventType) {
        this.eventType = eventType;
    }

    protected void setDatesBasedOnStatus(Date eventDate, String status) {
        Date date = (eventDate != null)? eventDate : new Date();
        switch (status) {
            case "ON":
                this.startDate = date;
                break;
            case "OFF":
                this.endDate = date;
                break;
            default:
                throw new EventDTOParseException("Failed to parse [ " + this.getClass().getSimpleName() + " ] from DTO");
        }
    }
}
