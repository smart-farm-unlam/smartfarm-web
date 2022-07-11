package api.smartfarm.models.documents.events;

import api.smartfarm.models.documents.EventType;
import api.smartfarm.models.dtos.events.EventDTO;
import api.smartfarm.models.dtos.events.IrrigationEventDTO;
import api.smartfarm.models.exceptions.EventDTOParseException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IrrigationEvent extends Event {

    private String sectorId;

    private static final EventType EVENT_TYPE = new EventType("IrrigationEvent");

    public IrrigationEvent(IrrigationEventDTO irrigationEventDTO) {
        super(EVENT_TYPE);
        this.sectorId = irrigationEventDTO.getSectorId();
        this.setStatus(irrigationEventDTO.getStatus());
        super.setDatesBasedOnStatus();
    }


}
