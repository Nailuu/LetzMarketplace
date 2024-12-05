package lu.letzmarketplace.restapi.dto;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserLoginRequestDTOValidationsTests extends BaseValidationsTester<UserLoginRequestDTO> {
    
    @BeforeEach
    void setUp() {
        dto = UserLoginRequestDTO.builder()
                .email("bob@test.lu")
                .password(generateRandomAlphanumericString(12))
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
}