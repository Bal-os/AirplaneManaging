package os.balashov.airplanedemo.infrastructure.data.mongo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import os.balashov.airplanedemo.domain.entities.Airplane;
import os.balashov.airplanedemo.domain.repositories.AirplaneRepository;
import os.balashov.airplanedemo.infrastructure.data.mongo.mapper.AirplaneMapper;
import os.balashov.airplanedemo.infrastructure.data.mongo.model.AirplaneDocument;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class AirplaneRepositoryImpl implements AirplaneRepository {
    private final MongoAirplaneRepository mongoAirplaneRepository;
    private final AirplaneMapper airplaneMapper;

    @Override
    public List<Airplane> findAll() {
        return mongoAirplaneRepository.findAll()
                .parallelStream()
                .map(airplaneMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Airplane airplane) {
        AirplaneDocument airplaneDocument = airplaneMapper.toDocument(airplane);
        mongoAirplaneRepository.save(airplaneDocument);
    }
}
