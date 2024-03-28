package os.balashov.airplanedemo.aplication.utils;

public interface Interpolator {
    default double interpolate(double start, double end, double progress) {
        return start + progress * (end - start);
    }
    double interpolate(double start, double end);
}
