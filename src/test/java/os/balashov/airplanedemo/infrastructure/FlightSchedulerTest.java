package os.balashov.airplanedemo.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import os.balashov.airplanedemo.aplication.interfaces.AirplaneService;
import os.balashov.airplanedemo.domain.entities.Airplane;
import os.balashov.airplanedemo.domain.services.FlightScheduler;
import os.balashov.airplanedemo.infrastructure.config.SimpleAppConfig;
import os.balashov.airplanedemo.infrastructure.schedulers.FlightSchedulerImpl;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = {SimpleAppConfig.class})
public class FlightSchedulerTest {
    @Mock
    private AirplaneService mockAirplaneService;
    @Value("${schedule.rate}")
    private String rateString;
    private FlightScheduler flightScheduler;

    @BeforeEach
    public void setUp() {
        flightScheduler = new FlightSchedulerImpl(mockAirplaneService);
    }

    @Test
    public void testUpdateAirplanesCallsServiceMethods() throws InterruptedException {
        List<Airplane> airplanes = Arrays.asList(new Airplane(), new Airplane());
        when(mockAirplaneService.getAllAirplanes()).thenReturn(airplanes);

        flightScheduler.updateAirplanes();

        Thread.sleep(2 * Long.parseLong(rateString));

        verify(mockAirplaneService).getAllAirplanes();
        verify(mockAirplaneService, times(2)).manageAirplaneFlight(any(Airplane.class));
    }

    @Test
    public void testUpdateAirplanesWithServiceBehavior() {
        List<Airplane> airplanes = Arrays.asList(new Airplane(), new Airplane());
        when(mockAirplaneService.getAllAirplanes()).thenReturn(airplanes);

        flightScheduler.updateAirplanes();

        verify(mockAirplaneService, times(2)).manageAirplaneFlight(any(Airplane.class));
    }

}
