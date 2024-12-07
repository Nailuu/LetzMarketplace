package lu.letzmarketplace.restapi.configurations;

import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EnvironmentVariablesUnitTests {

    @Test
    @DisplayName("CORS")
    public void testCORS(@Value("${CORS}") String var) {
        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("JWT_SECRET_KEY")
    public void testJwtSecretKey(@Value("${JWT_SECRET_KEY}") String var) {
        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
        assertThat(Decoders.BASE64.decode(var).length).isEqualTo(512);
    }

    @Test
    @DisplayName("DB_HOST")
    public void testDbHost(@Value("${DB_HOST}") String var) {
        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("DB_PORT")
    public void testDbPort(@Value("${DB_PORT}") String var) {
        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("DB_NAME")
    public void testDbName(@Value("${DB_NAME}") String var) {
        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("DB_USERNAME")
    public void testDbUsername(@Value("${DB_USERNAME}") String var) {
        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("DB_PASSWORD")
    public void testDbPassword(@Value("${DB_PASSWORD}") String var) {
        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_HOST")
    public void testTestDbHost(@Value("${TEST_DB_HOST}") String var) {
        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_PORT")
    public void testTestDbPort(@Value("${TEST_DB_PORT}") String var) {
        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_NAME")
    public void testTestDbName(@Value("${TEST_DB_NAME}") String var) {
        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_USERNAME")
    public void testTestDbUsername(@Value("${TEST_DB_USERNAME}") String var) {
        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_PASSWORD")
    public void testTestDbPassword(@Value("${TEST_DB_PASSWORD}") String var) {
        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }
}
