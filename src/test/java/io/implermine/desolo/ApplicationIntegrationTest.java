package io.implermine.desolo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Spring Boot + TestContainers integration test
 */
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class ApplicationIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("desolo_test")
            .withUsername("test_user")
            .withPassword("test_password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private DataSource dataSource;

    @Test
    void should_IntegrateSpringBootApplication_With_TestContainers() throws Exception {
        // Given
        assertThat(postgres.isRunning()).isTrue();
        
        // When
        var connection = dataSource.getConnection();
        var productName = connection.getMetaData().getDatabaseProductName();
        
        // Then
        assertThat(productName).isEqualTo("PostgreSQL");
        assertThat(connection.isValid(1000)).isTrue();
        
        connection.close();
        System.out.println("Spring Boot + TestContainers integration successful!");
    }
} 