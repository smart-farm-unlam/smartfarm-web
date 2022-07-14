package api.smartfarm.services;

import api.smartfarm.models.documents.EventType;
import api.smartfarm.models.documents.Farm;
import api.smartfarm.models.documents.events.Event;
import api.smartfarm.models.dtos.events.EventDTO;
import api.smartfarm.models.dtos.events.EventListsDTO;
import api.smartfarm.models.exceptions.NotFoundException;
import api.smartfarm.repositories.EventDAO;
import api.smartfarm.repositories.EventTypeDAO;
import api.smartfarm.services.events.EventResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final List<EventResolver> eventsResolvers;
    private final EventDAO eventDAO;
    private final EventTypeDAO eventTypeDAO;
    private final FarmService farmService;

    @Autowired
    public EventService(
        List<EventResolver> eventsResolvers,
        EventDAO eventDAO,
        EventTypeDAO eventTypeDAO,
        FarmService farmService
    ) {
        this.eventsResolvers = eventsResolvers;
        this.eventDAO = eventDAO;
        this.eventTypeDAO = eventTypeDAO;
        this.farmService = farmService;
    }

    public List<Event> getEvents(String farmId) {
        return eventDAO.findByFarmId(farmId);
    }

    public void registerEvent(String farmId, EventDTO eventDTO) {
        EventType eventType = getEventTypeById(eventDTO.getEventType());

        EventResolver eventResolver = eventsResolvers.stream()
            .filter(r -> r.apply(eventType.getId()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Event type " + eventType.getId() + " not supported"));

        Event event = eventResolver.mapEvent(eventDTO, eventType);
        handleEvent(event, farmId, eventResolver);
    }

    private void handleEvent(Event event, String farmId, EventResolver eventResolver) {
        Farm farm = farmService.getFarmById(farmId);
        event.setFarmId(farmId);
        if (event.getStartDate() != null) {
            eventDAO.save(event);
            if (farm.getEvents() == null) {
                farm.setEvents(new ArrayList<>());
            }
            farm.getEvents().add(event.getId());
        } else if (event.getEndDate() != null) {
            Event lastStartedEvent = eventResolver.getLastStartedEvent(event);
            lastStartedEvent.setEndDate(event.getEndDate());
            eventDAO.save(lastStartedEvent);
        }
        farmService.update(farm);
    }

    private EventType getEventTypeById(String id) {
        return eventTypeDAO.findById(id).orElseThrow(
            () -> new NotFoundException("Event type: " + id + " not found in database")
        );
    }

    public void registerEventLists(String farmId, EventListsDTO eventListDTO) {
        eventListDTO.getIrrigationEvents().forEach(event -> registerEvent(farmId, event));
        eventListDTO.getAntiFrostEvents().forEach(event -> registerEvent(farmId, event));
    }
}
