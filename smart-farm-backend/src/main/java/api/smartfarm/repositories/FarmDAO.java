package api.smartfarm.repositories;

import api.smartfarm.models.documents.Farm;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface FarmDAO extends MongoRepository<Farm, String> {

    @Query(value="{'_id': ?0, 'sectors.crop.plants.id': ?1}")
    Optional<Farm> findByIdAndSectorsCropPlantsId(String farmId, String plantId);

    Optional<Farm> findByUserId(String userId);

}
