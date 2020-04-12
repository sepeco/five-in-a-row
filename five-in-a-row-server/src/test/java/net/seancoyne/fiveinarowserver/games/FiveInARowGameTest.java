package net.seancoyne.fiveinarowserver.games;

import net.seancoyne.fiveinarowserver.model.request.DisconnectUserRequest;
import net.seancoyne.fiveinarowserver.model.request.GameStateRequest;
import net.seancoyne.fiveinarowserver.model.request.MoveRequest;
import net.seancoyne.fiveinarowserver.model.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FiveInARowGameTest {

    FiveInARowGame testInstance;

    @BeforeEach
    public void setup() {
        testInstance = new FiveInARowGame();
    }

    @Test
    public void test_constructor_initialised_game_correctly() {
        int height = 6;
        int width = 9;

        assertEquals(height, testInstance.getBoard().length);
        assertEquals(width, testInstance.getBoard()[0].length);
        assertTrue(testInstance.isActiveGame());
        assertNotNull(testInstance.getGameId());
        assertNotNull(testInstance.getRegisteredPlayers());
    }

    @Test
    public void test_registerPlayer_success() {
        // Given
        String username = "username";
        String colourSelection = "X";

        // When
        RegisterResponse registerResponse = testInstance.registerPlayer(username, colourSelection);

        // Then
        assertNotNull(registerResponse);

        assertEquals(ResponseState.SUCCESS, registerResponse.getResponseState());
        assertEquals("User registered for game", registerResponse.getMessage());
        assertEquals(username, testInstance.getLastMoveByPlayer());
        assertTrue(testInstance.getRegisteredPlayers().containsKey(username));
        assertTrue(testInstance.getRegisteredPlayers().containsValue(colourSelection));
    }

    @Test
    public void test_registerPlayer_failed_game_full() {
        // Given
        String userOne = "usernameOne";
        String colourOne = "X";
        String userTwo = "usernameTwo";
        String colourTwo = "O";
        String userThree = "usernameThree";
        String colourThree = "Y";

        testInstance.registerPlayer(userOne, colourOne);
        testInstance.registerPlayer(userTwo, colourTwo);

        // When
        RegisterResponse registerPlayerTwo = testInstance.registerPlayer(userThree, colourThree);

        // Then
        assertNotNull(registerPlayerTwo);

        assertEquals(ResponseState.FAILED, registerPlayerTwo.getResponseState());
        assertEquals("Selected game is already full. Try creating a new game", registerPlayerTwo.getMessage());
    }

    @Test
    public void test_registerPlayer_failed_colour_already_taken() {
        // Given
        String userOne = "usernameOne";
        String colourOne = "X";
        String userTwo = "usernameTwo";
        String colourTwo = "X";

        testInstance.registerPlayer(userOne, colourOne);

        // When
        RegisterResponse registerPlayerTwo = testInstance.registerPlayer(userTwo, colourTwo);

        // Then
        assertNotNull(registerPlayerTwo);

        assertEquals(ResponseState.FAILED, registerPlayerTwo.getResponseState());
        assertEquals("Selected colour is not available for this game", registerPlayerTwo.getMessage());
    }

    @Test
    public void test_registerPlayer_failed_user_already_registered() {
        // Given
        String userOne = "username";
        String colourOne = "X";
        String userTwo = "username";
        String colourTwo = "O";

        testInstance.registerPlayer(userOne, colourOne);

        // When
        RegisterResponse registerPlayerTwo = testInstance.registerPlayer(userTwo, colourTwo);

        // Then
        assertNotNull(registerPlayerTwo);

        assertEquals(ResponseState.FAILED, registerPlayerTwo.getResponseState());
        assertEquals("Username already registered for this game", registerPlayerTwo.getMessage());
    }

    @Test
    public void test_registerPlayer_failed_game_over() {
        // Given
        String username = "username";
        String colourSelection = "X";

        testInstance.setActiveGame(false);

        // When
        RegisterResponse registerResponse = testInstance.registerPlayer(username, colourSelection);

        // Then
        assertNotNull(registerResponse);

        assertEquals(ResponseState.FAILED, registerResponse.getResponseState());
        assertEquals("This game is over. Try creating a new game", registerResponse.getMessage());
    }

    @Test
    public void test_getGameStateForUser_success() {
        // Given
        String userOne = "usernameOne";
        String colourOne = "X";
        String userTwo = "usernameTwo";
        String colourTwo = "O";

        testInstance.registerPlayer(userOne, colourOne);
        testInstance.registerPlayer(userTwo, colourTwo);

        GameStateRequest gameStateRequest = GameStateRequest.builder()
                .username(userOne)
                .gameId(testInstance.getGameId())
                .build();

        // When
        GameStateResponse gameStateResponse = testInstance.getGameStateForUser(gameStateRequest);

        // Then
        assertEquals(ResponseState.SUCCESS, gameStateResponse.getResponseState());
        assertEquals(GameState.YOUR_MOVE, gameStateResponse.getGameState());
        assertEquals("Make your move", gameStateResponse.getMessage());
        assertNotNull(gameStateResponse.getCurrentBoard());
        assertFalse(gameStateResponse.isWinner());
    }

    @Test
    public void test_getGameStateForUser_success_other_players_move() {
        // Given
        String userOne = "usernameOne";
        String colourOne = "X";
        String userTwo = "usernameTwo";
        String colourTwo = "O";

        testInstance.registerPlayer(userOne, colourOne);
        testInstance.registerPlayer(userTwo, colourTwo);

        GameStateRequest gameStateRequest = GameStateRequest.builder()
                .username(userTwo)
                .gameId(testInstance.getGameId())
                .build();

        // When
        GameStateResponse gameStateResponse = testInstance.getGameStateForUser(gameStateRequest);

        // Then
        assertEquals(ResponseState.SUCCESS, gameStateResponse.getResponseState());
        assertEquals(GameState.WAITING_FOR_NEXT_PLAYER_TO_MAKE_MOVE, gameStateResponse.getGameState());
        assertEquals("Waiting for other player to make move", gameStateResponse.getMessage());
        assertNotNull(gameStateResponse.getCurrentBoard());
        assertFalse(gameStateResponse.isWinner());
    }

    @Test
    public void test_getGameStateForUser_success_winner() {
        // Given
        String userOne = "usernameOne";
        String colourOne = "X";
        String userTwo = "usernameTwo";
        String colourTwo = "O";

        testInstance.registerPlayer(userOne, colourOne);
        testInstance.registerPlayer(userTwo, colourTwo);

        testInstance.setWinningPlayer(userOne);

        GameStateRequest gameStateRequestUserOne = GameStateRequest.builder()
                .username(userOne)
                .gameId(testInstance.getGameId())
                .build();

        GameStateRequest gameStateRequestUserTwo = GameStateRequest.builder()
                .username(userTwo)
                .gameId(testInstance.getGameId())
                .build();

        // When
        GameStateResponse gameStateResponseUserOne = testInstance.getGameStateForUser(gameStateRequestUserOne);
        GameStateResponse gameStateResponseUserTwo = testInstance.getGameStateForUser(gameStateRequestUserTwo);

        // Then
        assertEquals(ResponseState.SUCCESS, gameStateResponseUserOne.getResponseState());
        assertEquals(GameState.GAME_OVER, gameStateResponseUserOne.getGameState());
        assertEquals("Game Over, usernameOne is the winner", gameStateResponseUserOne.getMessage());
        assertNull(gameStateResponseUserOne.getCurrentBoard());
        assertTrue(gameStateResponseUserOne.isWinner());

        assertEquals(ResponseState.SUCCESS, gameStateResponseUserTwo.getResponseState());
        assertEquals(GameState.GAME_OVER, gameStateResponseUserTwo.getGameState());
        assertEquals("Game Over, usernameOne is the winner", gameStateResponseUserTwo.getMessage());
        assertNull(gameStateResponseUserTwo.getCurrentBoard());
        assertFalse(gameStateResponseUserTwo.isWinner());
    }

    @Test
    public void test_getGameStateForUser_success_game_not_active() {
        // Given
        String userOne = "usernameOne";
        String colourOne = "X";

        testInstance.registerPlayer(userOne, colourOne);
        testInstance.setActiveGame(false);

        GameStateRequest gameStateRequestUserOne = GameStateRequest.builder()
                .username(userOne)
                .gameId(testInstance.getGameId())
                .build();

        // When
        GameStateResponse gameStateResponseUserOne = testInstance.getGameStateForUser(gameStateRequestUserOne);

        // Then
        assertEquals(ResponseState.SUCCESS, gameStateResponseUserOne.getResponseState());
        assertEquals(GameState.GAME_OVER, gameStateResponseUserOne.getGameState());
        assertEquals("This game is over. Try creating a new game", gameStateResponseUserOne.getMessage());
        assertNull(gameStateResponseUserOne.getCurrentBoard());
        assertFalse(gameStateResponseUserOne.isWinner());
    }

    @Test
    public void test_getGameStateForUser_success_waiting_for_next_player() {
        // Given
        String userOne = "usernameOne";
        String colourOne = "X";

        testInstance.registerPlayer(userOne, colourOne);

        GameStateRequest gameStateRequestUserOne = GameStateRequest.builder()
                .username(userOne)
                .gameId(testInstance.getGameId())
                .build();

        // When
        GameStateResponse gameStateResponseUserOne = testInstance.getGameStateForUser(gameStateRequestUserOne);

        // Then
        assertEquals(ResponseState.SUCCESS, gameStateResponseUserOne.getResponseState());
        assertEquals(GameState.WAITING_FOR_NEXT_PLAYER_TO_REGISTER, gameStateResponseUserOne.getGameState());
        assertEquals("Waiting for the next player", gameStateResponseUserOne.getMessage());
        assertNull(gameStateResponseUserOne.getCurrentBoard());
        assertFalse(gameStateResponseUserOne.isWinner());
    }

    @Test
    public void test_getGameStateForUser_failed_unknown_user() {
        // Given
        GameStateRequest gameStateRequest = GameStateRequest.builder()
                .username("userOne")
                .gameId(testInstance.getGameId())
                .build();

        // When
        GameStateResponse gameStateResponse = testInstance.getGameStateForUser(gameStateRequest);

        // Then
        assertEquals(ResponseState.FAILED, gameStateResponse.getResponseState());
        assertNull(gameStateResponse.getGameState());
        assertEquals("Username is not registered", gameStateResponse.getMessage());
        assertNull(gameStateResponse.getCurrentBoard());
        assertFalse(gameStateResponse.isWinner());
    }

    @Test
    public void test_makeMove_success() {
        // Given
        String userOne = "userOne";
        String userOneColour = "X";
        String userTwo = "userTwo";
        String userTwoColour = "O";

        int moveColumn = 4;

        testInstance.registerPlayer(userOne, userOneColour);
        testInstance.registerPlayer(userTwo, userTwoColour);

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setUsername(userOne);
        moveRequest.setGameId(testInstance.getGameId());
        moveRequest.setColumn(moveColumn);

        // When
        MoveResponse moveResponse = testInstance.makeMove(moveRequest);

        assertEquals(ResponseState.SUCCESS, moveResponse.getResponseState());
        assertEquals("Move made", moveResponse.getMessage());
        assertFalse(moveResponse.isTryAgain());

        assertEquals(userOneColour, testInstance.getBoard()[testInstance.getBoard().length - 1][moveColumn - 1]);
    }

    @Test
    public void test_makeMove_failed_column_full() {
        // Given
        String userOne = "userOne";
        String userOneColour = "X";
        String userTwo = "userTwo";
        String userTwoColour = "O";

        int moveColumn = 4;

        testInstance.registerPlayer(userOne, userOneColour);
        testInstance.registerPlayer(userTwo, userTwoColour);

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setUsername(userOne);
        moveRequest.setGameId(testInstance.getGameId());
        moveRequest.setColumn(moveColumn);

        String[][] newBoard = new String[6][9];
        for (int i = 0; i < newBoard.length; i++) {
            newBoard[i][moveColumn - 1] = userOneColour;
        }
        testInstance.setBoard(newBoard);

        // When
        MoveResponse moveResponse = testInstance.makeMove(moveRequest);

        assertEquals(ResponseState.FAILED, moveResponse.getResponseState());
        assertEquals("The selected column is full", moveResponse.getMessage());
        assertTrue(moveResponse.isTryAgain());

        assertEquals(userOneColour, testInstance.getBoard()[testInstance.getBoard().length - 1][moveColumn - 1]);
    }

    @Test
    public void test_makeMove_success_winning_move_right() {
        // Given
        String userOne = "userOne";
        String userOneColour = "X";
        String userTwo = "userTwo";
        String userTwoColour = "O";

        int moveColumn = 5;

        testInstance.registerPlayer(userOne, userOneColour);
        testInstance.registerPlayer(userTwo, userTwoColour);

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setUsername(userOne);
        moveRequest.setGameId(testInstance.getGameId());
        moveRequest.setColumn(moveColumn);

        String[][] newBoard = new String[6][9];
        for (int i = 0; i < 4; i++) {
            newBoard[5][i] = userOneColour;
        }
        testInstance.setBoard(newBoard);

        // When
        MoveResponse moveResponse = testInstance.makeMove(moveRequest);

        assertEquals(ResponseState.SUCCESS, moveResponse.getResponseState());
        assertEquals("Move made", moveResponse.getMessage());
        assertFalse(moveResponse.isTryAgain());

        assertEquals(userOneColour, testInstance.getBoard()[testInstance.getBoard().length - 1][moveColumn - 1]);
        assertEquals(userOne, testInstance.getWinningPlayer());
    }

    @Test
    public void test_makeMove_success_winning_move_left() {
        // Given
        String userOne = "userOne";
        String userOneColour = "X";
        String userTwo = "userTwo";
        String userTwoColour = "O";

        int moveColumn = 2;

        testInstance.registerPlayer(userOne, userOneColour);
        testInstance.registerPlayer(userTwo, userTwoColour);

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setUsername(userOne);
        moveRequest.setGameId(testInstance.getGameId());
        moveRequest.setColumn(moveColumn);

        String[][] newBoard = new String[6][9];
        for (int i = 5; i > 1; i--) {
            newBoard[5][i] = userOneColour;
        }
        testInstance.setBoard(newBoard);

        // When
        MoveResponse moveResponse = testInstance.makeMove(moveRequest);

        assertEquals(ResponseState.SUCCESS, moveResponse.getResponseState());
        assertEquals("Move made", moveResponse.getMessage());
        assertFalse(moveResponse.isTryAgain());

        assertEquals(userOneColour, testInstance.getBoard()[testInstance.getBoard().length - 1][moveColumn - 1]);
        assertEquals(userOne, testInstance.getWinningPlayer());
    }

    @Test
    public void test_makeMove_success_winning_move_up() {
        // Given
        String userOne = "userOne";
        String userOneColour = "X";
        String userTwo = "userTwo";
        String userTwoColour = "O";

        int moveColumn = 1;

        testInstance.registerPlayer(userOne, userOneColour);
        testInstance.registerPlayer(userTwo, userTwoColour);

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setUsername(userOne);
        moveRequest.setGameId(testInstance.getGameId());
        moveRequest.setColumn(moveColumn);

        String[][] newBoard = new String[6][9];
        for (int i = 5; i > 1; i--) {
            newBoard[i][0] = userOneColour;
        }
        testInstance.setBoard(newBoard);

        // When
        MoveResponse moveResponse = testInstance.makeMove(moveRequest);

        assertEquals(ResponseState.SUCCESS, moveResponse.getResponseState());
        assertEquals("Move made", moveResponse.getMessage());
        assertFalse(moveResponse.isTryAgain());

        assertEquals(userOneColour, testInstance.getBoard()[testInstance.getBoard().length - 1][moveColumn - 1]);
        assertEquals(userOne, testInstance.getWinningPlayer());
    }

    @Test
    public void test_makeMove_success_winning_move_up_and_right() {
        // Given
        String userOne = "userOne";
        String userOneColour = "X";
        String userTwo = "userTwo";
        String userTwoColour = "O";

        int moveColumn = 6;

        testInstance.registerPlayer(userOne, userOneColour);
        testInstance.registerPlayer(userTwo, userTwoColour);

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setUsername(userOne);
        moveRequest.setGameId(testInstance.getGameId());
        moveRequest.setColumn(moveColumn);

        String[][] newBoard = new String[6][9];
        for (int i = 4; i > 0; i--) {
            newBoard[i][i] = userOneColour;
        }
        testInstance.setBoard(newBoard);

        // When
        MoveResponse moveResponse = testInstance.makeMove(moveRequest);

        assertEquals(ResponseState.SUCCESS, moveResponse.getResponseState());
        assertEquals("Move made", moveResponse.getMessage());
        assertFalse(moveResponse.isTryAgain());

        assertEquals(userOneColour, testInstance.getBoard()[testInstance.getBoard().length - 1][moveColumn - 1]);
        assertEquals(userOne, testInstance.getWinningPlayer());
    }

    @Test
    public void test_makeMove_success_winning_move_up_and_left() {
        // Given
        String userOne = "userOne";
        String userOneColour = "X";
        String userTwo = "userTwo";
        String userTwoColour = "O";

        int moveColumn = 1;

        testInstance.registerPlayer(userOne, userOneColour);
        testInstance.registerPlayer(userTwo, userTwoColour);

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setUsername(userOne);
        moveRequest.setGameId(testInstance.getGameId());
        moveRequest.setColumn(moveColumn);

        String[][] newBoard = new String[6][9];

        int j = 1;
        for (int i = 4; i > 0; i--) {
            newBoard[i][j] = userOneColour;
            j += 1;
        }
        testInstance.setBoard(newBoard);

        // When
        MoveResponse moveResponse = testInstance.makeMove(moveRequest);

        assertEquals(ResponseState.SUCCESS, moveResponse.getResponseState());
        assertEquals("Move made", moveResponse.getMessage());
        assertFalse(moveResponse.isTryAgain());

        assertEquals(userOneColour, testInstance.getBoard()[testInstance.getBoard().length - 1][moveColumn - 1]);
        assertEquals(userOne, testInstance.getWinningPlayer());
    }

    @Test
    public void test_makeMove_failed_move_out_of_bounds() {
        // Given
        String userOne = "userOne";
        String userOneColour = "X";
        String userTwo = "userTwo";
        String userTwoColour = "O";

        int moveColumn = 40;

        testInstance.registerPlayer(userOne, userOneColour);
        testInstance.registerPlayer(userTwo, userTwoColour);

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setUsername(userOne);
        moveRequest.setGameId(testInstance.getGameId());
        moveRequest.setColumn(moveColumn);

        // When
        MoveResponse moveResponse = testInstance.makeMove(moveRequest);

        assertEquals(ResponseState.FAILED, moveResponse.getResponseState());
        assertEquals("The selected column is out of bounds of the board", moveResponse.getMessage());
        assertTrue(moveResponse.isTryAgain());
    }

    @Test
    public void test_makeMove_failed_cannot_move_twice() {
        // Given
        String userOne = "userOne";
        String userOneColour = "X";
        String userTwo = "userTwo";
        String userTwoColour = "O";

        int moveColumn = 4;

        testInstance.registerPlayer(userOne, userOneColour);
        testInstance.registerPlayer(userTwo, userTwoColour);

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setUsername(userTwo);
        moveRequest.setGameId(testInstance.getGameId());
        moveRequest.setColumn(moveColumn);

        // When
        MoveResponse moveResponse = testInstance.makeMove(moveRequest);

        assertEquals(ResponseState.FAILED, moveResponse.getResponseState());
        assertEquals("Its not your move yet", moveResponse.getMessage());
        assertFalse(moveResponse.isTryAgain());
    }

    @Test
    public void test_makeMove_failed_not_active_game() {
        // Given
        String userOne = "userOne";
        String userOneColour = "X";
        int moveColumn = 4;

        testInstance.registerPlayer(userOne, userOneColour);

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setUsername(userOne);
        moveRequest.setGameId(testInstance.getGameId());
        moveRequest.setColumn(moveColumn);

        testInstance.setActiveGame(false);

        // When
        MoveResponse moveResponse = testInstance.makeMove(moveRequest);

        assertEquals(ResponseState.FAILED, moveResponse.getResponseState());
        assertEquals("This game is over. Try creating a new game", moveResponse.getMessage());
        assertFalse(moveResponse.isTryAgain());
    }

    @Test
    public void test_makeMove_failed_unknown_user() {
        // Given
        int moveColumn = 4;

        MoveRequest moveRequest = new MoveRequest();
        moveRequest.setUsername("userOne");
        moveRequest.setGameId(testInstance.getGameId());
        moveRequest.setColumn(moveColumn);

        // When
        MoveResponse moveResponse = testInstance.makeMove(moveRequest);

        assertEquals(ResponseState.FAILED, moveResponse.getResponseState());
        assertEquals("Username is not registered", moveResponse.getMessage());
        assertFalse(moveResponse.isTryAgain());
    }

    @Test
    public void test_disconnectUser_success() {
        // Given
        String userOne = "userOne";
        String userOneColour = "X";
        String userTwo = "userTwo";
        String userTwoColour = "O";

        testInstance.registerPlayer(userOne, userOneColour);
        testInstance.registerPlayer(userTwo, userTwoColour);

        DisconnectUserRequest disconnectUserRequest = DisconnectUserRequest.builder()
                .username(userOne)
                .gameId(testInstance.getGameId())
                .build();

        // When
        DisconnectResponse response = testInstance.disconnectUser(disconnectUserRequest);

        // Then
        assertNotNull(response);

        assertEquals(ResponseState.SUCCESS, response.getResponseState());
        assertEquals("User disconnected, game over", response.getMessage());

        assertFalse(testInstance.isActiveGame());
    }

    @Test
    public void test_disconnectUser_failed_game_not_active() {
        // Given
        String userOne = "userOne";
        String userOneColour = "X";
        String userTwo = "userTwo";
        String userTwoColour = "O";

        testInstance.registerPlayer(userOne, userOneColour);
        testInstance.registerPlayer(userTwo, userTwoColour);

        DisconnectUserRequest disconnectUserRequest = DisconnectUserRequest.builder()
                .username(userOne)
                .gameId(testInstance.getGameId())
                .build();

        testInstance.setActiveGame(false);

        // When
        DisconnectResponse response = testInstance.disconnectUser(disconnectUserRequest);

        // Then
        assertNotNull(response);

        assertEquals(ResponseState.FAILED, response.getResponseState());
        assertEquals("This game is over. Try creating a new game", response.getMessage());

        assertFalse(testInstance.isActiveGame());
    }

    @Test
    public void test_disconnectUser_failed_user_not_found() {
        // Given

        DisconnectUserRequest disconnectUserRequest = DisconnectUserRequest.builder()
                .username("userOne")
                .gameId(testInstance.getGameId())
                .build();

        // When
        DisconnectResponse response = testInstance.disconnectUser(disconnectUserRequest);

        // Then
        assertNotNull(response);

        assertEquals(ResponseState.FAILED, response.getResponseState());
        assertEquals("Username is not registered", response.getMessage());

        assertTrue(testInstance.isActiveGame());
    }
}