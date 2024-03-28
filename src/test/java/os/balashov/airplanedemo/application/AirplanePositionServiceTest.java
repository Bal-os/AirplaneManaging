package os.balashov.airplanedemo.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import os.balashov.airplanedemo.domain.entities.Airplane;
import os.balashov.airplanedemo.domain.entities.AirplaneCharacteristics;
import os.balashov.airplanedemo.domain.entities.TemporaryPoint;
import os.balashov.airplanedemo.domain.entities.WayPoint;
import os.balashov.airplanedemo.infrastructure.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {AppConfig.class})
public class AirplanePositionServiceTest {
    @Mock
    private PlaneCalculationImpl planeCalculationImpl;
    private AirplanePositionServiceImpl airplanePositionService;

    @BeforeEach
    public void setUp() {
        airplanePositionService = new AirplanePositionServiceImpl(planeCalculationImpl);
    }

    @Test
    public void testIsDestinationReached_WhenEmptyRoute() {
        Airplane airplane = new Airplane();

        assertFalse(airplanePositionService.isDestinationNotReached(airplane));
    }

    @Test
    public void testCreateNewDestination_CalculatesRouteAndUpdates() {
        List<WayPoint> wayPoints = new ArrayList<>();
        TemporaryPoint point = new TemporaryPoint();
        List<TemporaryPoint> expectedRoute = new ArrayList<>();
        expectedRoute.add(point);
        when(planeCalculationImpl.calculateRoute(any(), any())).thenReturn(expectedRoute);
        Airplane airplane = mock(Airplane.class);
        when(airplane.getId()).thenReturn(1L);
        when(airplane.getCharacteristics()).thenReturn(new AirplaneCharacteristics());

        airplanePositionService.createNewDestination(airplane, wayPoints);

        verify(planeCalculationImpl).calculateRoute(airplane.getCharacteristics(), wayPoints);
        verify(airplane).setPosition(any());
    }


    @Test
    public void testIsDestinationNotReached_WhenRouteHasPoints() {
        Airplane airplane = new Airplane();
        List<WayPoint> wayPoints = new ArrayList<>();
        TemporaryPoint point = new TemporaryPoint();
        List<TemporaryPoint> temporaryPoints = new ArrayList<>(List.of(point, point, point));
        when(planeCalculationImpl.calculateRoute(any(), any())).thenReturn(temporaryPoints);

        airplanePositionService.createNewDestination(airplane, wayPoints);
        boolean reached = airplanePositionService.isDestinationNotReached(airplane);

        assertTrue(reached);
    }

    @Test
    public void testUpdatePosition_CallsNextAndUpdatesAirplane() {
        Airplane airplane = new Airplane();
        List<WayPoint> wayPoints = new ArrayList<>();
        TemporaryPoint firstPoint = new TemporaryPoint();
        firstPoint.setHeight(5);
        TemporaryPoint secondPoint = new TemporaryPoint();
        secondPoint.setHeight(4);
        List<TemporaryPoint> temporaryPoints = new ArrayList<>(List.of(firstPoint, secondPoint));
        when(planeCalculationImpl.calculateRoute(any(), any())).thenReturn(temporaryPoints);
        airplanePositionService.createNewDestination(airplane, wayPoints);

        TemporaryPoint actualPoint = airplanePositionService.updatePosition(airplane);

        assertEquals(secondPoint, actualPoint);
    }
}
