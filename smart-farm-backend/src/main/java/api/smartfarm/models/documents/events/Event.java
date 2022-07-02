package api.smartfarm.models.documents.events;

import api.smartfarm.models.documents.EventType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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

    private String farmId;

    public Event(EventType eventType) {
        this.eventType = eventType;
    }
}
