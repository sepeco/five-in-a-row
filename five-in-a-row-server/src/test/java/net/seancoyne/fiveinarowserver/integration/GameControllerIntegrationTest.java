package net.seancoyne.fiveinarowserver.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.seancoyne.fiveinarowserver.model.request.CreateGameRequest;
import net.seancoyne.fiveinarowserver.model.request.MoveRequest;
import net.seancoyne.fiveinarowserver.model.request.RegisterRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameControllerIntegrationTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeAll
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        this.mapper = new ObjectMapper();
    }

    @Test
    void createGame() throws Exception {
        String username = "username";
        String colour = "colour";

        CreateGameRequest createGameRequest = new CreateGameRequest();
        createGameRequest.setSelectedColour(colour);
        createGameRequest.setUsername(username);

        mockMvc.perform(post("/createGame")
                .content(mapper.writeValueAsBytes(createGameRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseState").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("New game created! Send the game id to your friend"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameId").isNotEmpty());
    }

    @Test
    void register() throws Exception {
        String username = "username";
        String colour = "colour";
        Integer gameId = 0;

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUserName(username);
        registerRequest.setColour(colour);
        registerRequest.setGameId(gameId);

        mockMvc.perform(post("/register")
                .content(mapper.writeValueAsBytes(registerRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseState").value("FAILED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No game with this ID exists"));
    }

    @Test
    void makeMove() throws Exception {
        Integer column = 4;
        Integer gameId = 0;
        String username = "username";

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setColumn(column);
        moveRequest.setGameId(gameId);
        moveRequest.setUsername(username);

        mockMvc.perform(put("/makeMove")
                .content(mapper.writeValueAsBytes(moveRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseState").value("FAILED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No game with this ID exists"));
    }

    @Test
    void gameState() throws Exception {
        Integer gameId = 0;
        String username = "username";

        mockMvc.perform(get("/gameState/{gameId}/user/{username}", gameId, username))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseState").value("FAILED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No game with this ID exists"));

    }

    @Test
    void disconnectUser() throws Exception {
        Integer gameId = 0;
        String username = "username";

        mockMvc.perform(delete("/disconnect/{gameId}/user/{username}", gameId, username))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseState").value("FAILED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No game with this ID exists"));

    }
}
