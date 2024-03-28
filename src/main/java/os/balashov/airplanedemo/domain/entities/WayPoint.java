package os.balashov.airplanedemo.domain.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class WayPoint {
    private double latitude; // y
    private double longitude; // x
    private double height;
    private double speed;
}