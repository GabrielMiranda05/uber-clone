package com.gabrielmiranda.uberclone.ride;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, UUID> {

    @EntityGraph(attributePaths = {"driver", "driver.user"})
    Page<Ride> findByPassengerIdOrderByRequestedAtDesc(UUID passengerId, Pageable pageable);

    @EntityGraph(attributePaths = {"passenger"})
    Page<Ride> findByDriverUserIdOrderByRequestedAtDesc(UUID driverId, Pageable pageable);

    Optional<Ride> findFirstByPassengerIdAndStatusIn(UUID passengerId, Collection<RideStatus> statuses);

    Optional<Ride> findFirstByDriverUserIdAndStatusIn(UUID driverId, Collection<RideStatus> statuses);
}
