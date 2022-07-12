package api.smartfarm.services.events;

import api.smartfarm.models.documents.EventType;
import api.smartfarm.models.documents.events.Event;
import api.smartfarm.models.documents.events.IrrigationEvent;
import api.smartfarm.models.dtos.events.EventDTO;
import api.smartfarm.models.dtos.events.IrrigationEventDTO;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.EventDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IrrigationEventResolver implements EventResolver {

    private final EventDAO eventDAO;

    private static final String EVENT_NAME = "IrrigationEvent";

    @Autowired
    public IrrigationEventResolver(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    @Override
    public Event mapEvent(EventDTO eventDTO, EventType eventType) {
        IrrigationEventDTO irrigationEventDTO = (IrrigationEventDTO) eventDTO;
        return new IrrigationEvent(irrigationEventDTO, eventType);
    }

    @Override
    public Event getLastStartedEvent(Event event) {
        IrrigationEvent irrigationEvent = (IrrigationEvent) event;

        String farmId = irrigationEvent.getFarmId();
        String sectorId = irrigationEvent.getSectorId();
        String eventType = irrigationEvent.getEventType().getId();

        return eventDAO.findLastEventByFarmAndSectorAndEventType(farmId, sectorId, eventType)
            .orElseThrow(() -> new NotFoundException("No started event in database for farm id[" + farmId + "]" +
                ", sector id[" + sectorId + "] and event type[" + eventType + "]"));
    }

}
