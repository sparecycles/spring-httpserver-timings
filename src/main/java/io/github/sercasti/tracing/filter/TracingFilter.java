package io.github.sercasti.tracing.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import io.github.sercasti.tracing.core.Tracing;
import io.github.sercasti.tracing.core.TracingMetric;

public class TracingFilter extends OncePerRequestFilter {
    public static final String SERVER_TIMING_HEADER = "Server-Timing";

    private final Tracing tracing;

    public TracingFilter(Tracing tracing) {
        this.tracing = tracing;
    }

    private void applyTrailingServerTimings(HttpServletResponse response, TracingMetric total) {
        Supplier<Map<String, String>> existingTrailerFieldSupplier = response.getTrailerFields();

        List<TracingMetric> requestMetrics = tracing.getMetrics();

        response.setTrailerFields(() -> {
            Map<String, String> trailerFields = new HashMap<>(
                Optional.ofNullable(existingTrailerFieldSupplier)
                    .map(Supplier::get)
                    .orElse(Collections.emptyMap())
            );

            if (total != null && total.getDuration() == null) {
                total.stop();
            }

            trailerFields.put(SERVER_TIMING_HEADER, requestMetrics.stream()
                .map(TracingMetric::toString)
                .collect(Collectors.joining(",")));

            return trailerFields;
        });
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        applyTrailingServerTimings(response, tracing.start("total", null));

        filterChain.doFilter(request, response);
    }
}
