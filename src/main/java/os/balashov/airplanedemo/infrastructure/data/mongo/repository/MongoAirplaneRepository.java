package os.balashov.airplanedemo.infrastructure.data.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import os.balashov.airplanedemo.infrastructure.data.mongo.model.AirplaneDocument;

public interface MongoAirplaneRepository extends MongoRepository<AirplaneDocument, String> {
}
