package lu.letzmarketplace.restapi.configurations;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EnvironmentVariablesUnitTests {
    @Autowired
    private Environment env;

    @Test
    @DisplayName("CORS")
    public void testCORS() {
        String var = env.getProperty("CORS");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("JWT_SECRET_KEY")
    public void testJwtSecretKey() {
        String var = env.getProperty("JWT_SECRET_KEY");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
        assertThat(Decoders.BASE64.decode(var).length).isEqualTo(512);
    }

    @Test
    @DisplayName("DB_HOST")
    public void testDbHost() {
        String var = env.getProperty("DB_HOST");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("DB_PORT")
    public void testDbPort() {
        String var = env.getProperty("DB_PORT");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("DB_NAME")
    public void testDbName() {
        String var = env.getProperty("DB_NAME");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("DB_USERNAME")
    public void testDbUsername() {
        String var = env.getProperty("DB_USERNAME");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("DB_PASSWORD")
    public void testDbPassword() {
        String var = env.getProperty("DB_PASSWORD");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_HOST")
    public void testTestDbHost() {
        String var = env.getProperty("TEST_DB_HOST");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_PORT")
    public void testTestDbPort() {
        String var = env.getProperty("TEST_DB_PORT");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_NAME")
    public void testTestDbName() {
        String var = env.getProperty("TEST_DB_NAME");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_USERNAME")
    public void testTestDbUsername() {
        String var = env.getProperty("TEST_DB_USERNAME");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_PASSWORD")
    public void testTestDbPassword() {
        String var = env.getProperty("TEST_DB_PASSWORD");

        assertThat(var).isNotNull();
        assertThat(var).isNotEmpty();
    }
}
