package io.github.sercasti.tracing.core;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Service to add metrics of the server to a http response.
 */
public interface Tracing {
    /**
     * Create a new metric with the current time as start time and the given name.
     *
     * @param name the name of the metric
     * @return the metric
     */
    TracingMetric start(@NonNull String name, @Nullable String description);

    List<TracingMetric> getMetrics();
}
