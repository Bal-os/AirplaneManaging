package os.balashov.airplanedemo.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import os.balashov.airplanedemo.domain.entities.AirplaneCharacteristics;
import os.balashov.airplanedemo.domain.entities.TemporaryPoint;
import os.balashov.airplanedemo.domain.entities.WayPoint;
import os.balashov.airplanedemo.domain.services.PlaneCalculation;
import os.balashov.airplanedemo.infrastructure.config.SimpleAppConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {SimpleAppConfig.class})
public class PlaneCalculationTest {
    @Autowired
    private PlaneCalculation planeCalculation;

    @Test
    public void testCalculateRoute() {
        AirplaneCharacteristics characteristics = new AirplaneCharacteristics(1000, 10, 100, 10);
        WayPoint point1 = new WayPoint(0, 0, 1000, 500);
        WayPoint point2 = new WayPoint(1000, 1000, 2000, 600);
        List<WayPoint> wayPoints = Arrays.asList(point1, point2);

        List<TemporaryPoint> result = planeCalculation.calculateRoute(characteristics, wayPoints);

        assertNotNull(result, "Result should not be null");
        Assertions.assertFalse(result.isEmpty(), "Result should contain at least one point");

        TemporaryPoint firstPoint = result.get(0);
        assertEquals(point1.getLatitude(), firstPoint.getLatitude(), "First point latitude should match the first waypoint");
        assertEquals(point1.getLongitude(), firstPoint.getLongitude(), "First point longitude should match the first waypoint");

        TemporaryPoint lastPoint = result.get(result.size() - 1);
        assertEquals(point2.getLatitude(), lastPoint.getLatitude(), "Last point latitude should match the last waypoint");
        assertEquals(point2.getLongitude(), lastPoint.getLongitude(), "Last point longitude should match the last waypoint");

        for (int i = 1; i < result.size() - 1; i++) {
            TemporaryPoint point = result.get(i);
            assertTrue(point.getLatitude() >= point1.getLatitude() && point.getLatitude() <= point2.getLatitude(), "Intermediate point latitude should be between the waypoints");
            assertTrue(point.getLongitude() >= point1.getLongitude() && point.getLongitude() <= point2.getLongitude(), "Intermediate point longitude should be between the waypoints");
        }
    }

    @Test
    public void testCalculateRouteMultiplePoints() {
        AirplaneCharacteristics characteristics = new AirplaneCharacteristics(1000, 10, 100, 10);
        WayPoint point1 = new WayPoint(0, 0, 1000, 500);
        WayPoint point2 = new WayPoint(1000, 1000, 2000, 600);
        WayPoint point3 = new WayPoint(2000, 2000, 3000, 700);
        List<WayPoint> wayPoints = Arrays.asList(point1, point2, point3);


        List<TemporaryPoint> result = planeCalculation.calculateRoute(characteristics, wayPoints);

        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Result should contain at least one point");

        TemporaryPoint firstPoint = result.get(0);
        assertEquals(point1.getLatitude(), firstPoint.getLatitude(), "First point latitude should match the first waypoint");
        assertEquals(point1.getLongitude(), firstPoint.getLongitude(), "First point longitude should match the first waypoint");

        TemporaryPoint lastPoint = result.get(result.size() - 1);
        assertEquals(point3.getLatitude(), lastPoint.getLatitude(), "Last point latitude should match the last waypoint");
        assertEquals(point3.getLongitude(), lastPoint.getLongitude(), "Last point longitude should match the last waypoint");
    }

    @Test
    public void testCalculateRouteTwentyPoints() {
        AirplaneCharacteristics characteristics = new AirplaneCharacteristics(1000, 10, 100, 10);
        List<WayPoint> wayPoints = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            wayPoints.add(new WayPoint(i * 1000, i * 1000, 1000 + i * 100, 500 + i * 10));
        }

        List<TemporaryPoint> result = planeCalculation.calculateRoute(characteristics, wayPoints);

        assertNotNull(result, "Result should not be null");
        assertEquals(20, result.size(), "Result should contain 20 points");
    }

    @Test
    public void testCalculateRouteIncompleteCharacteristics() {
        AirplaneCharacteristics characteristics = new AirplaneCharacteristics(0, 0, 0, 0);
        WayPoint point1 = new WayPoint(0, 0, 1000, 500);
        WayPoint point2 = new WayPoint(1000, 1000, 2000, 600);
        List<WayPoint> wayPoints = Arrays.asList(point1, point2);

        List<TemporaryPoint> result = planeCalculation.calculateRoute(characteristics, wayPoints);

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be empty for incomplete characteristics");
    }

    @Test
    public void testCalculateRouteInvalidPoints() {
        AirplaneCharacteristics characteristics = new AirplaneCharacteristics(1000, 10, 100, 10);
        WayPoint point1 = new WayPoint(-1, -1, -1, -1);
        WayPoint point2 = new WayPoint(1000, 1000, 2000, 600);
        List<WayPoint> wayPoints = Arrays.asList(point1, point2);

        List<TemporaryPoint> result = planeCalculation.calculateRoute(characteristics, wayPoints);

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be lower for invalid points");
    }

    @Test
    public void testCalculateRouteManyPoints() {
        AirplaneCharacteristics characteristics = new AirplaneCharacteristics(1000, 10, 100, 10);
        List<WayPoint> wayPoints = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            wayPoints.add(new WayPoint(i * 1000, i * 1000, 1000 + i * 100, 500 + i * 5));
        }

        List<TemporaryPoint> result = planeCalculation.calculateRoute(characteristics, wayPoints);

        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Result should contain at least one point");
        assertTrue(result.size() >= wayPoints.size(), "Result should contain more TemporaryPoints than WayPoints");
    }

    @Test
    public void testCalculateRouteComplexMultipleResultPoints() {
        AirplaneCharacteristics characteristics = new AirplaneCharacteristics(1000, 10, 100, 50);
        WayPoint point1 = new WayPoint(0, 0, 1000, 500);
        WayPoint point2 = new WayPoint(0, 10000, 2000, 600);
        WayPoint point3 = new WayPoint(4330, 7500, 3000, 700);
        List<WayPoint> wayPoints = Arrays.asList(point1, point2, point3);


        List<TemporaryPoint> result = planeCalculation.calculateRoute(characteristics, wayPoints);

        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Result should contain at least one point");

        TemporaryPoint firstPoint = result.get(0);
        assertEquals(point1.getLatitude(), firstPoint.getLatitude(), "First point latitude should match the first waypoint");
        assertEquals(point1.getLongitude(), firstPoint.getLongitude(), "First point longitude should match the first waypoint");

        TemporaryPoint lastPoint = result.get(result.size() - 1);
        assertEquals(point3.getLatitude(), lastPoint.getLatitude(), "Last point latitude should match the last waypoint");
        assertEquals(point3.getLongitude(), lastPoint.getLongitude(), "Last point longitude should match the last waypoint");

        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Result should contain at least one point");
        assertTrue(result.size() > wayPoints.size(), "Result should contain more TemporaryPoints than WayPoints");
    }
}
