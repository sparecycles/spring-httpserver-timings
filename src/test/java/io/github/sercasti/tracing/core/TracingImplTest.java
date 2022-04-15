package io.github.sercasti.tracing.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

/**
 * Unit tests for {@link TracingImpl}.
 */
public class TracingImplTest {

    private static final String NAME = "cache";
    private static final String DESC = "Cache Read";

    @Test
    public void testStart() {
        final TracingImpl tracing = new TracingImpl();
        final TracingMetric metric = tracing.start(NAME, DESC);
        assertEquals(metric.getName(), NAME);
        assertEquals(metric.getDescription(), DESC);
        assertNull(metric.getDuration());
    }

    @Test
    public void testConvert() throws InterruptedException {
        final TracingImpl tracing = new TracingImpl();
        final TracingMetric metric = tracing.start(NAME, DESC);
        Thread.sleep(100);
        metric.stop();
        assertTrue(metric.toString().startsWith("cache;desc=\"Cache Read\";dur="));
    }

}
