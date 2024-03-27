package os.balashov.airplanedemo.domain.repositories;

import os.balashov.airplanedemo.domain.entities.Airplane;

import java.util.List;

public interface AirplaneRepository {
    List<Airplane> findAll();
    void save(Airplane airplane);
}
