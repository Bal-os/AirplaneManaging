package os.balashov.airplanedemo.aplication;

import lombok.NoArgsConstructor;
import os.balashov.airplanedemo.aplication.utils.InterpolationCoefficientCalculator;
import os.balashov.airplanedemo.aplication.utils.Interpolator;
import os.balashov.airplanedemo.aplication.utils.TemperaryPointAdapter;
import os.balashov.airplanedemo.aplication.utils.Vector;
import os.balashov.airplanedemo.domain.entities.AirplaneCharacteristics;
import os.balashov.airplanedemo.domain.entities.TemporaryPoint;
import os.balashov.airplanedemo.domain.entities.WayPoint;
import os.balashov.airplanedemo.domain.services.PlaneCalculation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * Implementation of the PlaneCalculation interface.
 * This class provides methods for calculating an airplane's route based on its characteristics and a list of waypoints.
 */
@NoArgsConstructor
public class PlaneCalculationImpl implements PlaneCalculation {

    /**
     * Calculates an airplane's route based on its characteristics and a list of waypoints.
     * @param characteristics The airplane's characteristics.
     * @param wayPoints The list of waypoints.
     * @return A list of TemporaryPoint representing the airplane's route.
     */
    @Override
    public List<TemporaryPoint> calculateRoute(AirplaneCharacteristics characteristics, List<WayPoint> wayPoints) {
        if(isCharacteristicsIncorrect(characteristics)) {
            return Collections.emptyList();
        }

        List<WayPoint> checkedWayPoints = new LinkedList<>();
        for (WayPoint wayPoint : wayPoints) {
            if (wayPoint.getSpeed() > characteristics.getMaxSpeed()) continue;
            if (wayPoint.getSpeed() < 0) continue;
            if (wayPoint.getHeight() < 0) continue;

            checkedWayPoints.add(wayPoint);
        }
        if(checkedWayPoints.size() < 2) {
            return Collections.emptyList();
        }

        return generateTemporaryPoints(characteristics, checkedWayPoints).stream()
                .map(TemperaryPointAdapter::getTemporaryPoint)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the airplane's characteristics are incorrect.
     * @param airplaneCharacteristics The airplane's characteristics.
     * @return true if the characteristics are incorrect, false otherwise.
     */
    private boolean isCharacteristicsIncorrect(AirplaneCharacteristics airplaneCharacteristics) {
        return airplaneCharacteristics.getMaxChangeAngle() <= 0 &&
                airplaneCharacteristics.getMaxChangeHeight() <= 0 &&
                airplaneCharacteristics.getMaxChangeSpeed() <= 0 &&
                airplaneCharacteristics.getMaxSpeed() <= 0;
    }

    /**
     * Generate TemporaryPoint wrapped in our own adapter.
     * @param characteristics The airplane's characteristics.
     * @param wayPoints The list of waypoints.
     * @return A list of TemporaryPointAdapter which wrap a TemporaryPoint.
     */
    private LinkedList<TemperaryPointAdapter> generateTemporaryPoints(AirplaneCharacteristics characteristics,
                                                                      List<WayPoint> wayPoints) {
        Vector vectorFromStartPoint = new Vector(wayPoints.get(1), wayPoints.get(0));
        LinkedList<TemperaryPointAdapter> outputPoints = new LinkedList<>();

        outputPoints.add(TemperaryPointAdapter.builder()
                .wayPoint(wayPoints.get(0))
                .angle(0)
                .build());
        for (int i = 1; i < wayPoints.size() - 1; i++) {
            WayPoint currentPoint = wayPoints.get(i);
            WayPoint nextPoint = wayPoints.get(i + 1);
            Vector vectorToNextPoint = new Vector(nextPoint, currentPoint);

            int numberOfIntermediatePoints = addIntermediatePoints(characteristics,
                    outputPoints, currentPoint, vectorFromStartPoint, vectorToNextPoint);

            WayPoint prevPoint = wayPoints.get(i - 1);
            addInterpolationPoints(outputPoints,
                    prevPoint, currentPoint, characteristics, numberOfIntermediatePoints);

            double course = vectorFromStartPoint.routeSensitiveAngleInDegrees(vectorToNextPoint);
            outputPoints.add(TemperaryPointAdapter.builder()
                    .wayPoint(currentPoint)
                    .angle(course)
                    .build());
        }
        outputPoints.add(TemperaryPointAdapter.builder()
                .wayPoint(wayPoints.get(wayPoints.size() - 1))
                .angle(0)
                .build());
        return outputPoints;
    }

    /**
     * Add intermediate points which are depends on angles.
     * Intermediate points helps to carry out the maxChangeAngle.
     * @return A number of intermediate points added to the outputPoints list.
     */
    private int addIntermediatePoints(AirplaneCharacteristics characteristics,
                                      LinkedList<TemperaryPointAdapter> outputPoints,
                                      WayPoint currentPoint,
                                      Vector vectorFromStartPoint,
                                      Vector vectorToNextPoint) {
        if(characteristics.getMaxChangeAngle() <= 0) {
            return 0;
        }

        double maxChangeAngle = Math.toRadians(characteristics.getMaxChangeAngle());
        int pointsAmountBeforeAdd = outputPoints.size();
        while (addIntermediatePoint(outputPoints,
                currentPoint, vectorFromStartPoint, vectorToNextPoint, maxChangeAngle));
        return outputPoints.size() - pointsAmountBeforeAdd;
    }

    /**
     * Add one intermediate point.
     * @return False if it is not possible to add more intermediate points.
     */
    private boolean addIntermediatePoint(LinkedList<TemperaryPointAdapter> outputPoints,
                                         WayPoint currentPoint,
                                         Vector vectorFromStartPoint,
                                         Vector vectorToNextPoint,
                                         double maxChangeAngle) {
        TemperaryPointAdapter prevPoint = outputPoints.getLast();
        Vector currentVector = new Vector(currentPoint, prevPoint);
        double angleChange = currentVector.angleBetween(vectorToNextPoint);
        if (angleChange <= maxChangeAngle) {
            return false;
        }

        Vector intermediateVector = vectorToNextPoint
                .multiply(1 - angleChange / maxChangeAngle);
        TemperaryPointAdapter intermediatePoint = TemperaryPointAdapter.builder()
                .wayPoint(currentPoint)
                .latitude(prevPoint.getLatitude() + intermediateVector.getY())
                .longitude(prevPoint.getLongitude() + intermediateVector.getX())
                .angle(vectorFromStartPoint.routeSensitiveAngleInDegrees(intermediateVector))
                .build();

        if (prevPoint.equals(intermediatePoint)) {
            return false;
        }
        outputPoints.add(intermediatePoint);
        return true;
    }

    /**
     * Add interpolation points which are depends on several parameters.
     * Interpolation points helps to carry out the maxSpeed, maxChangeSpeed, maxChangeHeight limitations.
     */
    private void addInterpolationPoints(List<TemperaryPointAdapter> outputPoints,
                                        WayPoint point1,
                                        WayPoint point2,
                                        AirplaneCharacteristics characteristics,
                                        int numberOfIntermediatePoints) {
        int coefficient = getInterpolationCoefficientCalculator().calculateInterpolationCoefficient(point1,
                point2, characteristics, numberOfIntermediatePoints);
        int startIndex = outputPoints.size() - numberOfIntermediatePoints - 1;

        for(ListIterator<TemperaryPointAdapter> iterator = outputPoints.listIterator(startIndex);
                iterator.hasNext();) {

            double progress = (double) iterator.nextIndex() / numberOfIntermediatePoints;
            Interpolator interpolator = getInterpolator(progress);
            iterator.next().updateSpeedWithInterpolation(point1, point2, interpolator)
                    .updateHeightWithInterpolation(point1, point2, interpolator);

            addInterpolatePointsBeforeCurrent(point1, point2, coefficient, iterator);
        }
    }

    /**
     * Add interpolation points before the current.
     * Interpolation points amount depends on special coefficient calculated for every segment between wayPoints.
     * @param point1 First wayPoint which the interpolation is depends on.
     * @param point2 Second wayPoint which the interpolation is depends on.
     * @param coefficient Number of Interpolation points needed to insert between two intermediate points.
     * @param iterator List iterator mark current point of list.
     */
    private void addInterpolatePointsBeforeCurrent(WayPoint point1,
                                                   WayPoint point2,
                                                   int coefficient,
                                                   ListIterator<TemperaryPointAdapter> iterator) {
        for (int k = 1; k < coefficient; k++) {
            Interpolator interpolator = getInterpolator( (double) k / coefficient);
            TemperaryPointAdapter prevPoint = iterator.previous();
            TemperaryPointAdapter currentPoint = iterator.next();
            TemperaryPointAdapter midPoint = prevPoint.getBuilder()
                    .interpolateHeight(point1, point2, interpolator)
                    .interpolateSpeed(point1, point2, interpolator)
                    .interpolateLatitude(prevPoint, currentPoint, interpolator)
                    .interpolateLongitude(prevPoint, currentPoint, interpolator)
                    .build();

            iterator.previous();
            iterator.add(midPoint);
            iterator.next();
            iterator.next();
        }
    }

    private InterpolationCoefficientCalculator getInterpolationCoefficientCalculator() {
        return new InterpolationCoefficientCalculator() {
            @Override
            public int calculateInterpolationCoefficient(WayPoint point1,
                                                         WayPoint point2,
                                                         AirplaneCharacteristics characteristics,
                                                         int numberOfIntermediatePoints) {
                return InterpolationCoefficientCalculator.super.calculateInterpolationCoefficient(point1,
                        point2, characteristics, numberOfIntermediatePoints);
            }
        };
    }

    private Interpolator getInterpolator(double progress) {
        return new Interpolator() {
            @Override
            public double interpolate(double start, double end) {
                return Interpolator.super.interpolate(start, end, progress);
            }
        };
    }
}
