package org.cv.cloudwatch.service;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.binder.MeterBinder;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

import java.util.Map;

public class MetricService {
    private MeterRegistry cloudwatchMeterRegistry;
    private Counter hitCounter;
    private Gauge gauge;

    public MetricService(CloudWatchConfig config) {
        this.cloudwatchMeterRegistry = new CloudWatchMeterRegistry(config, Clock.SYSTEM, CloudWatchAsyncClient.create());
        hitCounter = Counter.builder("BKSiteHitCounter").baseUnit("Hits").description("Page Hit Counter").register(cloudwatchMeterRegistry);
    }

    public void initiateGauge(Map<String, Boolean> connectionPool) {
        gauge = Gauge.builder("ConnectionPoolGauge", connectionPool, value -> {
                    Double usedConnections = 0.0;
                    for (Map.Entry<String, Boolean> entry : value.entrySet()) {
                        if (entry.getValue().equals(Boolean.FALSE)) {
                            usedConnections++;
                        }
                    }
                    return usedConnections;
                })
                .tag("GaugeName", "Bhushan's Gauge")
                .strongReference(true)
                .baseUnit("Used Connections")
                .description("Gauge to monitor connection pool")
                .register(cloudwatchMeterRegistry);
    }

    public void incrementHitCounter() {
        hitCounter.increment();
    }

    public Counter getHitCounter() {
        return hitCounter;
    }

    public Gauge getGauge() {
        return gauge;
    }
}
