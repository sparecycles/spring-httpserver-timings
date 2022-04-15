package io.github.sercasti.tracing.filter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import io.github.sercasti.tracing.core.Tracing;
import io.github.sercasti.tracing.core.TracingImpl;

/**
 * Unit tests for {@link TracingFilter}.
 */
public class TracingFilterTest {

    @Test
    public void testDoFilter() throws IOException, ServletException {
        Tracing testTracing = new TracingImpl();
        TracingFilter filter = new TracingFilter(testTracing);
        HttpServletRequest request = mock(HttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        testTracing.start("test", null).stop();
        filter.doFilterInternal(request, response, filterChain);
        assertNotNull(response.getHeader("Server-Timing"));
    }

}
