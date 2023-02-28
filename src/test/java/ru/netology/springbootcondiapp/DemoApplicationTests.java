package ru.netology.springbootcondiapp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    static final int DEVAPPPORT = 8080;
    static final int PRODAPPPORT = 8081;
    static final GenericContainer<?> devapp = new GenericContainer<>("docker.io/library/devapp1")
            .withExposedPorts(DEVAPPPORT);
    static final GenericContainer<?> prodapp = new GenericContainer<>("docker.io/library/prodapp1")
            .withExposedPorts(PRODAPPPORT);
    @Autowired
    TestRestTemplate restTemplate;

    @BeforeAll
    public static void setUp() {
        devapp.start();
        prodapp.start();
    }

    @Test
    void devappProfile() {
        String expectedBody = "Current profile is dev";

        Integer devAppPortTest = devapp.getMappedPort(DEVAPPPORT);
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:" + devAppPortTest + "/profile", String.class);
        System.out.println(forEntity.getBody());

        assertEquals(expectedBody, forEntity.getBody());
    }

    @Test
    void prodappProfile() {
        String expectedBody = "Current profile is production";

        Integer prodAppPortTest = prodapp.getMappedPort(PRODAPPPORT);
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:" + prodAppPortTest + "/profile", String.class);
        System.out.println(forEntity.getBody());

        assertEquals(expectedBody, forEntity.getBody());
    }

}
