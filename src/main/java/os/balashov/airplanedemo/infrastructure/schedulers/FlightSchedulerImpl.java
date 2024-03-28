package os.balashov.airplanedemo.infrastructure.schedulers;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import os.balashov.airplanedemo.application.interfaces.AirplaneService;
import os.balashov.airplanedemo.domain.entities.Airplane;
import os.balashov.airplanedemo.domain.services.FlightScheduler;

@Service
@AllArgsConstructor
public class FlightSchedulerImpl implements FlightScheduler {
    private final AirplaneService airplaneService;

    @Override
    @Scheduled(fixedRateString = "${schedule.rate}")
    public void updateAirplanes() {
        for (Airplane airplane : airplaneService.getAllAirplanes()) {
            airplaneService.manageAirplaneFlight(airplane);
        }
    }
}
