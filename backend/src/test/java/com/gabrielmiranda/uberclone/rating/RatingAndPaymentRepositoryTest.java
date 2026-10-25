package com.gabrielmiranda.uberclone.rating;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gabrielmiranda.uberclone.TestcontainersConfig;
import com.gabrielmiranda.uberclone.payment.Payment;
import com.gabrielmiranda.uberclone.payment.PaymentMethod;
import com.gabrielmiranda.uberclone.payment.PaymentRepository;
import com.gabrielmiranda.uberclone.ride.Ride;
import com.gabrielmiranda.uberclone.ride.RideRepository;
import com.gabrielmiranda.uberclone.user.Role;
import com.gabrielmiranda.uberclone.user.User;
import com.gabrielmiranda.uberclone.user.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(TestcontainersConfig.class)
@ActiveProfiles("test")
class RatingAndPaymentRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RideRepository rideRepository;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    PaymentRepository paymentRepository;

    User passenger;
    User driverUser;
    Ride ride;

    @BeforeEach
    void setUp() {
        passenger = userRepository.save(
            new User("Ana", "ana@example.com", "11999990000", "hash", Role.PASSENGER));
        driverUser = userRepository.save(
            new User("Beto", "beto@example.com", "11988880000", "hash", Role.DRIVER));
        ride = rideRepository.save(
            new Ride(passenger, -23.55, -46.63, "Origem", -23.56, -46.65, "Destino", 5000, 2500));
    }

    @Test
    void rejectsSecondRatingFromSameRaterOnSameRide() {
        ratingRepository.saveAndFlush(new Rating(ride, passenger, driverUser, (short) 5, "Ótimo"));

        assertThat(ratingRepository.existsByRideIdAndRaterId(ride.getId(), passenger.getId())).isTrue();
        assertThatThrownBy(() -> ratingRepository.saveAndFlush(
            new Rating(ride, passenger, driverUser, (short) 4, null)))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void rejectsSelfRatingAndScoreOutOfRange() {
        assertThatThrownBy(() -> ratingRepository.saveAndFlush(
            new Rating(ride, passenger, passenger, (short) 5, null)))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void rejectsScoreOutOfRange() {
        assertThatThrownBy(() -> ratingRepository.saveAndFlush(
            new Rating(ride, passenger, driverUser, (short) 6, null)))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void computesAverageScoreAndReturnsNullWithoutRatings() {
        ratingRepository.save(new Rating(ride, passenger, driverUser, (short) 5, null));
        ratingRepository.save(new Rating(ride, driverUser, passenger, (short) 3, null));

        assertThat(ratingRepository.findAverageScoreByRatedId(driverUser.getId())).isEqualTo(5.0);
        assertThat(ratingRepository.findAverageScoreByRatedId(passenger.getId())).isEqualTo(3.0);
        assertThat(ratingRepository.countByRatedId(driverUser.getId())).isEqualTo(1);
        assertThat(ratingRepository.findAverageScoreByRatedId(UUID.randomUUID())).isNull();
    }

    @Test
    void findsPaymentByRideAndRejectsSecondPayment() {
        paymentRepository.saveAndFlush(new Payment(ride, 2500, PaymentMethod.PIX));

        assertThat(paymentRepository.findByRideId(ride.getId())).isPresent();
        assertThatThrownBy(() -> paymentRepository.saveAndFlush(new Payment(ride, 2500, PaymentMethod.CASH)))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

}
