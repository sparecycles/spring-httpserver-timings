package io.github.sercasti.tracing.core;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class TracingMetric {
    private static final String SERVER_TIMING_HEADER_DUR = "dur=";
    private static final String SERVER_TIMING_HEADER_DESC = "desc=";

    private final String name;
    private final String description;
    private final Instant startTime;
    private Duration duration;

    public TracingMetric(@NonNull String name, @Nullable String description) {
        this(name, description, Clock.systemUTC());
    }

    public TracingMetric(@NonNull String name, @Nullable String description, @NonNull Clock clock) {
        this.name = name;
        this.description = description;
        this.startTime = Instant.now(clock);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Duration getDuration() {
        return duration;
    }

    public void stop() {
        if (duration != null) {
            throw new IllegalStateException("Metric '" + name + "' was already stopped!");
        }
        duration = Duration.between(startTime, Instant.now());
    }

    private String formatDuration() {
        if (null == duration) {
            return null;
        }

        return SERVER_TIMING_HEADER_DUR + (Math.max(1.0, duration.toNanos()) / 1000000.0);
    }

    private String formatDescription() {
        if (null == description) {
            return null;
        }

        return SERVER_TIMING_HEADER_DESC + rfc7230quotedString(description);
    }

    public String toString() {
        return Stream.of(
            name,
            formatDescription(),
            formatDuration()
        ).filter(Objects::nonNull).collect(Collectors.joining(";"));
    }

    private static String rfc7230quotedString(String raw) {
        return '"' + raw.replaceAll("[\"\\\\]", "\\\\$0") + '"';
    }
}
