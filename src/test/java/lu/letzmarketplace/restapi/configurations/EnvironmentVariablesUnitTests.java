package lu.letzmarketplace.restapi.configurations;

import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvironmentVariablesUnitTests {

    @Test
    @DisplayName("CORS")
    public void testCORS() {
        String var = System.getenv("CORS");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("JWT_SECRET_KEY")
    public void testJwtSecretKey() {
        String var = System.getenv("JWT_SECRET_KEY");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
        assertThat(Decoders.BASE64.decode(var).length).isEqualTo(512);
    }

    @Test
    @DisplayName("DB_HOST")
    public void testDbHost() {
        String var = System.getenv("DB_HOST");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("DB_PORT")
    public void testDbPort() {
        String var = System.getenv("DB_PORT");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("DB_NAME")
    public void testDbName() {
        String var = System.getenv("DB_NAME");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("DB_USERNAME")
    public void testDbUsername() {
        String var = System.getenv("DB_USERNAME");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("DB_PASSWORD")
    public void testDbPassword() {
        String var = System.getenv("DB_PASSWORD");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_HOST")
    public void testTestDbHost() {
        String var = System.getenv("TEST_DB_HOST");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_PORT")
    public void testTestDbPort() {
        String var = System.getenv("TEST_DB_PORT");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_NAME")
    public void testTestDbName() {
        String var = System.getenv("TEST_DB_NAME");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_USERNAME")
    public void testTestDbUsername() {
        String var = System.getenv("TEST_DB_USERNAME");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_PASSWORD")
    public void testTestDbPassword() {
        String var = System.getenv("TEST_DB_PASSWORD");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }
}
