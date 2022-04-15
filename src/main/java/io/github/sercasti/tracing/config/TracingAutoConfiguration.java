package io.github.sercasti.tracing.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.RequestScope;

import io.github.sercasti.tracing.core.Tracing;
import io.github.sercasti.tracing.core.TracingImpl;
import io.github.sercasti.tracing.filter.TracingFilter;
import io.github.sercasti.tracing.interceptor.TracingInterceptor;

@ConditionalOnClass(Tracing.class)
@ConditionalOnExpression("${sercasti.tracing.disabled:'false'} != 'true'")
public class TracingAutoConfiguration {
    @ConditionalOnMissingBean
    @Bean
    @RequestScope
    public Tracing tracing() {
        return new TracingImpl();
    }

    @Bean
    public FilterRegistrationBean<TracingFilter> tracingFilter(Tracing tracing) {
        return new FilterRegistrationBean<>(new TracingFilter(tracing));
    }

    @Bean
    @ConditionalOnMissingBean
    public TracingInterceptor tracingInterceptor(Tracing tracing) {
        return new TracingInterceptor(tracing);
    }
}
