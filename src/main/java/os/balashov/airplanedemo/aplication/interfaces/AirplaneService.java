package os.balashov.airplanedemo.aplication.interfaces;

import os.balashov.airplanedemo.domain.entities.Airplane;

import java.util.List;

public interface AirplaneService {
    List<Airplane> getAllAirplanes();
    void manageAirplaneFlight(Airplane airplane);
}
