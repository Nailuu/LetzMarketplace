package lu.letzmarketplace.restapi.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterEach;

import java.util.Random;
import java.util.Set;

public abstract class BaseValidationsTester<D> {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    protected Set<ConstraintViolation<D>> violations;
    protected D dto;

    @AfterEach
    void tearDown() {
        violations.clear();
    }

    // Validate DTO using Validator and set violations
    protected void validate() {
        violations = validator.validate(dto);
    }

    public static String generateRandomAlphanumericString(int length) {
        return generateRandomString(length, "[A-Za-z0-9]");
    }

    public static String generateRandomString(int length, String regex) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char c;
            do {
                c = (char) (random.nextInt(128));  // Generate random character
            } while (!String.valueOf(c).matches(regex)); // Ensure it matches the regex
            sb.append(c);
        }

        return sb.toString();
    }
}
