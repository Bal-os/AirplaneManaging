package os.balashov.airplanedemo.aplication.utils;

import lombok.Getter;
import os.balashov.airplanedemo.domain.entities.TemporaryPoint;
import os.balashov.airplanedemo.domain.entities.WayPoint;

@Getter
public class TemperaryPointAdapter extends WayPoint {
    private double angle;
    private TemperaryPointAdapter(TemperaryPointAdapterBuilder builder) {
        super(builder.wayPoint.getLatitude(),
                builder.wayPoint.getLongitude(),
                builder.wayPoint.getHeight(),
                builder.wayPoint.getSpeed());
        this.angle = builder.angle;
    }
    public TemperaryPointAdapter(TemporaryPoint temporaryPoint) {
        super(temporaryPoint.getLatitude(),
                temporaryPoint.getLongitude(),
                temporaryPoint.getHeight(),
                temporaryPoint.getSpeed());
        this.angle = temporaryPoint.getAngle();
    }
    public TemporaryPoint getTemporaryPoint() {
        return new TemporaryPoint(getLatitude(), getLongitude(), getHeight(), getSpeed(), getAngle());
    }
    public TemperaryPointAdapter updateSpeedWithInterpolation(WayPoint point1,
                                                              WayPoint point2,
                                                              Interpolator interpolator) {
        this.setSpeed(interpolator.interpolate(point1.getSpeed(), point2.getSpeed()));
        return this;
    }
    public TemperaryPointAdapter updateHeightWithInterpolation(WayPoint point1,
                                                               WayPoint point2,
                                                               Interpolator interpolator) {
        this.setHeight(interpolator.interpolate(point1.getSpeed(), point2.getSpeed()));
        return this;
    }

    public TemperaryPointAdapterBuilder getBuilder() {
        return builder().adapter(this);
    }

    public static TemperaryPointAdapterBuilder builder() {
        return new TemperaryPointAdapterBuilder();
    }

    public static class TemperaryPointAdapterBuilder {
        private WayPoint wayPoint = new WayPoint();
        private double angle;

        public TemperaryPointAdapterBuilder latitude(double latitude) {
            this.wayPoint.setLatitude(latitude);
            return this;
        }

        public TemperaryPointAdapterBuilder longitude(double longitude) {
            this.wayPoint.setLongitude(longitude);
            return this;
        }

        public TemperaryPointAdapterBuilder speed(double speed) {
            this.wayPoint.setSpeed(speed);
            return this;
        }

        public TemperaryPointAdapterBuilder height(double height) {
            this.wayPoint.setHeight(height);
            return this;
        }

        public TemperaryPointAdapterBuilder angle(double angle) {
            this.angle = angle;
            return this;
        }

        public TemperaryPointAdapterBuilder wayPoint(WayPoint wayPoint) {
            this.wayPoint = wayPoint;
            return this;
        }

        public TemperaryPointAdapterBuilder adapter(TemperaryPointAdapter adapter) {
            return wayPoint(adapter)
                    .angle(adapter.angle);
        }

        public TemperaryPointAdapterBuilder temperaryPoint(TemporaryPoint temporaryPoint) {
            return adapter(new TemperaryPointAdapter(temporaryPoint));
        }

        public TemperaryPointAdapterBuilder interpolateHeight(WayPoint point1,
                                                              WayPoint point2,
                                                              Interpolator interpolator) {
            return height(interpolator.interpolate(point1.getHeight(), point2.getHeight()));
        }

        public TemperaryPointAdapterBuilder interpolateSpeed(WayPoint point1,
                                                             WayPoint point2,
                                                             Interpolator interpolator) {
            return speed(interpolator.interpolate(point1.getSpeed(), point2.getSpeed()));
        }

        public TemperaryPointAdapterBuilder interpolateLatitude(WayPoint point1,
                                                                WayPoint point2,
                                                                Interpolator interpolator) {
            return latitude(interpolator.interpolate(point1.getLatitude(), point2.getLatitude()));
        }

        public TemperaryPointAdapterBuilder interpolateLongitude(WayPoint point1,
                                                                WayPoint point2,
                                                                Interpolator interpolator) {
            return longitude(interpolator.interpolate(point1.getLongitude(), point2.getLongitude()));
        }

        public TemperaryPointAdapter build() {
            return new TemperaryPointAdapter(this);
        }
    }
}
