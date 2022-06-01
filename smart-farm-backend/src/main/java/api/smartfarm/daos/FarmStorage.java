package api.smartfarm.daos;

import api.smartfarm.entities.Farm;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class FarmStorage implements FarmDAO {

    @Override
    public Optional<Farm> getById(String id) {
        return Optional.of(new Farm("1", "Tomate", 21.5));
    }

    @Override
    public Farm createFarm(Farm farm) {
        return null;
    }
}
