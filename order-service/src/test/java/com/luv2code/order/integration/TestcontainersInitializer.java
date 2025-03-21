package com.luv2code.order.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class TestcontainersInitializer {

    @Autowired
    protected WebTestClient webTestClient;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:latest"))
            .withReuse(true);

    static RabbitMQContainer rabbitMQ = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13.7-management-alpine"))
            .withExposedPorts(5672, 15672)
            .withCopyFileToContainer(MountableFile.forClasspathResource("rabbitmq/definitions.json"),
                    "/etc/rabbitmq/definitions.json")
            .withEnv("RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS", "-rabbitmq_management load_definitions \"/etc/rabbitmq/definitions.json\"")
            .waitingFor(Wait.forListeningPort())
            .withReuse(true);

    static {
        try {
            Startables.deepStart(postgres, rabbitMQ).join();
        } catch (Exception e) {
            throw new RuntimeException("Failed to start Testcontainers", e);
        }
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.rabbitmq.host", rabbitMQ::getHost);
        registry.add("spring.rabbitmq.port", () -> rabbitMQ.getMappedPort(5672));
        registry.add("spring.rabbitmq.username", rabbitMQ::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMQ::getAdminPassword);
    }
}
