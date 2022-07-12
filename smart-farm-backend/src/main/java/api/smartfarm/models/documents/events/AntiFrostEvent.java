package api.smartfarm.models.documents.events;

import api.smartfarm.models.documents.EventType;
import api.smartfarm.models.dtos.events.AntiFrostEventDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AntiFrostEvent extends Event {

    private static final EventType EVENT_TYPE = new EventType("AntiFrostEvent");

    public AntiFrostEvent(AntiFrostEventDTO antiFrostEventDTO) {
        super(EVENT_TYPE);
        this.setStatus(antiFrostEventDTO.getStatus());
        super.setDatesBasedOnStatus();
    }
}
