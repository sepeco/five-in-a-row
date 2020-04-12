package net.seancoyne.fiveinarowclient.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientConfigTest {

    private ClientConfig testInstance;

    @BeforeAll
    void setup() {
        testInstance = new ClientConfig();
    }

    @Test
    public void test_restTemplate() {
        assertTrue(testInstance.restTemplate() instanceof RestTemplate);
    }

    @Test
    public void test_httpHeaders() {
        assertTrue(testInstance.httpHeaders() instanceof HttpHeaders);
    }

    @Test
    public void test_objectMapper() {
        assertTrue(testInstance.objectMapper() instanceof ObjectMapper);
    }
}