package org.cv.cloudwatch.properties;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "management.metrics.export.cloudwatch")
public class CloudWatchProperties extends StepRegistryProperties {

    private String namespace = "BhushanCV/Metrics";
    private boolean enabled = true;

    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Duration getStep() {
        return Duration.ofSeconds(30);
    }

  /*  @Override
    public Integer getBatchSize() {
        return 5;
    }*/
}