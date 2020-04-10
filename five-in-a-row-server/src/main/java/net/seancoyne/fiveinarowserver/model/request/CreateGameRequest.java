package net.seancoyne.fiveinarowserver.model.request;

import lombok.Data;

@Data
public class CreateGameRequest {
    private String username;
    private String selectedColour;
}
