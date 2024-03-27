package os.balashov.airplanedemo.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import os.balashov.airplanedemo.domain.entities.Airplane;
import os.balashov.airplanedemo.domain.entities.AirplaneCharacteristics;
import os.balashov.airplanedemo.domain.entities.TemporaryPoint;
import os.balashov.airplanedemo.infrastructure.data.mongo.mapper.AirplaneMapper;
import os.balashov.airplanedemo.infrastructure.data.mongo.model.AirplaneDocument;

import java.util.ArrayList;


public class AirplaneMapperTest {
    @Test
    public void testToDocumentValidAirplane() {
        Airplane airplane = new Airplane(1L,
                new AirplaneCharacteristics(100, 10, 1000, 45),
                new TemporaryPoint(0, 0, 0, 0, 0),
                new ArrayList<>());
        AirplaneMapper mapper = new AirplaneMapper();
        AirplaneDocument document = mapper.toDocument(airplane);

        Assertions.assertEquals(airplane.getId().toString(), document.getId());
        Assertions.assertSame(airplane, document.getAirplane());
    }

    @Test
    public void testToDocumentNullAirplane() {
        AirplaneMapper mapper = new AirplaneMapper();

        Assertions.assertThrows(NullPointerException.class, () -> mapper.toDocument(null));
    }

    @Test
    public void testToEntityValidDocument() {
        Airplane airplane = new Airplane(1L, new AirplaneCharacteristics(100, 10, 1000, 45), new TemporaryPoint(0, 0, 0, 0, 0), new ArrayList<>());
        AirplaneDocument document = new AirplaneDocument();
        document.setId(airplane.getId().toString());
        document.setAirplane(airplane);

        AirplaneMapper mapper = new AirplaneMapper();
        Airplane retrievedAirplane = mapper.toEntity(document);

        Assertions.assertSame(airplane, retrievedAirplane);
    }

    @Test
    public void testToEntityNullDocument() {
        AirplaneMapper mapper = new AirplaneMapper();

        Assertions.assertThrows(NullPointerException.class, () -> mapper.toEntity(null));
    }

    @Test
    public void testToEntityNullAirplaneInDocument() {
        AirplaneDocument document = new AirplaneDocument();
        document.setId("1");

        AirplaneMapper mapper = new AirplaneMapper();
        Airplane retrievedAirplane = mapper.toEntity(document);

        Assertions.assertNull(retrievedAirplane);
    }


}

