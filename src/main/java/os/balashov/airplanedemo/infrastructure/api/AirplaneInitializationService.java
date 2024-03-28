package os.balashov.airplanedemo.infrastructure.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import os.balashov.airplanedemo.domain.repositories.AirplaneRepository;

import javax.annotation.PostConstruct;

@Service
public class AirplaneInitializationService {
    @Autowired
    private AirplaneRepository airplaneRepository;

    @PostConstruct
    public void init() {

    }
}
