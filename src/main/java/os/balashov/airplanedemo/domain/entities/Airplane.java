package os.balashov.airplanedemo.domain.entities;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Airplane {
    @NonNull
    private Long id;
    @NonNull
    private AirplaneCharacteristics characteristics;
    private TemporaryPoint position;
    @NonNull
    private List<Flight> flights;
}
