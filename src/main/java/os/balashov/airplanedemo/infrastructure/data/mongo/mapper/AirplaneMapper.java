package os.balashov.airplanedemo.infrastructure.data.mongo.mapper;

import org.springframework.stereotype.Component;
import os.balashov.airplanedemo.domain.entities.Airplane;
import os.balashov.airplanedemo.infrastructure.data.mongo.model.AirplaneDocument;

@Component
public class AirplaneMapper {

    public AirplaneDocument toDocument(Airplane airplane) {
        AirplaneDocument document = new AirplaneDocument();
        document.setId(airplane.getId().toString());
        document.setAirplane(airplane);
        return document;
    }

    public Airplane toEntity(AirplaneDocument document) {
        return document.getAirplane();
    }
}

