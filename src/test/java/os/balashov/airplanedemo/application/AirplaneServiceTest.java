package os.balashov.airplanedemo.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import os.balashov.airplanedemo.aplication.AirplanePositionServiceImpl;
import os.balashov.airplanedemo.aplication.AirplaneServiceImpl;
import os.balashov.airplanedemo.aplication.interfaces.AirplanePositionService;
import os.balashov.airplanedemo.domain.entities.*;
import os.balashov.airplanedemo.domain.repositories.AirplaneRepository;
import os.balashov.airplanedemo.aplication.PlaneCalculationImpl;
import os.balashov.airplanedemo.domain.services.OperatorData;
import os.balashov.airplanedemo.infrastructure.config.AppConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {AppConfig.class})
public class AirplaneServiceTest {
    @Mock
    private AirplaneRepository mockAirplaneRepository;
    @Mock
    private OperatorData mockOperatorData;
    private AirplanePositionService airplanePositionService;
    private AirplaneServiceImpl airplaneService;


    @BeforeEach
    public void setUp() {
        airplanePositionService = new AirplanePositionServiceImpl(new PlaneCalculationImpl());
        airplaneService = new AirplaneServiceImpl(mockAirplaneRepository,
                airplanePositionService,
                mockOperatorData);
    }

    @Test
    public void testGetAllAirplanes() {
        List<Airplane> expectedAirplanes = Arrays.asList(new Airplane(), new Airplane());
        when(mockAirplaneRepository.findAll()).thenReturn(expectedAirplanes);

        List<Airplane> airplanes = airplaneService.getAllAirplanes();

        assertEquals(expectedAirplanes, airplanes);
        verify(mockAirplaneRepository).findAll();
    }

    @Test
    public void testManageAirplaneFlightInFlight() {
        Airplane airplane = new Airplane(1L,
                new AirplaneCharacteristics(100, 10, 1000, 45),
                new TemporaryPoint(0, 0, 0, 0, 0),
                new ArrayList<>());
        List<WayPoint> wayPoints = Arrays.asList(
                new WayPoint(1, 1, 1, 1),
                new WayPoint(2, 2, 2, 2));
        Flight flight = new Flight(1L, wayPoints, new ArrayList<>());
        airplane.getFlights().add(flight);

        when(mockOperatorData.getWayPoints(airplane)).thenReturn(wayPoints);

        airplaneService.manageAirplaneFlight(airplane);

        assertEquals(wayPoints.get(0).getHeight(), airplane.getPosition().getHeight());
        assertEquals(wayPoints.get(0).getLatitude(), airplane.getPosition().getLatitude());
        assertEquals(wayPoints.get(0).getLongitude(), airplane.getPosition().getLongitude());
    }

    @Test
    public void testManageAirplaneFlightNotInFlight() {
        Airplane airplane = new Airplane(1L,
                new AirplaneCharacteristics(100, 10, 1000, 45),
                new TemporaryPoint(0, 0, 0, 0, 0),
                new ArrayList<>());
        List<WayPoint> wayPoints = Arrays.asList(new WayPoint(1, 1, 1, 1), new WayPoint(2, 2, 2, 2));

        when(mockOperatorData.getWayPoints(airplane)).thenReturn(wayPoints);

        airplaneService.manageAirplaneFlight(airplane);

        assertEquals(1, airplane.getFlights().size());
    }

}
