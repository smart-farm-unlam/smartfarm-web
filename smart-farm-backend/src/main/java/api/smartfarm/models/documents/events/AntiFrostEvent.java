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

    public AntiFrostEvent(AntiFrostEventDTO antiFrostEventDTO, EventType eventType) {
        super(eventType);
        super.setDatesBasedOnStatus(antiFrostEventDTO.getStatus());
    }
}
