package net.seancoyne.fiveinarowclient.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserInteractionTest {

    private BufferedReader bufferedReader;
    private UserInteraction testInstance;

    @BeforeEach
    public void setup() {
        bufferedReader = Mockito.mock(BufferedReader.class);
        testInstance = new UserInteraction(bufferedReader);
    }

    @Test
    public void test_getPlayerStringInputWithMessage() throws IOException {
        // Given
        String message = "message";
        String response = "response";
        when(bufferedReader.readLine()).thenReturn(response);

        // When
        String result = testInstance.getPlayerStringInputWithMessage(message);

        // Then
        assertEquals(response, result);
    }

    @Test
    public void test_getPlayerIntegerInputWithMessage() throws IOException {
        // Given
        String message = "message";
        Integer response = 1;
        when(bufferedReader.readLine()).thenReturn(String.valueOf(response));

        // When
        Integer result = testInstance.getPlayerIntegerInputWithMessage(message);

        // Then
        assertEquals(response, result);
    }

    @Test
    public void test_getPlayerBooleanInputWithMessage() throws IOException {
        // Given
        String message = "message";
        Boolean response = true;
        when(bufferedReader.readLine()).thenReturn(String.valueOf(response));

        // When
        boolean result = testInstance.getPlayerBooleanInputWithMessage(message);

        // Then
        assertEquals(response, result);
    }

}