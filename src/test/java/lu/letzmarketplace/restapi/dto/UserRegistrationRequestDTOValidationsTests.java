package lu.letzmarketplace.restapi.dto;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRegistrationRequestDTOValidationsTests extends BaseValidationsTester<UserRegistrationRequestDTO> {

    @BeforeEach
    void setUp() {
        dto = UserRegistrationRequestDTO.builder()
                .username(generateRandomAlphanumericString(10))
                .email("bob@test.lu")
                .firstName(generateRandomAlphanumericString(10))
                .lastName(generateRandomAlphanumericString(10))
                .password(generateRandomAlphanumericString(10))
                .build();
    }

    @Test
    @DisplayName("Test 1: All")
    @Order(1)
    void testValidInputs() {
        validate();
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Test 2: Email")
    @Order(2)
    void testEmail() {
        // Blank
        dto.setEmail(null);
        validate();
        assertFalse(violations.isEmpty());

        // Invalid email
        dto.setEmail("bob@test.");
        validate();
        assertFalse(violations.isEmpty());

        // Too long (101 characters)
        dto.setEmail(generateRandomAlphanumericString(91) + "@gmail.com");
        validate();
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Test 3: Password")
    @Order(3)
    void testPassword() {
        // Blank
        dto.setPassword(null);
        validate();
        assertFalse(violations.isEmpty());

        // Too short (7 characters)
        dto.setPassword(generateRandomAlphanumericString(7));
        validate();
        assertFalse(violations.isEmpty());

        // Too long (101 characters)
        dto.setPassword(generateRandomAlphanumericString(101));
        validate();
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Test 4: Username")
    @Order(4)
    void testUsername() {
        // Blank
        dto.setUsername(null);
        validate();
        assertFalse(violations.isEmpty());

        // Too short (3 characters)
        dto.setUsername(generateRandomAlphanumericString(3));
        validate();
        assertFalse(violations.isEmpty());

        // Too long (101 characters)
        dto.setUsername(generateRandomAlphanumericString(101));
        validate();
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Test 5: First Name")
    @Order(5)
    void testFirstName() {
        // Blank
        dto.setFirstName(null);
        validate();
        assertFalse(violations.isEmpty());

        // Too short (1 character)
        dto.setFirstName("a");
        validate();
        assertFalse(violations.isEmpty());

        // Too long (31 characters)
        dto.setUsername(generateRandomAlphanumericString(31));
        validate();
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Test 6: Last Name")
    @Order(6)
    void testLastName() {
        // Blank
        dto.setLastName(null);
        validate();
        assertFalse(violations.isEmpty());

        // Too short (1 character)
        dto.setLastName("a");
        validate();
        assertFalse(violations.isEmpty());

        // Too long (31 characters)
        dto.setLastName(generateRandomAlphanumericString(31));
        validate();
        assertFalse(violations.isEmpty());
    }
}