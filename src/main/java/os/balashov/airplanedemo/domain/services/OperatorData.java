package os.balashov.airplanedemo.domain.services;

import os.balashov.airplanedemo.domain.entities.Airplane;
import os.balashov.airplanedemo.domain.entities.WayPoint;

import java.util.List;

public interface OperatorData {
    List<WayPoint> getWayPoints(Airplane airplane);
}
