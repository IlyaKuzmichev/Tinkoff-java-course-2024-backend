package edu.java.bot.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrometerConfig {
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public Counter processedMessageCounter(ApplicationConfig config, MeterRegistry registry) {
        return Counter.builder(config.micrometer().processedMessageCounter().name())
            .description(config.micrometer().processedMessageCounter().description())
            .tag("application", applicationName)
            .register(registry);
    }
}
