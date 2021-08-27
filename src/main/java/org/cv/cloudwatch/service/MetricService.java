package org.cv.cloudwatch.service;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

import java.util.Map;

public class MetricService {
    private MeterRegistry meterRegistry;
    private Counter hitCounter;
    private Gauge gauge;
    //private CompositeMeterRegistry compositeMeterRegistry;
    private  Map<String, Boolean> connectionPool;

    public MetricService(CloudWatchConfig config) {
        // compositeMeterRegistry = new CompositeMeterRegistry(); // to hold multiple metric registries
        this.meterRegistry = new CloudWatchMeterRegistry(config, Clock.SYSTEM, CloudWatchAsyncClient.create());
        //        compositeMeterRegistry.add(meterRegistry);

        hitCounter = Counter.builder("HitCounter").baseUnit("Hits").description("Page Hit Counter").register(meterRegistry);
        gauge = Gauge.builder("ConnectionPoolGauge", this.connectionPool, value -> {
                    Double usedConnections = 0.0;
                    for (Map.Entry<String, Boolean> entry : value.entrySet()) {
                        if (entry.getValue().equals(Boolean.FALSE)) {
                            usedConnections++;
                        }
                    }
                    return usedConnections;
                })
                .baseUnit("UsedConnections")
                .description("Gauge to monitor connection pool")
                .register(meterRegistry);
    }

    public void setConnectionPool(Map<String, Boolean> connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void incrementHitCounter() {
        hitCounter.increment();
    }
}
