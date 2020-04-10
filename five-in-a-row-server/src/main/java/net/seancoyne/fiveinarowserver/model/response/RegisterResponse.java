package net.seancoyne.fiveinarowserver.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisterResponse {
    private ResponseState responseState;
    private String message;
}
