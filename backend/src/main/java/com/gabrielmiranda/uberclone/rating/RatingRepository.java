package com.gabrielmiranda.uberclone.rating;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

    boolean existsByRideIdAndRaterId(UUID rideId, UUID raterId);

    long countByRatedId(UUID ratedId);

    @Query("select avg(r.score) from Rating r where r.rated.id = :userId")
    Double findAverageScoreByRatedId(@Param("userId") UUID userId);
}
