//package net.seancoyne.fiveinarowserver.service;
//
//import lombok.extern.log4j.Log4j2;
//import net.seancoyne.fiveinarowserver.model.*;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Log4j2
//@Component
//public class BizService {
//
//    private Map<String, String> registeredUsers;
//
//    private int[][] board;
//
//    public BizService() {
//        registeredUsers = new HashMap<>();
//        board = new int[6][9];
//    }
//
//    public RegisterResponse register(RegisterRequest registerRequest) {
//
//        // Check username isnt already taken or hasent already registered
//        if (registeredUsers.containsKey(registerRequest.getUserName())) {
//            return RegisterResponse.builder()
//                    .responseState(ResponseState.FAILED)
//                    .message("Username is already registered")
//                    .build();
//        }
//
//        // Check colour isnt already taken
//        if (registeredUsers.containsValue(registerRequest.getColour())) {
//            return RegisterResponse.builder()
//                    .responseState(ResponseState.FAILED)
//                    .message("Colour is already taken")
//                    .build();
//        }
//
//        // check the number of registered users
//        if (registeredUsers.size() >= 2) {
//            return RegisterResponse.builder()
//                    .responseState(ResponseState.FAILED)
//                    .message("The game is already full")
//                    .build();
//        }
//
//        // Register user
//        registeredUsers.put(registerRequest.getUserName(), registerRequest.getColour());
//
//        // Return to the user that they are registered
//        return RegisterResponse.builder()
//                .responseState(ResponseState.SUCCESS)
//                .message("Successfully registered for game")
//                .build();
//    }
//
//    public GameStateResponse getGameState(String userName) {
//
//        // check user is registered
//        if (!registeredUsers.containsKey(userName)) {
//            return GameStateResponse.builder()
//                    .responseState(ResponseState.FAILED)
//                    .message("Username is not registered")
//                    .build();
//        }
//
//        // check all players are registered
//        if (registeredUsers.size() == 1) {
//            return GameStateResponse.builder()
//                    .responseState(ResponseState.SUCCESS)
//                    .gameState(GameState.WAITING_FOR_NEXT_PLAYER_TO_REGISTER)
//                    .message("Waiting for the next player")
//                    .build();
//        }
//
//        return GameStateResponse.builder()
//                .responseState(ResponseState.SUCCESS)
//                .gameState(GameState.YOUR_MOVE)
//                .currentBoard(board)
//                .build();
//    }
//}
