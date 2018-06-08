package com.playground.ewnclient.client;

import com.playground.ewnclient.server.NotConnectedToServerException;
import com.playground.ewnclient.server.ServerAction;
import com.playground.ewnclient.server.ServerConnection;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleClient implements IClient, Runnable {

    boolean isRunning;
    private Reader inputReader;
    private ServerConnection serverConnection;

    public ConsoleClient(Reader inputReader) {
        this.inputReader = inputReader;
        this.isRunning = true;
        this.serverConnection = new ServerConnection();
    }

    String readInput() {
        Scanner scanner = new Scanner(inputReader);

        String input = scanner.nextLine();
        System.out.println("Received input: " + input);

        return input;
    }

    void processInput(String input) {
        ClientAction clientAction;
        List<String> inputArgs = Arrays.asList(input.split("\\s"));
        try {
            clientAction = ClientAction.valueOf(inputArgs.get(0).toUpperCase());
            switch (clientAction) {
                case LOGIN:
                    if (inputArgs.size() == 2) {
                        login(inputArgs.get(1));
                    } else {
                        System.out.println("Usage: 'login sokrates'");
                    }
                    break;
                case LOGOUT:
                    logout();
                    break;
                case CONNECT:
                    connect();
                    break;
                case PLAY:
                    play();
                    break;
                case HELP:
                    help();
                    break;
                case EXIT:
                    exit();
                    break;
            }
        } catch (IllegalArgumentException e) {
            help();
        }
    }

    public void login(String username) {
        try {
            String loginResponse = serverConnection.sendAction(ServerAction.LOGIN, username);
            System.out.println(loginResponse);
            String werBinIchResponse = serverConnection.sendAction(ServerAction.WERBINICH, null);
            System.out.println(werBinIchResponse);
        } catch (NotConnectedToServerException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void logout() {
        try {
            String response = serverConnection.sendAction(ServerAction.LOGOUT, null);
            System.out.println(response);
        } catch (NotConnectedToServerException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            serverConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        try {
            String listeResponse = serverConnection.sendAction(ServerAction.LISTE, null);
            System.out.println(listeResponse);
//            List<String> availablePlayers = parseAvailablePlayers(listeResponse);

//            String spielResponse = serverConnection.sendAction(ServerAction.SPIEL, null);
//            System.out.println(spielResponse);
        } catch (NotConnectedToServerException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> parseAvailablePlayers(String listeResponse) {
        List<String> players = new ArrayList<String>();
        Pattern pattern = Pattern.compile(".*:\\s\\b(.*)\\b+");
        Matcher matcher = pattern.matcher(listeResponse);
        while (matcher.find()) {
            System.out.println("group 1: " + matcher.group(1));
            System.out.println("group 2: " + matcher.group(2));
            System.out.println("group 3: " + matcher.group(3));
        }
        return players;
    }

    public void help() {
        System.out.println("Available commands (case insensitive): " + Arrays.toString(ClientAction.values()));
    }

    public void exit() {
        System.out.println("Exiting");
        this.isRunning = false;
    }

    public void run() {
        help();
        while (this.isRunning) {
            this.processInput(this.readInput());
        }
    }
}
