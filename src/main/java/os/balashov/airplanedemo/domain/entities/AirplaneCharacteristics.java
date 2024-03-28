package os.balashov.airplanedemo.domain.entities;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AirplaneCharacteristics {
    private double maxSpeed;
    private double maxChangeSpeed;
    private double maxChangeHeight;
    private double maxChangeAngle;
}
