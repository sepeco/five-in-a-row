package net.seancoyne.fiveinarowclient.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import net.seancoyne.fiveinarowclient.model.Player;
import net.seancoyne.fiveinarowclient.model.request.CreateGameRequest;
import net.seancoyne.fiveinarowclient.model.request.MoveRequest;
import net.seancoyne.fiveinarowclient.model.request.RegisterRequest;
import net.seancoyne.fiveinarowclient.model.response.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Component
public class FiveInARowClient {

    private final String serverHost = "http://localhost:8080";

    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private ObjectMapper objectMapper;

    public FiveInARowClient() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.objectMapper = new ObjectMapper();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
    }

    public CreateGameResponse createGame(Player player) {
        String endpoint = "/createGame";

        CreateGameRequest request = CreateGameRequest.builder()
                .username(player.getUsername())
                .selectedColour(player.getColourChoice())
                .build();

        try {
            HttpEntity<String> requestEntity = new HttpEntity<String>(objectMapper.writeValueAsString(request), headers);
            ResponseEntity<CreateGameResponse> responseEntity = restTemplate.exchange(
                    serverHost + endpoint,
                    HttpMethod.POST,
                    requestEntity,
                    CreateGameResponse.class
            );
            return responseEntity.getBody();
        } catch (JsonProcessingException e) {
            log.error("Error processing request", e);
            return null;
        }
    }

    public RegisterResponse registerPlayer(Player player) {
        String endpoint = "/register";

        RegisterRequest request = RegisterRequest.builder()
                .userName(player.getUsername())
                .gameId(player.getGameId())
                .colour(player.getColourChoice())
                .build();

        try {
            HttpEntity<String> requestEntity = new HttpEntity<String>(objectMapper.writeValueAsString(request), headers);
            ResponseEntity<RegisterResponse> responseEntity = restTemplate.exchange(
                    serverHost + endpoint,
                    HttpMethod.POST,
                    requestEntity,
                    RegisterResponse.class
            );
            return responseEntity.getBody();
        } catch (JsonProcessingException e) {
            log.error("Error processing request", e);
            return null;
        }
    }

    public GameStateResponse getGameState(Player player) {
        String endpoint = "/gameState/";
        String resource = "/user/";

        try {
            HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
            ResponseEntity<GameStateResponse> responseEntity = restTemplate.exchange(
                    serverHost + endpoint + player.getGameId() + resource + player.getUsername(),
                    HttpMethod.GET,
                    requestEntity,
                    GameStateResponse.class
            );
            return responseEntity.getBody();
        } catch (RestClientException e) {
            log.error("Error processing request", e);
            return null;
        }
    }

    public MoveResponse makeMove(Player player, int move) {
        String endpoint = "/makeMove";

        MoveRequest request = MoveRequest.builder()
                .column(move)
                .gameId(player.getGameId())
                .username(player.getUsername())
                .build();
        try {
            HttpEntity<String> requestEntity = new HttpEntity<String>(objectMapper.writeValueAsString(request), headers);
            ResponseEntity<MoveResponse> responseEntity = restTemplate.exchange(
                    serverHost + endpoint,
                    HttpMethod.PUT,
                    requestEntity,
                    MoveResponse.class
            );
            return responseEntity.getBody();
        } catch (JsonProcessingException e) {
            log.error("Error processing request", e);
            return null;
        }
    }

    public DisconnectResponse disconnect(Player player) {
        String endpoint = "/gameState/";
        String resource = "/user/";
        try {
            HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
            ResponseEntity<DisconnectResponse> responseEntity = restTemplate.exchange(
                    serverHost + endpoint + player.getGameId() + resource + player.getUsername(),
                    HttpMethod.DELETE,
                    requestEntity,
                    DisconnectResponse.class
            );
            return responseEntity.getBody();
        } catch (RestClientException e) {
            log.error("Error processing request", e);
            return null;
        }
    }
}
