package io.implermine.desolo;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Basic PostgreSQL TestContainer functionality verification
 */
@Testcontainers
class PostgreSQLContainerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @Test
    void should_StartPostgreSQLContainer_And_EstablishConnection() throws Exception {
        // Given
        assertThat(postgres.isRunning()).isTrue();
        
        // When
        String jdbcUrl = postgres.getJdbcUrl();
        
        // Then
        try (Connection connection = DriverManager.getConnection(jdbcUrl, postgres.getUsername(), postgres.getPassword())) {
            assertThat(connection.isValid(5)).isTrue();
            
            var resultSet = connection.createStatement().executeQuery("SELECT version()");
            if (resultSet.next()) {
                String version = resultSet.getString(1);
                assertThat(version).contains("PostgreSQL");
                System.out.println("TestContainers verification completed successfully!");
            }
        }
    }
} 