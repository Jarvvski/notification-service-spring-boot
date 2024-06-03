package com.github.jarvvski.service.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricRecorder {
    private final Counter notificationsSent;
    private final Counter notificationsFailed;
    private final Counter notificationsPending;

    public MetricRecorder(MeterRegistry meterRegistry) {
        this.notificationsSent = Counter.builder("notifications_sent")
            .description("Number of notifications sent")
            .register(meterRegistry);
        this.notificationsFailed = Counter.builder("notifications_failed")
            .description("Number of notifications failed to send")
            .register(meterRegistry);
        this.notificationsPending = Counter.builder("notifications_pending")
            .description("Number of notifications pending")
            .register(meterRegistry);
    }


    public void incrementNotificationsSent() {
        notificationsSent.increment();
    }

    public void incrementNotificationsFailed() {
        notificationsFailed.increment();
    }

    public void incrementNotificationsPending() {
        notificationsPending.increment();
    }
}
