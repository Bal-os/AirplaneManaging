package os.balashov.airplanedemo.domain.entities;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Airplane {
    private Long id;
    private AirplaneCharacteristics characteristics;
    private TemporaryPoint position;
    private List<Flight> flights;
}
