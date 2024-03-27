package os.balashov.airplanedemo.infrastructure.data.mongo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import os.balashov.airplanedemo.domain.entities.Airplane;

@Document(collection = "airplanes")
@Getter
@Setter
public class AirplaneDocument {
    @Id
    private String id;
    private Airplane airplane;
}
