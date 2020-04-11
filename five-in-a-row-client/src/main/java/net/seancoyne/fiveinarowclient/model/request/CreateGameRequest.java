package net.seancoyne.fiveinarowclient.model.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateGameRequest {
    private String username;
    private String selectedColour;
}
