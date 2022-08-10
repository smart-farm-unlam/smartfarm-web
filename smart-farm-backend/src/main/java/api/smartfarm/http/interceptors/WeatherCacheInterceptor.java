package api.smartfarm.http.interceptors;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WeatherCacheInterceptor implements ClientHttpRequestInterceptor {

    private final Cache<String, ClientHttpResponse> cache;

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherCacheInterceptor.class);

    public WeatherCacheInterceptor(
        Long size,
        Long ttl,
        int concurrencyLevel
    ) {
        cache = CacheBuilder.newBuilder()
            .maximumSize(size)
            .expireAfterWrite(ttl, TimeUnit.MINUTES)
            .concurrencyLevel(concurrencyLevel)
            .build();
    }

    @Override
    public ClientHttpResponse intercept(
        HttpRequest request,
        byte[] body,
        ClientHttpRequestExecution execution
    ) throws IOException {
        String key = request.getURI().getPath() + "?" + request.getURI().getQuery();
        ClientHttpResponse response = cache.getIfPresent(key);
        if (response != null) {
            LOGGER.info("Cache hit for key {}", key);
            return response;
        }

        response = execution.execute(request, body);
        cache.put(key, response);
        return response;
    }
}
