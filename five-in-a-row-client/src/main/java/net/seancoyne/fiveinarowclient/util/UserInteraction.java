package net.seancoyne.fiveinarowclient.util;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class UserInteraction {

    private BufferedReader br;

    public UserInteraction() {
        br  = new BufferedReader(new InputStreamReader(System.in));
    }

    public String getPlayerStringInputWithMessage(String message) throws IOException {
        System.out.println(message);
        return br.readLine();
    }

    public Integer getPlayerIntegerInputWithMessage(String message) throws IOException {
        System.out.println(message);
        return Integer.parseInt(br.readLine());
    }

    public Boolean getPlayerBooleanInputWithMessage(String message) throws IOException {
        System.out.println(message);
        return Boolean.parseBoolean(br.readLine());
    }

    public void displayPlayerMessage(String message) {
        System.out.println(message);
    }
}
