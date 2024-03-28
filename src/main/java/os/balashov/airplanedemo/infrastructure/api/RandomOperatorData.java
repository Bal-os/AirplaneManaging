package os.balashov.airplanedemo.infrastructure.api;

import org.springframework.stereotype.Component;
import os.balashov.airplanedemo.domain.entities.Airplane;
import os.balashov.airplanedemo.domain.entities.WayPoint;
import os.balashov.airplanedemo.domain.services.OperatorData;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

@Component
public class RandomOperatorData implements OperatorData {
    private static final int MAX_POINTS = 100;
    private static final int MIN_POINTS = 10;
    private final double LATITUDE_MIN = - 100000.0;
    private final double LATITUDE_MAX = 100000.0;
    private final double LONGITUDE_MIN = - 100000.0;
    private final double LONGITUDE_MAX = 100000.0;
    private final double HEIGHT_MIN = 100.0;
    private final double HEIGHT_MAX = 2000.0;
    private final double SPEED_MIN = 100.0;
    private final double SPEED_MAX = 1000.0;

    private final Random random = new Random();

    @Override
    public List<WayPoint> getWayPoints(Airplane airplane) {
        List<WayPoint> wayPoints = new ArrayList<>();
        for (int i = 0; i < random.nextDouble() * MAX_POINTS + MIN_POINTS; i++) {
            double latitude = random.nextDouble() * LATITUDE_MAX + random.nextDouble() * LATITUDE_MIN;
            double longitude = random.nextDouble() * LONGITUDE_MAX + random.nextDouble() * LONGITUDE_MIN;
            double height = (random.nextDouble() * HEIGHT_MAX + HEIGHT_MIN) % HEIGHT_MAX;
            double speed = (random.nextDouble() * SPEED_MAX + SPEED_MIN) % SPEED_MAX;
            WayPoint point = new WayPoint(latitude, longitude, height, speed);
            wayPoints.add(point);
        }
        return wayPoints;
    }
}
