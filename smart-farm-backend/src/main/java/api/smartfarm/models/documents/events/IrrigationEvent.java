package api.smartfarm.models.documents.events;

import api.smartfarm.models.documents.EventType;
import api.smartfarm.models.dtos.events.IrrigationEventDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IrrigationEvent extends Event {

    private String sectorId;

    public IrrigationEvent(IrrigationEventDTO irrigationEventDTO, EventType eventType) {
        super(eventType);
        this.setSectorId(irrigationEventDTO.getSectorId());
        super.setDatesBasedOnStatus(irrigationEventDTO.getStatus());
    }

}
