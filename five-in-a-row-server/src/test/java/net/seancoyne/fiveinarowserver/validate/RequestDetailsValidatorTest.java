package net.seancoyne.fiveinarowserver.validate;

import net.seancoyne.fiveinarowserver.model.request.CreateGameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestDetailsValidatorTest {

    private RequestDetailsValidator testInstance;

    @BeforeEach
    public void setup() {
        testInstance = new RequestDetailsValidator();
    }

    @Test
    void requiredParametersIsInvalid_valid() {
        // When
        boolean result = testInstance.requiredParametersIsInvalid("StringTest", Boolean.TRUE, new CreateGameRequest());

        // Then
        assertFalse(result);
    }

    @Test
    void requiredParametersIsInvalid_invalid() {
        // When
        boolean result = testInstance.requiredParametersIsInvalid("StringTest", null, new CreateGameRequest());

        // Then
        assertTrue(result);
    }

    @Test
    void requiredParametersIsInvalid_invalid_blank_string() {
        // When
        boolean result = testInstance.requiredParametersIsInvalid("");

        // Then
        assertTrue(result);
    }

    @Test
    void requiredParametersIsInvalid_invalid_white_space() {
        // When
        boolean result = testInstance.requiredParametersIsInvalid(" ");

        // Then
        assertTrue(result);
    }
}