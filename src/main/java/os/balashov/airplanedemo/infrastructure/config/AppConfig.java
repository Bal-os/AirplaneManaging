package os.balashov.airplanedemo.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import os.balashov.airplanedemo.application.PlaneCalculationImpl;
import os.balashov.airplanedemo.domain.services.PlaneCalculation;

@Configuration
@EnableScheduling
public class AppConfig {
    @Bean
    public PlaneCalculation planeCalculation() {
        return new PlaneCalculationImpl();
    }
}

