package net.seancoyne.fiveinarowclient.service;

import net.seancoyne.fiveinarowclient.model.Player;
import net.seancoyne.fiveinarowclient.util.UserInteraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    private UserInteraction userInteraction;
    private GameService gameService;

    private MenuService testInstance;

    @BeforeEach
    void setup() {
        userInteraction = Mockito.mock(UserInteraction.class);
        gameService = Mockito.mock(GameService.class);

        testInstance = new MenuService(userInteraction, gameService);
    }

    @Test
    public void test_collectPlayerUsername_new_game() throws IOException {
        // Given
        when(userInteraction.getPlayerBooleanInputWithMessage("Do you have a Game ID and would like to join an existing game? (true or false)")).thenReturn(false);
        when(userInteraction.getPlayerStringInputWithMessage("Please Provide Your Username:")).thenReturn("Username");
        when(userInteraction.getPlayerStringInputWithMessage("Please Provide Your Colour Choice (X or O):")).thenReturn("X");
        when(gameService.createNewGame(any(Player.class))).thenReturn(true);

        // When
        testInstance.collectSessionInfo();

        // Then
        verify(userInteraction, times(1)).getPlayerBooleanInputWithMessage("Do you have a Game ID and would like to join an existing game? (true or false)");
        verify(userInteraction, times(1)).getPlayerStringInputWithMessage("Please Provide Your Username:");
        verify(userInteraction, times(1)).getPlayerStringInputWithMessage("Please Provide Your Colour Choice (X or O):");
        verify(gameService, times(1)).createNewGame(any(Player.class));
    }

    @Test
    public void test_collectPlayerUsername_new_game_ioExceotion() throws IOException {
        // Given
        when(userInteraction.getPlayerBooleanInputWithMessage("Do you have a Game ID and would like to join an existing game? (true or false)")).thenThrow(new IOException());

        // When
        testInstance.collectSessionInfo();

        // Then
        verify(userInteraction, times(1)).getPlayerBooleanInputWithMessage("Do you have a Game ID and would like to join an existing game? (true or false)");
        verify(userInteraction, times(0)).getPlayerStringInputWithMessage("Please Provide Your Username:");
        verify(userInteraction, times(0)).getPlayerStringInputWithMessage("Please Provide Your Colour Choice (X or O):");
        verify(gameService, times(0)).createNewGame(any(Player.class));
    }

    @Test
    public void test_collectPlayerUsername_join_existing_game() throws IOException {
        // Given
        when(userInteraction.getPlayerBooleanInputWithMessage("Do you have a Game ID and would like to join an existing game? (true or false)")).thenReturn(true);
        when(userInteraction.getPlayerStringInputWithMessage("Please Provide Your Username:")).thenReturn("Username");
        when(userInteraction.getPlayerStringInputWithMessage("Please Provide Your Colour Choice (X or O):")).thenReturn("X");
        when(userInteraction.getPlayerIntegerInputWithMessage("Please Provide The Existing Game ID:")).thenReturn(1);
        when(gameService.joinExistingGame(any(Player.class))).thenReturn(true);

        // When
        testInstance.collectSessionInfo();

        // Then
        verify(userInteraction, times(1)).getPlayerBooleanInputWithMessage("Do you have a Game ID and would like to join an existing game? (true or false)");
        verify(userInteraction, times(1)).getPlayerStringInputWithMessage("Please Provide Your Username:");
        verify(userInteraction, times(1)).getPlayerStringInputWithMessage("Please Provide Your Colour Choice (X or O):");
        verify(userInteraction, times(1)).getPlayerIntegerInputWithMessage("Please Provide The Existing Game ID:");
        verify(gameService, times(1)).joinExistingGame(any(Player.class));
    }

    @Test
    public void test_collectPlayerUsername_join_existing_game_IOException() throws IOException {
        // Given
        when(userInteraction.getPlayerBooleanInputWithMessage("Do you have a Game ID and would like to join an existing game? (true or false)")).thenThrow(new IOException());

        // When
        testInstance.collectSessionInfo();

        // Then
        verify(userInteraction, times(1)).getPlayerBooleanInputWithMessage("Do you have a Game ID and would like to join an existing game? (true or false)");
        verify(userInteraction, times(0)).getPlayerStringInputWithMessage("Please Provide Your Username:");
        verify(userInteraction, times(0)).getPlayerStringInputWithMessage("Please Provide Your Colour Choice (X or O):");
        verify(userInteraction, times(0)).getPlayerIntegerInputWithMessage("Please Provide The Existing Game ID:");
        verify(gameService, times(0)).joinExistingGame(any(Player.class));
    }
}