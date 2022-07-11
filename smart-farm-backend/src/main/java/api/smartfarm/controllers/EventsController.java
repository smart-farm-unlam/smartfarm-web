package api.smartfarm.controllers;


import api.smartfarm.models.documents.events.Event;
import api.smartfarm.models.dtos.events.EventDTO;
import api.smartfarm.services.FarmService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@CrossOrigin
public class EventsController {

    private final FarmService farmService;

    public EventsController(FarmService farmService) {
        this.farmService = farmService;
    }

    @PostMapping("/{farmId}")
    @ResponseStatus(HttpStatus.OK)
    public void registerEvent(
        @PathVariable String farmId,
        @RequestBody EventDTO eventDTO
    ) {
        farmService.registerEvent(farmId, eventDTO);
    }

    @GetMapping("/{farmId}")
    public List<Event> getEvents(@PathVariable String farmId) {
        return farmService.getEvents(farmId);
    }

}
