package api.smartfarm.daos;

import api.smartfarm.entities.Farm;

import java.util.Optional;

public interface FarmDAO {
    Optional<Farm> getById(String id);
    Farm createFarm(Farm farm);
}
