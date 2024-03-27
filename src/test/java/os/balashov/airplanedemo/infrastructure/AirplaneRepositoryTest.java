package os.balashov.airplanedemo.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import os.balashov.airplanedemo.domain.entities.Airplane;
import os.balashov.airplanedemo.domain.entities.AirplaneCharacteristics;
import os.balashov.airplanedemo.domain.entities.TemporaryPoint;
import os.balashov.airplanedemo.domain.repositories.AirplaneRepository;
import os.balashov.airplanedemo.infrastructure.config.SimpleAppConfig;
import os.balashov.airplanedemo.infrastructure.data.mongo.mapper.AirplaneMapper;
import os.balashov.airplanedemo.infrastructure.data.mongo.model.AirplaneDocument;
import os.balashov.airplanedemo.infrastructure.data.mongo.repository.AirplaneRepositoryImpl;
import os.balashov.airplanedemo.infrastructure.data.mongo.repository.MongoAirplaneRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {SimpleAppConfig.class})
public class AirplaneRepositoryTest {
    @Mock
    private MongoAirplaneRepository mockMongoAirplaneRepository;
    @Mock
    private AirplaneMapper mockAirplaneMapper;
    private AirplaneRepository airplaneRepository;

    @BeforeEach
    public void setUp() {
        airplaneRepository = new AirplaneRepositoryImpl(mockMongoAirplaneRepository, mockAirplaneMapper);
    }

    @Test
    public void testFindAllEmptyList() {
        List<AirplaneDocument> emptyList = Collections.emptyList();
        when(mockMongoAirplaneRepository.findAll()).thenReturn(emptyList);
        when(mockAirplaneMapper.toEntity(null)).thenReturn(null);

        List<Airplane> airplanes = airplaneRepository.findAll();

        assertTrue(airplanes.isEmpty());
    }

    @Test
    public void testFindAllWithListAndMapping() {
        Airplane airplane = new Airplane(1L,
                new AirplaneCharacteristics(100, 10, 1000, 45),
                new TemporaryPoint(0, 0, 0, 0, 0),
                new ArrayList<>());
        AirplaneDocument document = new AirplaneDocument();
        document.setId(airplane.getId().toString());
        document.setAirplane(airplane);
        List<AirplaneDocument> documentList = Collections.singletonList(document);
        when(mockMongoAirplaneRepository.findAll()).thenReturn(documentList);
        when(mockAirplaneMapper.toEntity(document)).thenReturn(airplane);

        List<Airplane> airplanes = airplaneRepository.findAll();

        assertEquals(1, airplanes.size());
        assertEquals(airplane, airplanes.get(0));
    }

    @Test
    public void testSaveValidAirplane() {
        Airplane airplane = new Airplane(1L,
                new AirplaneCharacteristics(100, 10, 1000, 45),
                new TemporaryPoint(0, 0, 0, 0, 0),
                new ArrayList<>());
        AirplaneDocument document = new AirplaneDocument();
        document.setId(airplane.getId().toString());
        document.setAirplane(airplane);
        when(mockAirplaneMapper.toDocument(airplane)).thenReturn(document);

        airplaneRepository.save(airplane);

        verify(mockMongoAirplaneRepository).save(document);
    }

}
