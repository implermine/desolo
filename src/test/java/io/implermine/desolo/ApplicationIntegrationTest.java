package io.implermine.desolo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Spring Boot + TestContainers integration test
 */
@SpringBootTest
@ActiveProfiles("test")
class ApplicationIntegrationTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void should_IntegrateSpringBootApplication_With_TestContainers() throws Exception {
        var connection = dataSource.getConnection();
        var productName = connection.getMetaData().getDatabaseProductName();

        // Then
        assertThat(productName).isEqualTo("PostgreSQL");
        assertThat(connection.isValid(1000)).isTrue();

        connection.close();
        System.out.println("Spring Boot + TestContainers YML integration successful!");
    }
} 