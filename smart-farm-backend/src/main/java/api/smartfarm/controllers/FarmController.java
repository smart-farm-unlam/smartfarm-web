package api.smartfarm.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/farms")
public class FarmController {

    @GetMapping
    public String event() {
        return "Hello world con deploy atom en azure";
    }

}
