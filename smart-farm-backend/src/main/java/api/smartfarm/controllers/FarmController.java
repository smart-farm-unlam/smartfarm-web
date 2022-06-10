package api.smartfarm.controllers;

import api.smartfarm.entities.events.EventParameter;
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
        return farm.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //"localhost:8080/farms/1/parameter?query=temperature"
    @GetMapping("/{id}/event")
    public Double getParameter(
            @PathVariable String id,
            @RequestParam String query
    ) {
        EventParameter event = null;
        try {
            event = (EventParameter) Class.forName(buildEventParameterQuery(query)).newInstance();
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return farmService.getParameter(id, event);
    }

    private String buildEventParameterQuery(String query) {
        query = query.substring(0, 1).toUpperCase() + query.substring(1); // uppercase first letter
        return "api.smartfarm.entities.events.".concat(query); /// concat with path
    }

    @PostMapping
    public ResponseEntity<Farm> create(@RequestParam Farm farm) {
        farmService.save(farm);
        return null;
    }
}
