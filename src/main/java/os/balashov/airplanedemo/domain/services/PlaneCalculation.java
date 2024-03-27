package os.balashov.airplanedemo.domain.services;

import os.balashov.airplanedemo.domain.entities.AirplaneCharacteristics;
import os.balashov.airplanedemo.domain.entities.WayPoint;
import os.balashov.airplanedemo.domain.entities.TemporaryPoint;

import java.util.List;

public interface PlaneCalculation {
    List<TemporaryPoint> calculateRoute(AirplaneCharacteristics characteristics, List<WayPoint> wayPoints);
}
