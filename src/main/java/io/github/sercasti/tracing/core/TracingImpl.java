package io.github.sercasti.tracing.core;

import java.util.ArrayList;
import java.util.List;

public class TracingImpl implements Tracing {
    private final List<TracingMetric> metrics = new ArrayList<>();

    @Override
    public synchronized TracingMetric start(String name, String description) {
        TracingMetric metric = new TracingMetric(name, description);
        metrics.add(metric);
        return metric;
    }

    @Override
    public List<TracingMetric> getMetrics() {
        return metrics;
    }
}
