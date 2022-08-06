package api.smartfarm.http.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class LoggingFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);
    private static final BigDecimal ONE_MILLIS_IN_NANO = new BigDecimal(1000000);

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws ServletException, IOException {
        long startTime = System.nanoTime();

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        this.logRequest(httpRequest);
        chain.doFilter(httpRequest, httpResponse);
        this.logResponse(startTime);
    }

    private void logRequest(HttpServletRequest request) {
        String queryString = (request.getQueryString() != null) ? "?".concat(request.getQueryString()) : "";
        String uriQueryString = request.getRequestURI().concat(queryString);

        LOGGER.info("[REQUEST] Method[{}] URI[{}]", request.getMethod(), uriQueryString);
    }

    private void logResponse(long startTime) {
        BigDecimal spentTime = new BigDecimal(System.nanoTime() - startTime);
        LOGGER.info("[RESPONSE] Spent[{}]", spentTime.divide(ONE_MILLIS_IN_NANO, 2, RoundingMode.CEILING));
    }

}
