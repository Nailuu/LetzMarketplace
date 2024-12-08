package lu.letzmarketplace.restapi.configurations;

import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvironmentVariablesUnitTests {

    @Value("${CORS}")
    private String cors;

    @Value("${JWT_SECRET_KEY}")
    private String jwtSecretKey;

    @Value("${DB_HOST}")
    private String dbHost;

    @Value("${DB_PORT}")
    private String dbPort;

    @Value("${DB_NAME}")
    private String dbName;

    @Value("${DB_USERNAME}")
    private String dbUsername;

    @Value("${DB_PASSWORD}")
    private String dbPassword;

    @Value("${TEST_DB_HOST}")
    private String testDbHost;

    @Value("${TEST_DB_PORT}")
    private String testDbPort;

    @Value("${TEST_DB_NAME}")
    private String testDbName;

    @Value("${TEST_DB_USERNAME}")
    private String testDbUsername;

    @Value("${TEST_DB_PASSWORD}")
    private String testDbPassword;

    @Test
    @DisplayName("CORS")
    public void testCORS() {
        assertThat(cors).isNotNull();
        assertThat(cors).isNotEmpty();
    }

    @Test
    @DisplayName("JWT_SECRET_KEY")
    public void testJwtSecretKey() {
        assertThat(jwtSecretKey).isNotNull();
        assertThat(jwtSecretKey).isNotEmpty();
        assertThat(Decoders.BASE64.decode(jwtSecretKey).length).isEqualTo(512);
    }

    @Test
    @DisplayName("DB_HOST")
    public void testDbHost() {
        assertThat(dbHost).isNotNull();
        assertThat(dbHost).isNotEmpty();
    }

    @Test
    @DisplayName("DB_PORT")
    public void testDbPort() {
        assertThat(dbPort).isNotNull();
        assertThat(dbPort).isNotEmpty();
    }

    @Test
    @DisplayName("DB_NAME")
    public void testDbName() {
        assertThat(dbName).isNotNull();
        assertThat(dbName).isNotEmpty();
    }

    @Test
    @DisplayName("DB_USERNAME")
    public void testDbUsername() {
        assertThat(dbUsername).isNotNull();
        assertThat(dbUsername).isNotEmpty();
    }

    @Test
    @DisplayName("DB_PASSWORD")
    public void testDbPassword() {
        assertThat(dbPassword).isNotNull();
        assertThat(dbPassword).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_HOST")
    public void testTestDbHost() {
        assertThat(testDbHost).isNotNull();
        assertThat(testDbHost).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_PORT")
    public void testTestDbPort() {
        assertThat(testDbPort).isNotNull();
        assertThat(testDbPort).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_NAME")
    public void testTestDbName() {
        assertThat(testDbName).isNotNull();
        assertThat(testDbName).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_USERNAME")
    public void testTestDbUsername() {
        assertThat(testDbUsername).isNotNull();
        assertThat(testDbUsername).isNotEmpty();
    }

    @Test
    @DisplayName("TEST_DB_PASSWORD")
    public void testTestDbPassword() {
        assertThat(testDbPassword).isNotNull();
        assertThat(testDbPassword).isNotEmpty();
    }
}
