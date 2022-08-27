package api.smartfarm.clients.diagnostic;

import api.smartfarm.clients.diagnostic.model.DiagnosticResponse;
import api.smartfarm.models.exceptions.FailedDependencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DiagnosticClient {

    @Value("${diagnostic.api.host}")
    private String host;

    private final RestTemplate restTemplate;

    private static final String DIAGNOSTIC_URL = "%s/diagnostic?url=%s";

    private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosticClient.class);

    @Autowired
    public DiagnosticClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DiagnosticResponse diagnosticPlantHealth(String mediaResourceUrl) {
        String url = String.format(DIAGNOSTIC_URL, host, mediaResourceUrl);

        try {
            DiagnosticResponse response = restTemplate.getForObject(url, DiagnosticResponse.class);
            LOGGER.info("DiagnosticResponse {}", response);
            return response;
        } catch (Exception e) {
            throw new FailedDependencyException("Failed to obtain current weather from Diagnostic Client");
        }
    }

}
