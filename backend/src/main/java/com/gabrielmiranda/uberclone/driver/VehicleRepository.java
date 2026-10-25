package com.gabrielmiranda.uberclone.driver;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    Optional<Vehicle> findByDriverUserId(UUID driverId);

    boolean existsByPlate(String plate);
}
