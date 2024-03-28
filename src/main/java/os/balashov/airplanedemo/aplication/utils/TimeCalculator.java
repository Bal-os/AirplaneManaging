package os.balashov.airplanedemo.aplication.utils;

public interface TimeCalculator {
    default double calculateTimeFromSpeed(double distance, double startSpeed, double endSpeed) {
        if (distance < 0 || startSpeed < 0 || endSpeed < 0) return 0;
        double averageSpeed = (startSpeed + endSpeed) / 2.0;
        return distance / averageSpeed;
    }

    default double calculateTimeFromHeightDifference(double heightDifference, double heightChangeSpeed) {
        if (heightChangeSpeed <= 0 || heightDifference < 0) return 0;
        return heightDifference / heightChangeSpeed;
    }

    default double calculateTimeFromSpeedChange(double startSpeed, double endSpeed, double speedChangeAmount) {
        if (speedChangeAmount <= 0 || startSpeed < 0 || endSpeed < 0) return 0;
        double speedDiff = Math.abs(startSpeed - endSpeed);
        return speedDiff / speedChangeAmount;
    }
}
