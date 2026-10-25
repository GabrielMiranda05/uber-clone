package com.gabrielmiranda.uberclone.ride;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gabrielmiranda.uberclone.TestcontainersConfig;
import com.gabrielmiranda.uberclone.driver.DriverProfile;
import com.gabrielmiranda.uberclone.driver.DriverProfileRepository;
import com.gabrielmiranda.uberclone.user.Role;
import com.gabrielmiranda.uberclone.user.User;
import com.gabrielmiranda.uberclone.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(TestcontainersConfig.class)
@ActiveProfiles("test")
class RideRepositoryTest {

    @Autowired
    RideRepository rideRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DriverProfileRepository driverProfileRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    EntityManager entityManager;

    User passenger;
    DriverProfile driver;

    @BeforeEach
    void setUp() {
        passenger = userRepository.save(
            new User("Ana", "ana@example.com", "11999990000", "hash", Role.PASSENGER));
        User driverUser = userRepository.save(
            new User("Beto", "beto@example.com", "11988880000", "hash", Role.DRIVER));
        driver = driverProfileRepository.save(new DriverProfile(driverUser, "12345678900"));
    }

    private Ride newRide() {
        return new Ride(passenger, -23.55, -46.63, "Origem", -23.56, -46.65, "Destino", 5000, 2500);
    }

    @Test
    void allowsOnlyOneActiveRidePerPassenger() {
        rideRepository.saveAndFlush(newRide());

        assertThatThrownBy(() -> rideRepository.saveAndFlush(newRide()))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void acceptedRideRequiresDriver() {
        Ride ride = rideRepository.saveAndFlush(newRide());

        assertThatThrownBy(() -> jdbcTemplate.update(
            "UPDATE rides SET status = 'ACCEPTED' WHERE id = ?", ride.getId()))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void cancelledRideRequiresCancellationTimestamp() {
        Ride ride = rideRepository.saveAndFlush(newRide());

        assertThatThrownBy(() -> jdbcTemplate.update(
            "UPDATE rides SET status = 'CANCELLED' WHERE id = ?", ride.getId()))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void historyIsNewestFirstAndActiveRideIsFound() {
        Ride first = rideRepository.saveAndFlush(newRide());
        jdbcTemplate.update(
            "UPDATE rides SET status = 'CANCELLED', cancelled_at = now() WHERE id = ?", first.getId());
        entityManager.clear();

        Ride second = rideRepository.saveAndFlush(newRide());
        entityManager.clear();

        Page<Ride> history = rideRepository.findByPassengerIdOrderByRequestedAtDesc(
            passenger.getId(), PageRequest.of(0, 10));

        assertThat(history.getContent()).extracting(Ride::getId)
            .containsExactly(second.getId(), first.getId());
        assertThat(rideRepository.findFirstByPassengerIdAndStatusIn(passenger.getId(), RideStatus.ACTIVE))
            .get()
            .extracting(Ride::getId)
            .isEqualTo(second.getId());
    }
}
