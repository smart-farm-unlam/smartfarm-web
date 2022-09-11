package api.smartfarm.config;

import api.smartfarm.models.documents.DynamicConfiguration;
import api.smartfarm.repositories.DynamicConfigDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamicConfigurationProperties {

    private static DynamicConfiguration dynamicConfiguration;
    private final DynamicConfigDAO dynamicConfigDAO;

    @Autowired
    public DynamicConfigurationProperties(
            DynamicConfigDAO dynamicConfigDAO
    ) {
        this.dynamicConfigDAO = dynamicConfigDAO;
        dynamicConfiguration = dynamicConfigDAO.findAll().get(0);
    }

    public static DynamicConfiguration getDynamicConfiguration() {
        return dynamicConfiguration;
    }

    @Bean(name = "cronInterval")
    public String getCronInterval() {
        return dynamicConfigDAO.findAll().get(0).getCronInterval();
    }
}