package lu.letzmarketplace.restapi.repositories;

import lu.letzmarketplace.restapi.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryUnitTests {

    @Autowired
    private UserRepository userRepository;

    private User mock;

    @BeforeEach
    void setUp() {
        mock = User.builder()
                .username("bob123")
                .email("bob@test.lu")
                .firstName("bob")
                .lastName("joe")
                .password("123")
                .build();
    }

    @Test
    @DisplayName("Test 1: Save new user")
    @Order(1)
    @Rollback(false)
    void saveUser() {
        // Action
        userRepository.save(mock);
        userRepository.flush();

        // Verify
        assertThat(mock.getId()).isNotNull();
        assertThat(mock.getCreatedAt()).isNotNull();
        assertThat(mock.getUpdatedAt()).isNotNull();

        LocalDateTime now = LocalDateTime.now();
        assertThat(mock.getCreatedAt()).isBefore(now);
        assertThat(mock.getUpdatedAt()).isBefore(now);

        assertThat(userRepository.findAll()).size().isEqualTo(1);
    }

    @Test
    @DisplayName("Test 2: Find user by email")
    @Order(2)
    void findUserByEmail() {
        // Action
        User user = userRepository.findByEmail(mock.getEmail())
                .orElse(null);

        // Verify
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(mock.getEmail());

        // Action
        user = userRepository.findByEmail("unknown@test.lu")
                .orElse(null);

        // Verify
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Test 3: Find user by username")
    @Order(3)
    void findByUsername() {
        // Action
        User user = userRepository.findByUsername(mock.getUsername())
                .orElse(null);

        // Verify
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(mock.getUsername());

        // Action
        user = userRepository.findByUsername("unknown")
                .orElse(null);

        // Verify
        assertThat(user).isNull();
    }
}