package os.balashov.airplanedemo.domain.entities;

import lombok.*;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    private Long number;
    private List<WayPoint> wayPoints;
    private List<TemporaryPoint> passedPoints;
}