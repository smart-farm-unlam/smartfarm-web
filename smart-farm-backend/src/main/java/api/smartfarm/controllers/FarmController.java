package api.smartfarm.controllers;

import api.smartfarm.entities.EventParameter;
import api.smartfarm.entities.Farm;
import api.smartfarm.service.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/farms")
public class FarmController {

    private final FarmService farmService;

    @Autowired
    public FarmController(FarmService farmService) {
        this.farmService = farmService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Farm> getById(@PathVariable String id) {
        Optional<Farm> farm = farmService.getById(id);
        if (!farm.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(farm.get());
    }

    //"localhost:8080/farms/1/parameter?query=temperature"
 /*   @GetMapping("/{id}/event")
    public ResponseEntity<Farm> getParameter(
        @PathVariable String id,
        @RequestParam String query
    ) {
        EventParameter event = EventParameter.valueOf(query.toUpperCase());
        return farmService.getParameter(id, event);
    }

    @PostMapping
    public ResponseEntity<Farm> create(@RequestParam Farm farm) {
        farmService.save(farm);
    }
    */
}
