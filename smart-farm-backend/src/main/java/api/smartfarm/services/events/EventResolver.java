package api.smartfarm.services.events;

import api.smartfarm.models.documents.EventType;
import api.smartfarm.models.documents.events.Event;
import api.smartfarm.models.dtos.events.EventDTO;

public interface EventResolver {

    default boolean apply(String eventType) {
        return getEventName().equals(eventType);
    }

    String getEventName();

    Event mapEvent(EventDTO eventDTO, EventType eventType);

    Event getLastStartedEvent(Event event);
}
