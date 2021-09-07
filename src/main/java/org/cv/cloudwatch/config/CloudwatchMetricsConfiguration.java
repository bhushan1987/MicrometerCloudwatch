package org.cv.cloudwatch.config;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import org.cv.cloudwatch.properties.CloudWatchProperties;
import org.cv.cloudwatch.service.MetricService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CloudwatchMetricsConfiguration {

    @Bean
    public MetricService metricService(CloudWatchConfig config) {
        return new MetricService(config);
    }

    @Bean
    public CloudWatchConfig cloudWatchConfig(CloudWatchProperties properties) {
        return new CloudWatchConfig() {
            @Override
            public String prefix() {
                return null;
            }

            @Override
            public String namespace() {
                return properties.getNamespace();
            }


            @Override
            public Duration step() {
                return properties.getStep();
            }

            @Override
            public boolean enabled() {
                return properties.isEnabled();
            }

            @Override
            public int batchSize() {
                return properties.getBatchSize();
            }

            @Override
            public String get(String s) {
                return null;
            }
        };
    }
}

