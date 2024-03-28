package os.balashov.airplanedemo.application.utils;

import os.balashov.airplanedemo.domain.entities.AirplaneCharacteristics;
import os.balashov.airplanedemo.domain.entities.WayPoint;

public interface InterpolationCoefficientCalculator extends TimeCalculator {
    default int calculateInterpolationCoefficient(WayPoint point1,
                                                  WayPoint point2,
                                                  AirplaneCharacteristics characteristics,
                                                  int numberOfIntermediatePoints) {
        if (numberOfIntermediatePoints <= 0) return 0;

        double distance = calculateDistance(point1, point2);
        double speedTime = calculateTimeFromSpeed(distance, point1.getSpeed(), point2.getSpeed());

        double heightDifference = calculateHeightDifference(point1, point2);
        double heightDifferenceTime = calculateTimeFromHeightDifference(heightDifference,
                characteristics.getMaxChangeHeight());

        double speedChangeTime = calculateTimeFromSpeedChange(point1.getSpeed(),
                point2.getSpeed(), characteristics.getMaxChangeSpeed());

        double time = Math.max(heightDifferenceTime, Math.max(speedTime, speedChangeTime));
        double coefficient = time / numberOfIntermediatePoints;

        return (int) Math.ceil(coefficient);
    }

    private double calculateDistance(WayPoint point1, WayPoint point2) {
        double dx = point2.getLongitude() - point1.getLongitude();
        double dy = point2.getLatitude() - point1.getLatitude();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double calculateHeightDifference(WayPoint point1, WayPoint point2) {
        return Math.abs(point1.getHeight() - point2.getHeight());
    }

}
