package os.balashov.airplanedemo.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import os.balashov.airplanedemo.aplication.PlaneCalculationImpl;
import os.balashov.airplanedemo.domain.entities.AirplaneCharacteristics;
import os.balashov.airplanedemo.domain.entities.TemporaryPoint;
import os.balashov.airplanedemo.domain.entities.WayPoint;
import os.balashov.airplanedemo.domain.services.PlaneCalculation;
import os.balashov.airplanedemo.infrastructure.config.SimpleAppConfig;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = {SimpleAppConfig.class})
public class PlaneCalculationTest {
    @Autowired
    private PlaneCalculation planeCalculation;

    @Test
    public void testCalculateRoute() {
        // Підготовка даних для тесту
        AirplaneCharacteristics characteristics = new AirplaneCharacteristics(1000, 10, 100, 10); // Максимальна швидкість 1000 м/с, максимальне прискорення 10 м/с^2, швидкість зміни висоти 100 м/с, швидкість зміни курсу 10 град./с
        WayPoint point1 = new WayPoint(0, 0, 1000, 500); // Широта 0, довгота 0, висота проліту 1000 м, швидкість проліту 500 м/с
        WayPoint point2 = new WayPoint(1000, 1000, 2000, 600); // Широта 1000, довгота 1000, висота проліту 2000 м, швидкість проліту 600 м/с
        List<WayPoint> wayPoints = Arrays.asList(point1, point2);

        // Створення об'єкта для тестування
        PlaneCalculationImpl calculation = new PlaneCalculationImpl();

        // Виконання методу, який потрібно протестувати
        List<TemporaryPoint> result = calculation.calculateRoute(characteristics, wayPoints);

        // Перевірка результату
        Assertions.assertNotNull(result, "Result should not be null");
        Assertions.assertFalse(result.isEmpty(), "Result should contain at least one point");

        // Перевірка першої точки
        TemporaryPoint firstPoint = result.get(0);
        Assertions.assertEquals(point1.getLatitude(), firstPoint.getLatitude(), "First point latitude should match the first waypoint");
        Assertions.assertEquals(point1.getLongitude(), firstPoint.getLongitude(), "First point longitude should match the first waypoint");
        // Add more assertions as needed

        // Перевірка останньої точки
        TemporaryPoint lastPoint = result.get(result.size() - 1);
        Assertions.assertEquals(point2.getLatitude(), lastPoint.getLatitude(), "Last point latitude should match the last waypoint");
        Assertions.assertEquals(point2.getLongitude(), lastPoint.getLongitude(), "Last point longitude should match the last waypoint");
        // Add more assertions as needed
    }
}
