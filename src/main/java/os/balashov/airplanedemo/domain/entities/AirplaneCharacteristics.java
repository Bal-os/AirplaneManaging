package os.balashov.airplanedemo.domain.entities;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirplaneCharacteristics {
    private double maxSpeed;
    private double maxChangeSpeed;
    private double maxChangeHeight;
    private double maxChangeAngle;
}
