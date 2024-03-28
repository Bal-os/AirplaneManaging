package os.balashov.airplanedemo.domain.entities;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WayPoint {
    private double latitude; // y
    private double longitude; // x
    private double height;
    private double speed;
}