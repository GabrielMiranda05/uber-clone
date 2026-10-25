package com.gabrielmiranda.uberclone.driver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gabrielmiranda.uberclone.TestcontainersConfig;
import com.gabrielmiranda.uberclone.user.Role;
import com.gabrielmiranda.uberclone.user.User;
import com.gabrielmiranda.uberclone.user.UserRepository;
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
class DriverRepositoriesTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    DriverProfileRepository driverProfileRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    DriverProfile driver;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(
            new User("Beto", "beto@example.com", "11988880000", "hash", Role.DRIVER));
        driver = driverProfileRepository.save(new DriverProfile(user, "12345678900"));
    }

    @Test
    void newDriverStartsOfflineAndIsFoundByStatus() {
        assertThat(driverProfileRepository.findByStatus(DriverStatus.OFFLINE)).hasSize(1);
        assertThat(driverProfileRepository.findByStatus(DriverStatus.ONLINE)).isEmpty();
    }

    @Test
    void findsVehicleByDriverAndNormalizesPlate() {
        vehicleRepository.save(new Vehicle(driver, "abc1234", "Onix", "Prata", (short) 2022));

        assertThat(vehicleRepository.findByDriverUserId(driver.getUserId())).isPresent();
        assertThat(vehicleRepository.existsByPlate("ABC1234")).isTrue();
    }

    @Test
    void databaseRejectsSecondVehicleForSameDriver() {
        vehicleRepository.saveAndFlush(new Vehicle(driver, "ABC1234", "Onix", "Prata", (short) 2022));

        assertThatThrownBy(() -> vehicleRepository.saveAndFlush(
            new Vehicle(driver, "XYZ9876", "HB20", "Preto", (short) 2021)))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

}
