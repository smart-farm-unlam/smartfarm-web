package api.smartfarm.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@CrossOrigin
public class HealthCheckController {

    @GetMapping
    public String healthCheck() {
        return "I'm ok and healthy";
    }
}
