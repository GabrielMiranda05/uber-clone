package com.gabrielmiranda.uberclone.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gabrielmiranda.uberclone.TestcontainersConfig;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(TestcontainersConfig.class)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void findsUserByEmailIgnoringCase() {
        userRepository.save(new User("Ana", "ana@example.com", "11999990000", "hash", Role.PASSENGER));

        assertThat(userRepository.findByEmail("ANA@Example.com")).isPresent();
        assertThat(userRepository.existsByEmail("Ana@EXAMPLE.com")).isTrue();
        assertThat(userRepository.findByEmail("outra@example.com")).isEmpty();
    }

    @Test
    void databaseRejectsEmailDifferingOnlyByCase() {
        userRepository.saveAndFlush(new User("Ana", "ana@example.com", "11999990000", "hash", Role.PASSENGER));

        assertThatThrownBy(() -> jdbcTemplate.update(
            "INSERT INTO users (id, name, email, phone, password_hash, role) VALUES (?, ?, ?, ?, ?, ?)",
            UUID.randomUUID(), "Bob", "ANA@EXAMPLE.COM", "11888880000", "hash", "PASSENGER"))
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}
