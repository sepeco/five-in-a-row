package net.seancoyne.fiveinarowclient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.seancoyne.fiveinarowclient.model.Player;
import net.seancoyne.fiveinarowclient.util.UserInteraction;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
@Component
public class MenuService {

    private final UserInteraction userInteraction;
    private final GameService gameService;

    private Player player;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        collectSessionInfo();
    }

    public void collectSessionInfo() {
        player = new Player();
        try {
            userInteraction.displayPlayerMessage("Welcome to 5 in a row");
            Boolean joinExistingGame = userInteraction.getPlayerBooleanInputWithMessage("Do you have a Game ID and would like to join an existing game? (true or false)");

            if (joinExistingGame) {
                joinExistingGame();
            } else {
                createNewGame();
            }
        } catch (IOException e) {
            log.error("User input exception occurred", e);
        }
    }

    public void joinExistingGame() {

        boolean gameJoined = false;
        boolean tryingToJoin = true;

        while (tryingToJoin) {
            collectExistingGameInfo();
            collectPlayerUsername();
            collectPlayerColourChoice();

            gameJoined = gameService.joinExistingGame(player);
            try {
                if (gameJoined) {
                    tryingToJoin = false;
                } else {
                    tryingToJoin = userInteraction.getPlayerBooleanInputWithMessage("Would you like to try again? (true or false)");
                }
            } catch (Exception e) {
                log.error("Error occurred joining the game", e);
            }
        }

        try {
            if (gameJoined) {
                gameService.play();
            }
        } catch (Exception e) {
            log.error("Error occurred playing the game", e);
        }
    }

    public void createNewGame() {
        boolean gameCreated = false;
        boolean tryingToCreate = true;

        while (tryingToCreate) {
            collectPlayerUsername();
            collectPlayerColourChoice();

            gameCreated = gameService.createNewGame(player);

            try {

                if (gameCreated) {
                    tryingToCreate = false;
                } else {
                    tryingToCreate = userInteraction.getPlayerBooleanInputWithMessage("Would you like to try again? (true or false)");
                }
            } catch (Exception e) {
                log.error("Error occurred creating the game", e);
            }
        }

        try {
            if (gameCreated) {
                gameService.play();
            }
        } catch (Exception e) {
            log.error("Error occurred playing the game", e);
        }
    }

    public void collectPlayerUsername() {
        try {
            // Get their username
            player.setUsername(userInteraction.getPlayerStringInputWithMessage("Please Provide Your Username:"));
        } catch (IOException e) {
            log.error("User input exception occurred", e);
        }
    }

    public void collectPlayerColourChoice() {
        try {
            // Get their colour choice
            player.setColourChoice(userInteraction.getPlayerStringInputWithMessage("Please Provide Your Colour Choice (X or O):"));
        } catch (IOException e) {
            log.error("User input exception occurred", e);
        }
    }

    public void collectExistingGameInfo() {
        try {
            // Get their game info
            player.setGameId(userInteraction.getPlayerIntegerInputWithMessage("Please Provide The Existing Game ID:"));
        } catch (IOException e) {
            log.error("User input exception occurred", e);
        }
    }
}
