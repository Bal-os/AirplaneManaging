package os.balashov.airplanedemo.application.interfaces;

import os.balashov.airplanedemo.domain.entities.Airplane;
import os.balashov.airplanedemo.domain.entities.TemporaryPoint;
import os.balashov.airplanedemo.domain.entities.WayPoint;

import java.util.List;

public interface AirplanePositionService {
    boolean isDestinationNotReached(Airplane airplane);
    TemporaryPoint updatePosition(Airplane airplane);
    void createNewDestination(Airplane airplane, List<WayPoint> wayPoints);
}
