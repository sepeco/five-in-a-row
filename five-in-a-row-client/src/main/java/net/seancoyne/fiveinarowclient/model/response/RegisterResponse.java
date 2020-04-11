package net.seancoyne.fiveinarowclient.model.response;

import lombok.Data;

@Data
public class RegisterResponse {
    private ResponseState responseState;
    private String message;
}
