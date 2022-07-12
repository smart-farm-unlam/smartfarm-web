package api.smartfarm.controllers;


import api.smartfarm.models.documents.events.Event;
import api.smartfarm.models.dtos.events.EventDTO;
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
    public void registerEvent(
        @PathVariable String farmId,
        @RequestBody EventDTO eventDTO
    ) {
        eventService.registerEvent(farmId, eventDTO);
    }

    @GetMapping("/{farmId}")
    public List<Event> getEvents(@PathVariable String farmId) {
        return eventService.getEvents(farmId);
    }

}
