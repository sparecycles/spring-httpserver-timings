package io.github.sercasti.tracing.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Unit tests for {@link TracingMetric}.
 */
public class MetricTest {
    private static final String NAME = "John";
    private static final String DESC = "Lennon";
    
    @Test
    public void testShouldEqualParams()  {
        final TracingMetric metric = new TracingMetric(NAME, DESC);
        assertEquals(metric.getName(), NAME);
        assertEquals(metric.getDescription(), DESC);
        assertNull(metric.getDuration());
    }
    
    @Test
    public void testShouldNotStop() {
        TracingMetric metric = new TracingMetric(NAME, DESC);

        metric.stop();

        assertNotNull(metric.getDuration());

        Assertions.assertThrows(IllegalStateException.class, metric::stop);
    }

}
