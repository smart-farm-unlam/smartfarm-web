package api.smartfarm.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@CrossOrigin
public class HealthCheckController {

    @Value("${app.version}")
    private String version;

    @GetMapping
    public String healthCheck() {
        return "I'm ok and healthy, SmartFarm v" + version;
    }
}
