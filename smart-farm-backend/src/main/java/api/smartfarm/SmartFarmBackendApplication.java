package api.smartfarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartFarmBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartFarmBackendApplication.class, args);
    }

}
