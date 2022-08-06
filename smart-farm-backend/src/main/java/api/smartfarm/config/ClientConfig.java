package api.smartfarm.config;

import api.smartfarm.clients.weather.WeatherClient;
import api.smartfarm.http.interceptors.WeatherCacheInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {

    @Value("${weather-client.cache.size}")
    private Long size;

    @Value("${weather-client.cache.concurrency-level}")
    private int concurrencyLevel;

    @Value("${weather-client.cache.ttl}")
    private Long ttl;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean("restTemplateCacheable")
    public RestTemplate restTemplateCacheable(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
            .additionalInterceptors(new WeatherCacheInterceptor(size, ttl, concurrencyLevel))
            .build();

        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        return restTemplate;
    }

    @Bean
    public WeatherClient weatherClient(
        @Qualifier("restTemplateCacheable") RestTemplate restTemplate
    ) {
        return new WeatherClient(restTemplate);
    }

}
