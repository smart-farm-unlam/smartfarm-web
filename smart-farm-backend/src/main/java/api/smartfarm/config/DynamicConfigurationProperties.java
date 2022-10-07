package api.smartfarm.config;

import api.smartfarm.models.documents.DynamicConfiguration;
import api.smartfarm.repositories.DynamicConfigDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static api.smartfarm.models.documents.DynamicConfiguration.CronType.*;

@Configuration
public class DynamicConfigurationProperties {

    private final DynamicConfigDAO dynamicConfigDAO;

    @Autowired
    public DynamicConfigurationProperties(DynamicConfigDAO dynamicConfigDAO) {
        this.dynamicConfigDAO = dynamicConfigDAO;
    }

    @Bean(name = "measuresCronConfiguration")
    public DynamicConfiguration measuresCronConfiguration() {
        return dynamicConfigDAO.findByCronType(MEASURES_CONTROL.name());
    }

    @Bean(name = "measuresCronInterval")
    public String measuresCronInterval() {
        DynamicConfiguration parameterControl = dynamicConfigDAO.findByCronType(MEASURES_CONTROL.name());
        return parameterControl.getCronInterval();
    }

    @Bean(name = "weatherCronConfiguration")
    public DynamicConfiguration weatherCronConfiguration() {
        return dynamicConfigDAO.findByCronType(WEATHER_EVENT_CONTROL.name());
    }

    @Bean(name = "weatherCronInterval")
    public String weatherCronInterval() {
        DynamicConfiguration weatherControl = dynamicConfigDAO.findByCronType(WEATHER_EVENT_CONTROL.name());
        return weatherControl.getCronInterval();
    }

    @Bean(name = "disconnectionCronConfiguration")
    public DynamicConfiguration disconnectionCronConfiguration() {
        return dynamicConfigDAO.findByCronType(DISCONNECTION_CONTROL.name());
    }

    @Bean(name = "disconnectControlControlCronInterval")
    public String disconnectControlControlCronInterval() {
        DynamicConfiguration weatherControl = dynamicConfigDAO.findByCronType(DISCONNECTION_CONTROL.name());
        return weatherControl.getCronInterval();
    }
}