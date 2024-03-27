package os.balashov.airplanedemo.domain.entities;

import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class TemporaryPoint {
    private double latitude; // y
    private double longitude; // x
    private double height;
    private double speed;
    private double angle;
}