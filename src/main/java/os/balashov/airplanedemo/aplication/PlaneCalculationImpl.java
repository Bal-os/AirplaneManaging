package os.balashov.airplanedemo.aplication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import os.balashov.airplanedemo.domain.entities.AirplaneCharacteristics;
import os.balashov.airplanedemo.domain.entities.TemporaryPoint;
import os.balashov.airplanedemo.domain.entities.WayPoint;
import os.balashov.airplanedemo.domain.services.PlaneCalculation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
public class PlaneCalculationImpl implements PlaneCalculation {
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
        return getTemporaryPoints(characteristics, checkedWayPoints);
    }

    private LinkedList<TemporaryPoint> getTemporaryPoints(AirplaneCharacteristics characteristics, List<WayPoint> wayPoints) {
        LinkedList<TemporaryPoint> outputPoints = new LinkedList<>();
        TemporaryPoint firstPoint = buildTemporaryPointFromWayPoint(wayPoints.get(0), 0);
        outputPoints.add(firstPoint);
        Vector vectorFromStartPoint = new Vector(wayPoints.get(1), wayPoints.get(0));
        for (int i = 1; i < wayPoints.size() - 1; i++) {
            WayPoint currentPoint = wayPoints.get(i);
            WayPoint nextPoint = wayPoints.get(i + 1);
            Vector vectorToNextPoint = new Vector(nextPoint, currentPoint);

            int numberOfIntermediatePoints = getNumberOfIntermediatePoints(characteristics, outputPoints, currentPoint, vectorFromStartPoint, vectorToNextPoint);

            WayPoint prevPoint = wayPoints.get(i - 1);
            interpolationPointsAdd(outputPoints, prevPoint, currentPoint, characteristics, numberOfIntermediatePoints);

            double course = vectorFromStartPoint.routeSensitiveAngleInDegrees(vectorToNextPoint);
            outputPoints.add(buildTemporaryPointFromWayPoint(currentPoint, course));
        }
        TemporaryPoint lastPoint = buildTemporaryPointFromWayPoint(wayPoints.get(wayPoints.size() - 1), 0);
        outputPoints.add(lastPoint);
        return outputPoints;
    }

    private boolean isCharacteristicsIncorrect(AirplaneCharacteristics airplaneCharacteristics) {
        return airplaneCharacteristics.getMaxChangeAngle() <= 0 &&
                airplaneCharacteristics.getMaxChangeHeight() <= 0 &&
                airplaneCharacteristics.getMaxChangeSpeed() <= 0 &&
                airplaneCharacteristics.getMaxSpeed() <= 0;
    }

    private int getNumberOfIntermediatePoints(AirplaneCharacteristics characteristics,
                                              LinkedList<TemporaryPoint> outputPoints,
                                              WayPoint currentPoint,
                                              Vector vectorFromStartPoint,
                                              Vector vectorToNextPoint) {
        if(characteristics.getMaxChangeAngle() <= 0) {
            return 0;
        }

        double maxChangeAngle = Math.toRadians(characteristics.getMaxChangeAngle());

        return intermediatePointsAdd(outputPoints,
                currentPoint, vectorFromStartPoint, vectorToNextPoint, maxChangeAngle);
    }


    private int intermediatePointsAdd(LinkedList<TemporaryPoint> outputPoints,
                                      WayPoint currentPoint,
                                      Vector vectorFromStartPoint,
                                      Vector vectorToNextPoint,
                                      double maxChangeAngle) {
        int pointsAmountBeforeAdd = outputPoints.size();
        while (true) {
            TemporaryPoint prevPoint = outputPoints.getLast();
            Vector currentVector = new Vector(currentPoint, prevPoint);
            double angleChange = currentVector.angleBetween(vectorToNextPoint);
            if (angleChange <= maxChangeAngle) {
                break;
            }

            Vector intermediateVector = vectorToNextPoint
                    .multiply(1 - angleChange / maxChangeAngle);
            TemporaryPoint intermediatePoint = temporaryPointFromWayPointFromWayPointBuilder(currentPoint)
                    .latitude(prevPoint.getLatitude() + intermediateVector.getY())
                    .longitude(prevPoint.getLongitude() + intermediateVector.getX())
                    .angle(vectorFromStartPoint.routeSensitiveAngleInDegrees(intermediateVector))
                    .build();
            outputPoints.add(intermediatePoint);
        }
        return outputPoints.size() - pointsAmountBeforeAdd;
    }

    private void interpolationPointsAdd(List<TemporaryPoint> outputPoints,
                                        WayPoint point1,
                                        WayPoint point2,
                                        AirplaneCharacteristics characteristics,
                                        int numberOfIntermediatePointsAdded) {
        int linearInterpolationAmount = calculateLinearInterpolationAmount(point1, point2, characteristics);
        int coefficient = (int) Math.floor(((double) linearInterpolationAmount) / numberOfIntermediatePointsAdded);

        int startIndex = outputPoints.size() - numberOfIntermediatePointsAdded;
        for (int j = startIndex; j < outputPoints.size(); j++) {
            double progress = (double) j / numberOfIntermediatePointsAdded;
            TemporaryPoint currentPoint = outputPoints.get(j);
            updateIntermediatePointWithInterpolation(currentPoint, point1, point2, progress);

            for (int k = 0; k < coefficient; k++) {
                TemporaryPoint prevPoint = outputPoints.get(j - 1);
                outputPoints.add(j, buildInterpolatePoint(prevPoint, currentPoint, point1, point2, progress));
            }
        }
    }

    private void updateIntermediatePointWithInterpolation(TemporaryPoint changePoint,
                                                          WayPoint point1,
                                                          WayPoint point2,
                                                          double progress) {
        changePoint.setSpeed(interpolate(point1.getLatitude(), point2.getLatitude(), progress));
        changePoint.setHeight(interpolate(point1.getHeight(), point1.getHeight(), progress));
    }

    private TemporaryPoint buildInterpolatePoint(TemporaryPoint prevPoint,
                                                 TemporaryPoint currentPoint,
                                                 WayPoint point1,
                                                 WayPoint point2,
                                                 double progress) {
        return TemporaryPoint
                .builder()
                .latitude(interpolate(prevPoint.getLatitude(), currentPoint.getLatitude(), progress))
                .longitude(interpolate(prevPoint.getLongitude(), currentPoint.getLongitude(), progress))
                .height(interpolate(point1.getHeight(), point2.getHeight(), progress))
                .speed(interpolate(point1.getSpeed(), point2.getSpeed(), progress))
                .angle(currentPoint.getAngle())
                .build();
    }

    private TemporaryPoint buildTemporaryPointFromWayPoint(WayPoint currentPoint, double course) {
        return temporaryPointFromWayPointFromWayPointBuilder(currentPoint)
                .angle(course)
                .build();
    }

    private TemporaryPoint.TemporaryPointBuilder temporaryPointFromWayPointFromWayPointBuilder(WayPoint currentPoint) {
        return TemporaryPoint.builder()
                .latitude(currentPoint.getLatitude())
                .longitude(currentPoint.getLongitude())
                .speed(currentPoint.getSpeed())
                .height(currentPoint.getHeight());
    }
    private int calculateLinearInterpolationAmount(WayPoint point1,
                                                   WayPoint point2,
                                                   AirplaneCharacteristics characteristics) {
        double distance = calculateDistance(point1, point2);
        double speedTime = calculateTimeFromSpeed(distance, point1.getSpeed(), point2.getSpeed());

        double heightDifference = calculateHeightDifference(point1, point2);
        double heightDifferenceTime = calculateTimeFromHeightDifference(heightDifference,
                characteristics.getMaxChangeHeight());

        double speedChangeTime = calculateSpeedChangeTime(point1.getSpeed(),
                point2.getSpeed(),
                characteristics.getMaxChangeSpeed());

        double time = Math.max(heightDifferenceTime, Math.max(speedTime, speedChangeTime));
        return (int) Math.ceil(time);
    }

    private double calculateDistance(WayPoint point1, WayPoint point2) {
        double dx = point2.getLongitude() - point1.getLongitude();
        double dy = point2.getLatitude() - point1.getLatitude();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double calculateTimeFromSpeed(double distance, double startSpeed, double endSpeed) {
        double averageSpeed = (startSpeed + endSpeed) / 2.0;
        return distance / averageSpeed;
    }

    private double calculateHeightDifference(WayPoint point1, WayPoint point2) {
        return Math.abs(point1.getHeight() - point2.getHeight());
    }

    private double calculateTimeFromHeightDifference(double heightDifference, double heightChange) {
        if (heightChange <= 0) return 0;
        return heightDifference / heightChange;
    }

    private double calculateSpeedChangeTime(double startSpeed, double endSpeed, double speedChangeLimit) {
        if (speedChangeLimit <= 0) return 0;
        double speedDiff = Math.abs(startSpeed - endSpeed);
        return speedDiff / speedChangeLimit;
    }

    private double interpolate(double start, double end, double progress) {
        return start + progress * (end - start);
    }

    @Getter
    @AllArgsConstructor
    private static class Vector {
        private double x;
        private double y;

        public Vector(WayPoint a, WayPoint b) {
            this.x = a.getLongitude() - b.getLongitude();
            this.y = a.getLatitude() - b.getLatitude();
        }

        public Vector(WayPoint a, TemporaryPoint b) {
            this.x = a.getLongitude() - b.getLongitude();
            this.y = a.getLatitude() - b.getLatitude();
        }

        public double dotProduct(Vector other) {
            return this.x * other.x + this.y * other.y;
        }

        public double norm() {
            return Math.sqrt(this.dotProduct(this));
        }

        public double angleBetween(Vector other) {
            return Math.acos(this.dotProduct(other) / (this.norm() * other.norm()));
        }

        public double determinant(Vector other) {
            return this.getX() * other.getY() - this.getY() * other.getX();
        }

        public double routeSensitiveAngleBetween(Vector other) {
            return Math.atan2(this.determinant(other), this.dotProduct(other));
        }

        public double routeSensitiveAngleInDegrees(Vector other) {
            return Math.toDegrees(routeSensitiveAngleBetween(other));
        }

        public Vector multiply(double scalar) {
            return new Vector(this.x * scalar, this.y * scalar);
        }
    }
}
