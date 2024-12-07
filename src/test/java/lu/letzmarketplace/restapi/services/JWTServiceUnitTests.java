package lu.letzmarketplace.restapi.services;

import lu.letzmarketplace.restapi.models.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class JWTServiceUnitTests {
    private final String mockSecretKey = "414f385697d7137449b266c01dc01fa93f1f26a8e430a5efe976b0a7e95b189af4d1d70e03699e50b1635aedcc08d8679ce53e4586c5f577add7bf715b8346b5cef815a3578b86c57fa4ac859232467a394e3a470dc8d580b0e470679eb86a167b48c348efabf9db2899d671d1eac6379d8bccff32dbf7342eabe491e1f22ad398aa51cb3fb6df3a6ff3509d86989b66745f48df11f22feeac102e51e6ee0c40dcf5804a6ecf87774883142e506e969546f12ee7449b2d505c29abd98c227a03704582948cf4abf26dba084ba56369656f6f3834afe4ab0a067ca98f0b8c4e1f2127cefd7e0d895d4c9e1eb223965501e0c90b22499e93305391654d6332c3ef";

    @InjectMocks
    private JWTService jwtService;

    private User user;

    @BeforeEach
    public void setUp() throws Exception {
        Field secretKeyField = JWTService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtService, mockSecretKey);

        user = User.builder()
                .id(UUID.randomUUID())
                .username("bob123")
                .email("bob@test.lu")
                .firstName("bob")
                .lastName("joe")
                .password("123")
                .build();
    }

    @Test
    @DisplayName("Test 1: Generate access token")
    @Order(1)
    void generateAccessToken() {
        // Pre-condition
        String token = jwtService.generateAccessToken(user);

        // Assert & Verify
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("Test 2: Generate refresh token")
    @Order(2)
    void generateRefreshToken() {
        // Pre-condition
        String token = jwtService.generateRefreshToken(user);

        // Assert & Verify
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("Test 3: Extract email from access token")
    @Order(3)
    void extractAccessTokenEmail() {
        // Pre-condition
        String token = jwtService.generateAccessToken(user);
        String email = jwtService.extractEmail(token);

        // Assert & Verify
        assertThat(email).isNotNull();
        assertThat(email).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Test 4: Extract email from refresh token")
    @Order(4)
    void extractRefreshTokenEmail() {
        // Pre-condition
        String token = jwtService.generateAccessToken(user);
        String email = jwtService.extractEmail(token);

        // Assert & Verify
        assertThat(email).isNotNull();
        assertThat(email).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Test 5: Extract type from access token")
    @Order(4)
    void extractAccessTokenType() {
        // Pre-condition
        String token = jwtService.generateAccessToken(user);
        String type = jwtService.extractType(token);

        // Assert & Verify
        assertThat(type).isNotNull();
        assertThat(type).isEqualTo("access");
    }

    @Test
    @DisplayName("Test 5: Extract type from refresh token")
    @Order(5)
    void extractRefreshTokenType() {
        // Pre-condition
        String token = jwtService.generateRefreshToken(user);
        String type = jwtService.extractType(token);

        // Assert & Verify
        assertThat(type).isNotNull();
        assertThat(type).isEqualTo("refresh");
    }

    @Test
    @DisplayName("Test 5: Extract subject from access token")
    @Order(6)
    void extractAccessTokenSubject() {
        // Pre-condition
        String token = jwtService.generateAccessToken(user);
        String subject = jwtService.extractSubject(token);

        // Assert & Verify
        assertThat(subject).isNotNull();
        assertThat(subject).isEqualTo(user.getId().toString());
    }

    @Test
    @DisplayName("Test 7: Extract subject from refresh token")
    @Order(7)
    void extractRefreshTokenSubject() {
        // Pre-condition
        String token = jwtService.generateRefreshToken(user);
        String subject = jwtService.extractSubject(token);

        // Assert & Verify
        assertThat(subject).isNotNull();
        assertThat(subject).isEqualTo(user.getId().toString());
    }

    @Test
    @DisplayName("Test 8: Validate valid access token")
    @Order(8)
    void validateValidAccessToken() {
        // Pre-condition
        String token = jwtService.generateAccessToken(user);

        // Assert & Verify
        assertThat(jwtService.validateToken(token, user)).isTrue();
    }

    @Test
    @DisplayName("Test 9: Validate valid refresh token")
    @Order(9)
    void validateValidRefreshToken() {
        // Pre-condition
        String token = jwtService.generateRefreshToken(user);

        // Assert & Verify
        assertThat(jwtService.validateToken(token, user)).isTrue();
    }

    @Test
    @DisplayName("Test 10: Validate invalid token")
    @Order(10)
    void validateInvalidToken() {
        // Pre-condition
        String token = "token123";

        // Assert & Verify
        assertThat(jwtService.validateToken(token, user)).isFalse();
    }

    @Test
    @DisplayName("Test 11: Validate valid access token but wrong user")
    @Order(11)
    void validateValidAccessTokenWrongUser() {
        // Pre-condition
        String token = jwtService.generateAccessToken(user);
        user.setId(UUID.randomUUID());

        // Assert & Verify
        assertThat(jwtService.validateToken(token, user)).isFalse();
    }

    @Test
    @DisplayName("Test 12: Validate valid refresh token but wrong user")
    @Order(12)
    void validateValidRefreshTokenWrongUser() {
        // Pre-condition
        String token = jwtService.generateRefreshToken(user);
        user.setId(UUID.randomUUID());

        // Assert & Verify
        assertThat(jwtService.validateToken(token, user)).isFalse();
    }

    @Test
    @DisplayName("Test 12: Validate expired access token")
    @Order(12)
    void validateExpiredAccessToken() throws Exception {
        // Pre-condition
        Field field = JWTService.class.getDeclaredField("ACCESS_TOKEN_EXPIRATION");
        field.setAccessible(true);
        field.set(jwtService, 100L);
        String token = jwtService.generateAccessToken(user);

        Thread.sleep(101);

        // Assert & Verify
        assertThat(jwtService.validateToken(token, user)).isFalse();
    }

    @Test
    @DisplayName("Test 13: Validate expired refresh token")
    @Order(13)
    void validateExpiredRefreshToken() throws Exception {
        // Pre-condition
        Field field = JWTService.class.getDeclaredField("REFRESH_TOKEN_EXPIRATION");
        field.setAccessible(true);
        field.set(jwtService, 100L);
        String token = jwtService.generateRefreshToken(user);

        Thread.sleep(101);

        // Assert & Verify
        assertThat(jwtService.validateToken(token, user)).isFalse();
    }

    @Test
    @DisplayName("")
    @Order(14)
    void storeOrUpdateRefreshToken() {
        assertThat(null).isNotNull();
    }

    @Test
    @DisplayName("")
    @Order(15)
    void getRefreshTokenHistoryByUserId() {
        assertThat(null).isNotNull();
    }
}