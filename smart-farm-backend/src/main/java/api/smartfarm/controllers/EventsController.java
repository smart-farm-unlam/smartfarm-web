package api.smartfarm.controllers;

import api.smartfarm.models.documents.events.Event;
import api.smartfarm.models.dtos.events.EventDTO;
import api.smartfarm.models.dtos.events.EventListsDTO;
import api.smartfarm.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@CrossOrigin
public class EventsController {

    private final EventService eventService;

    @Autowired
    public EventsController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/{farmId}")
    @ResponseStatus(HttpStatus.OK)
    public void registerEventLists(
        @PathVariable String farmId,
        @RequestBody EventDTO eventDTO
    ) {
        eventService.registerEvent(farmId, eventDTO);
    }

    @PostMapping("/multiple/{farmId}")
    @ResponseStatus(HttpStatus.OK)
    public void registerEventLists(
        @PathVariable String farmId,
        @RequestBody EventListsDTO eventListsDTO
    ) {
        eventService.registerEventLists(farmId, eventListsDTO);
    }

    @GetMapping("/{farmId}")
    public List<Event> getEvents(
        @PathVariable String farmId,
        @RequestParam(required = false) String eventType,
        @RequestParam(required = false) String sectorId
    ) {
        return eventService.getEvents(farmId, eventType, sectorId);
    }
}
