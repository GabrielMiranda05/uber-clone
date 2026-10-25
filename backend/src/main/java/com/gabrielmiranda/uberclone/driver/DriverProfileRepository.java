package com.gabrielmiranda.uberclone.driver;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverProfileRepository extends JpaRepository<DriverProfile, UUID> {

    List<DriverProfile> findByStatus(DriverStatus status);
}
