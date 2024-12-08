package lu.letzmarketplace.restapi.repositories;

import lu.letzmarketplace.restapi.models.JWTRefreshTokenHistory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JWTRefreshTokenHistoryRepositoryUnitTests {

    @Autowired
    private JWTRefreshTokenHistoryRepository jwtRefreshTokenHistoryRepository;

    private JWTRefreshTokenHistory data;

    @BeforeEach
    void setUp() {
        data = JWTRefreshTokenHistory.builder()
                .token("123")
                .userId(UUID.randomUUID())
                .build();
    }

    @Test
    @DisplayName("Test 1: Save new history")
    @Order(1)
    void saveHistory() {
        // Action
        jwtRefreshTokenHistoryRepository.save(data);

        // Verify
        assertThat(jwtRefreshTokenHistoryRepository.findAll()).size().isEqualTo(1);
    }

    @Test
    @DisplayName("Test 2: Find by user id")
    @Order(2)
    void findByUserId() {
        // Action
        jwtRefreshTokenHistoryRepository.save(data);
        JWTRefreshTokenHistory result = jwtRefreshTokenHistoryRepository.findByUserId(data.getUserId())
                .orElse(null);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(data);
    }

    @Test
    @DisplayName("Test 3: Update")
    @Order(3)
    void update() {
        // Action
        jwtRefreshTokenHistoryRepository.save(data);
        jwtRefreshTokenHistoryRepository.flush();

        String oldToken = data.getToken();
        data.setToken("token789");
        jwtRefreshTokenHistoryRepository.save(data);

        // Assert
        JWTRefreshTokenHistory result = jwtRefreshTokenHistoryRepository.findByUserId(data.getUserId()).orElse(null);

        assertThat(result).isNotNull();
        assertThat(result.getToken()).isNotEqualTo(oldToken);
    }
}