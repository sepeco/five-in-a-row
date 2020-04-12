package net.seancoyne.fiveinarowclient.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserInteractionConfigTest {

    private UserInteractionConfig testInstance;

    @BeforeEach
    void setup() {
        testInstance = new UserInteractionConfig();
    }

    @Test
    void test_bufferedReader() {
        assertTrue(testInstance.bufferedReader() instanceof BufferedReader);
    }
}