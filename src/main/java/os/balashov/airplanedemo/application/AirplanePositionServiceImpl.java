package os.balashov.airplanedemo.application;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import os.balashov.airplanedemo.application.interfaces.AirplanePositionService;
import os.balashov.airplanedemo.domain.entities.Airplane;
import os.balashov.airplanedemo.domain.entities.TemporaryPoint;
import os.balashov.airplanedemo.domain.entities.WayPoint;
import os.balashov.airplanedemo.domain.services.PlaneCalculation;

import java.util.*;

@Service
@Log4j2
@AllArgsConstructor
public class AirplanePositionServiceImpl implements AirplanePositionService {
    private final Map<Long, Iterator<TemporaryPoint>> airplaneRoutes = new HashMap<>();
    private final PlaneCalculation planeCalculation;

    @Override
    public boolean isDestinationNotReached(Airplane airplane) {
        log.debug("Check if destination is reached for airplane: {}", airplane.getId());
        return getPosition(airplane).hasNext();
    }
    @Override
    public TemporaryPoint updatePosition(Airplane airplane) {
        log.debug("Update position for airplane: {}", airplane.getId());
        TemporaryPoint temporaryPoint = getPosition(airplane).next();
        airplane.setPosition(temporaryPoint);
        return temporaryPoint;
    }

    @Override
    public void createNewDestination(Airplane airplane, List<WayPoint> wayPoints) {
        log.info("Creating destination and route for airplane: {}", airplane.getId());
        List<TemporaryPoint> route = planeCalculation.calculateRoute(airplane.getCharacteristics(), wayPoints);

        if (route != null && !route.isEmpty()) {
            log.debug("Updating position for airplane: {}", airplane.getId());
            Iterator<TemporaryPoint> iterator = route.iterator();
            airplane.setPosition(iterator.next());
            airplaneRoutes.put(airplane.getId(), iterator);
        }
    }

    private Iterator<TemporaryPoint> getPosition(Airplane airplane) {
        return airplaneRoutes.getOrDefault(airplane.getId(), Collections.emptyIterator());
    }
}
