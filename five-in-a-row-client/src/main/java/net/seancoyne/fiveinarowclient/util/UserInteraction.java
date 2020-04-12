package net.seancoyne.fiveinarowclient.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserInteraction {

    private final BufferedReader bufferedReader;

    public String getPlayerStringInputWithMessage(String message) throws IOException {
        System.out.println(message);
        return bufferedReader.readLine();
    }

    public Integer getPlayerIntegerInputWithMessage(String message) throws IOException {
        System.out.println(message);
        return Integer.parseInt(bufferedReader.readLine());
    }

    public Boolean getPlayerBooleanInputWithMessage(String message) throws IOException {
        System.out.println(message);
        return Boolean.parseBoolean(bufferedReader.readLine());
    }

    public void displayPlayerMessage(String message) {
        System.out.println(message);
    }
}
