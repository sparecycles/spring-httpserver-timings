package io.github.sercasti.tracing.interceptor;

import java.util.regex.Pattern;

import io.github.sercasti.tracing.Traceable;
import io.github.sercasti.tracing.core.TracingMetric;
import io.github.sercasti.tracing.core.Tracing;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.MergedAnnotations;

@Aspect
public class TracingInterceptor {

    private final Tracing tracing;

    private final Pattern argMatcher = Pattern.compile("\\{(\\d+)}");

    @Autowired
    public TracingInterceptor(Tracing tracing) {
        this.tracing = tracing;
    }

    @Around("@annotation(io.github.sercasti.tracing.Traceable)")
    public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Traceable traceable = MergedAnnotations.from(signature.getMethod())
            .get(Traceable.class)
            .synthesize();

        String traceName = traceable.name().isEmpty() ? signature.getName() : traceable.name();
        String traceDescription = traceable.description().isEmpty() ? null : traceable.description();

        if (traceDescription != null) {
            traceDescription = argMatcher.matcher(traceDescription).replaceAll(match -> {
                int argIndex = Integer.parseInt(match.group(1));
                return String.valueOf(joinPoint.getArgs()[argIndex]);
            });
        }

        TracingMetric metric = tracing.start(traceName, traceDescription);

        try {
            return joinPoint.proceed();
        } finally {
            metric.stop();
        }
    }

}