package os.balashov.airplanedemo.infrastructure.api;

import org.springframework.stereotype.Component;
import os.balashov.airplanedemo.domain.entities.Airplane;
import os.balashov.airplanedemo.domain.entities.WayPoint;
import os.balashov.airplanedemo.domain.services.OperatorData;

import java.util.*;

@Component
public class OperatorDataImpl implements OperatorData {
    private final double LATITUDE_MIN = 40.0;
    private final double LATITUDE_MAX = 60.0;
    private final double LONGITUDE_MIN = 20.0;
    private final double LONGITUDE_MAX = 40.0;
    private final double HEIGHT_MIN = 500.0;
    private final double HEIGHT_MAX = 2000.0;
    private final double SPEED_MIN = 100.0;
    private final double SPEED_MAX = 300.0;

    public List<WayPoint> getWayPoints(Airplane airplane) {
        return getWayPoints();
    }

    private List<WayPoint> getWayPoints() {
        List<WayPoint> wayPoints = new ArrayList<>();

        // 4 точки по кутах квадрата
        wayPoints.add(new WayPoint(LATITUDE_MIN, LONGITUDE_MIN, HEIGHT_MIN, SPEED_MIN));
        wayPoints.add(new WayPoint(LATITUDE_MIN, LONGITUDE_MAX, HEIGHT_MIN, SPEED_MIN));
        wayPoints.add(new WayPoint(LATITUDE_MAX, LONGITUDE_MAX, HEIGHT_MIN, SPEED_MIN));
        wayPoints.add(new WayPoint(LATITUDE_MAX, LONGITUDE_MIN, HEIGHT_MIN, SPEED_MIN));

        // 4 точки по діагоналях квадрата
        wayPoints.add(new WayPoint(LATITUDE_MIN, (LONGITUDE_MIN + LONGITUDE_MAX) / 2, HEIGHT_MIN, SPEED_MIN));
        wayPoints.add(new WayPoint((LATITUDE_MIN + LATITUDE_MAX) / 2, LONGITUDE_MAX, HEIGHT_MIN, SPEED_MIN));
        wayPoints.add(new WayPoint((LATITUDE_MAX + LATITUDE_MIN) / 2, LONGITUDE_MIN, HEIGHT_MIN, SPEED_MIN));
        wayPoints.add(new WayPoint(LATITUDE_MAX, (LONGITUDE_MIN + LONGITUDE_MAX) / 2, HEIGHT_MIN, SPEED_MIN));

        // 8 точок по "спіралі"
        for (int i = 0; i < 8; i++) {
            double angle = Math.PI * 2 * i / 8;
            double radius = (LATITUDE_MAX - LATITUDE_MIN) / 4 * (i + 1);
            double lat = (LATITUDE_MIN + LATITUDE_MAX) / 2 + radius * Math.cos(angle);
            double lon = (LONGITUDE_MIN + LONGITUDE_MAX) / 2 + radius * Math.sin(angle);
            double height = HEIGHT_MIN + (HEIGHT_MAX - HEIGHT_MIN) * i / 8;
            double speed = SPEED_MIN + (SPEED_MAX - SPEED_MIN) * i / 8;
            wayPoints.add(new WayPoint(lat, lon, height, speed));
        }

        // Перемішування точок
        Collections.shuffle(wayPoints);
        return wayPoints;
    }
}
