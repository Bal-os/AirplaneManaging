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
        airplane1.setCharacteristics(
                new AirplaneCharacteristics(1000, 12, 50, 60));
        airplane1.setFlights(Collections.emptyList());
        airplaneRepository.save(airplane1);

        Airplane airplane2 = new Airplane();
        airplane2.setId(2L);
        airplane2.setCharacteristics(
                new AirplaneCharacteristics(833, 10, 53, 46));
        airplane2.setFlights(Collections.emptyList());
        airplaneRepository.save(airplane2);

        Airplane airplane3 = new Airplane();
        airplane3.setId(3L);
        airplane3.setCharacteristics(
                new AirplaneCharacteristics(977, 9, 46, 10));
        airplane3.setFlights(Collections.emptyList());
        airplaneRepository.save(airplane3);
    }
}
