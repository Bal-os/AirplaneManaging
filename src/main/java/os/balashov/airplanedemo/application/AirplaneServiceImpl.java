package os.balashov.airplanedemo.application;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import os.balashov.airplanedemo.application.interfaces.AirplanePositionService;
import os.balashov.airplanedemo.application.interfaces.AirplaneService;
import os.balashov.airplanedemo.domain.entities.Airplane;
import os.balashov.airplanedemo.domain.entities.Flight;
import os.balashov.airplanedemo.domain.entities.TemporaryPoint;
import os.balashov.airplanedemo.domain.entities.WayPoint;
import os.balashov.airplanedemo.domain.repositories.AirplaneRepository;
import os.balashov.airplanedemo.domain.services.OperatorData;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
@Log4j2
@Service
public class AirplaneServiceImpl implements AirplaneService {
    private final AirplaneRepository airplaneRepository;
    private final AirplanePositionService airplanePositionService;
    private final OperatorData operatorData;

    @Autowired
    public AirplaneServiceImpl(AirplaneRepository airplaneRepository,
                               AirplanePositionService airplanePositionService,
                               OperatorData operatorData) {
        this.airplaneRepository = airplaneRepository;
        this.airplanePositionService = airplanePositionService;
        this.operatorData = operatorData;
    }

    @Override
    @Transactional
    public void manageAirplaneFlight(Airplane airplane) {
        if(isAirplaneInFlight(airplane)) {
            log.debug("Updating flight for airplane: {}", airplane.getId());
            updateFlight(airplane);
        } else {
            log.debug("Creating flight for airplane: {}", airplane.getId());
            printFlightStat(airplane);
            createFlight(airplane);
        }
    }

    @Override
    public List<Airplane> getAllAirplanes() {
        log.debug("Getting all airplanes");
        return airplaneRepository.findAll();
    }

    private void createFlight(Airplane airplane) {
        Flight flight = new Flight();
        List<WayPoint> wayPoints = operatorData.getWayPoints(airplane);
        flight.setWayPoints(wayPoints);
        airplane.getFlights().add(flight);
        airplanePositionService.createNewDestination(airplane, wayPoints);
        airplaneRepository.save(airplane);
    }

    private void updateFlight(Airplane airplane) {
        if (airplanePositionService.isDestinationNotReached(airplane)) {
            TemporaryPoint newPosition = airplanePositionService.updatePosition(airplane);
            airplane.setPosition(newPosition);
            addNewPointToCurrentFlight(airplane, newPosition);
            airplaneRepository.save(airplane);
        }
    }

    private boolean isAirplaneInFlight(Airplane airplane) {
//        log.info("Checking if airplane is in flight: {}", airplane.getId());
        List<Flight> points = airplane.getFlights();
        return !points.isEmpty() && airplanePositionService.isDestinationNotReached(airplane);
    }

    private void addNewPointToCurrentFlight(Airplane airplane, TemporaryPoint newPosition) {
        Flight currentFlight = airplane.getFlights().get(airplane.getFlights().size() - 1);
        List<TemporaryPoint> passedPoints = currentFlight.getPassedPoints();
        if (Objects.isNull(passedPoints)) {
            currentFlight.setPassedPoints(new LinkedList<>());
        }
        currentFlight.getPassedPoints().add(newPosition);
    }

    private void printFlightStat(Airplane airplane) {
        List<Flight> previousFlights = airplane.getFlights();
        int sum = previousFlights.stream()
                .mapToInt(this::getFlightTime)
                .sum();
        log.info("Airplane {} has completed {} previous flights with total duration: {}",
                airplane.getId(),
                previousFlights.size(),
                sum);
    }

    private int getFlightTime(Flight flight) {
        if(flight.getPassedPoints() == null) return 0;
        return flight.getPassedPoints().size() + 1;
    }
}
