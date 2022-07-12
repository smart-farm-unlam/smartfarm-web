package api.smartfarm.services.events;

import api.smartfarm.models.documents.EventType;
import api.smartfarm.models.documents.events.AntiFrostEvent;
import api.smartfarm.models.documents.events.Event;
import api.smartfarm.models.dtos.events.AntiFrostEventDTO;
import api.smartfarm.models.dtos.events.EventDTO;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.EventDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AntiFrostEventResolver implements EventResolver {

    private final EventDAO eventDAO;

    private static final String EVENT_NAME = "AntiFrostEvent";

    @Autowired
    public AntiFrostEventResolver(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    @Override
    public Event mapEvent(EventDTO eventDTO, EventType eventType) {
        AntiFrostEventDTO antiFrostEventDTO = (AntiFrostEventDTO) eventDTO;
        return new AntiFrostEvent(antiFrostEventDTO, eventType);
    }

    @Override
    public Event getLastStartedEvent(Event event) {
        AntiFrostEvent antiFrostEvent = (AntiFrostEvent) event;

        String farmId = antiFrostEvent.getFarmId();
        String eventType = antiFrostEvent.getEventType().getId();

        return eventDAO.findLastEventByFarmAndEventType(farmId, eventType)
            .orElseThrow(() -> new NotFoundException("No started event in database for farm id[" + farmId + "]" +
                " and event type[" + eventType + "]"));
    }
}
