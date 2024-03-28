package os.balashov.airplanedemo.application.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import os.balashov.airplanedemo.domain.entities.WayPoint;

@Getter
@AllArgsConstructor
public class Vector {
    private double x;
    private double y;

    public Vector(WayPoint a, WayPoint b) {
        this.x = a.getLongitude() - b.getLongitude();
        this.y = a.getLatitude() - b.getLatitude();
    }

    public Vector(WayPoint a, TemperaryPointAdapter b) {
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
