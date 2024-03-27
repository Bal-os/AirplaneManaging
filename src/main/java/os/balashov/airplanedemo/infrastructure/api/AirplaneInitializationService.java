package os.balashov.airplanedemo.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import os.balashov.airplanedemo.domain.entities.Airplane;
import os.balashov.airplanedemo.domain.entities.AirplaneCharacteristics;
import os.balashov.airplanedemo.domain.repositories.AirplaneRepository;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
public class AirplaneInitializationService {
    @Autowired
    private AirplaneRepository airplaneRepository;

    @PostConstruct
    public void init() {
        Airplane airplane1 = new Airplane();
        airplane1.setId(1L);
        airplane1.setCharacteristics(AirplaneCharacteristics
                .builder()
                .maxChangeHeight(1.5)
                .maxChangeAngle(30)
                .maxChangeSpeed(1.68)
                .maxSpeed(597)
                .build());
        airplane1.setFlights(Collections.emptyList());
        airplaneRepository.save(airplane1);

        Airplane airplane2 = new Airplane();
        airplane2.setId(2L);
        airplane2.setCharacteristics(AirplaneCharacteristics
                .builder()
                .maxChangeAngle(41)
                .maxChangeSpeed(1.04)
                .maxChangeHeight(1.2)
                .maxSpeed(567)
                .build());
        airplane2.setFlights(Collections.emptyList());
        airplaneRepository.save(airplane2);

        Airplane airplane3 = new Airplane();
        airplane3.setId(3L);
        airplane3.setCharacteristics(AirplaneCharacteristics
                .builder()
                .maxChangeAngle(30)
                .maxChangeSpeed(2.24)
                .maxSpeed(564)
                .maxChangeHeight(1.8)
                .build());
        airplane3.setFlights(Collections.emptyList());
        airplaneRepository.save(airplane3);
    }
}
