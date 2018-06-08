package com.playground.ewnclient.client;

import com.playground.ewnclient.server.NotConnectedToServerException;
import com.playground.ewnclient.server.ServerAction;
import com.playground.ewnclient.server.ServerConnection;
import com.playground.ewnclient.server.ServerResponse;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConsoleClient implements IClient, Runnable {

    boolean isRunning;
    private Reader inputReader;
    private ServerConnection serverConnection;

    public ConsoleClient(Reader inputReader) {
        this.inputReader = inputReader;
        this.isRunning = true;
        this.serverConnection = new ServerConnection();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logout();
            }
        });
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
            serverConnection.sendAction(ServerAction.LOGIN, username);
            serverConnection.sendAction(ServerAction.WERBINICH, null);
        } catch (NotConnectedToServerException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void logout() {
        try {
            serverConnection.sendAction(ServerAction.LOGOUT, null);
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
            // For some reason, we need to send the LISTE command twice to get the actual list of players.
            serverConnection.sendAction(ServerAction.LISTE, null);
            ServerResponse listeResponse = serverConnection.sendAction(ServerAction.LISTE, null);

            List<String> opponents = listeResponse.availableOpponents();
            String chosenOpponent = chooseOpponent(opponents);

            // Again, apparently we need to double down on this command.
            serverConnection.sendAction(ServerAction.SPIEL, chosenOpponent);
            serverConnection.sendAction(ServerAction.SPIEL, chosenOpponent);
        } catch (NotConnectedToServerException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String chooseOpponent(List<String> opponents) {
        System.out.println("Please choose your opponent: " + opponents.toString());
        String choice = null;
        while (!opponents.contains(choice)) {
            choice = readInput();
        }
        return choice;
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
        logout();
    }
}
