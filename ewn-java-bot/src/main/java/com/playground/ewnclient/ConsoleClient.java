package com.playground.ewnclient;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Scanner;

public class ConsoleClient implements IClient, Runnable {

    boolean isRunning;
    private Reader inputReader;
    private ServerConnection serverConnection;

    ConsoleClient(Reader inputReader) {
        this.inputReader = inputReader;
        this.isRunning = true;
    }

    String readInput() {
        Scanner scanner = new Scanner(inputReader);

        String input = scanner.next();
        System.out.println("Received input: " + input);

        return input;
    }

    void processInput(String input) {
        ClientAction clientAction;
        try {
            clientAction = ClientAction.valueOf(input.toUpperCase());
            switch (clientAction) {
                case LOGIN:
                    login();
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

    public void login() {
        try {
            String loginResponse = serverConnection.sendAction(ServerAction.LOGIN, "max");
            System.out.println(loginResponse);
            String werBinIchResponse = serverConnection.sendAction(ServerAction.WERBINICH, null);
            System.out.println(werBinIchResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void logout() {
        if (!serverConnection.socket.isConnected()) {
            System.out.println("Already logged out.");
            return;
        }
        try {
            String response = serverConnection.sendAction(ServerAction.LOGOUT, null);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        serverConnection = new ServerConnection();
        serverConnection.connect();
        if (!serverConnection.socket.isConnected()) {
            System.out.println("Could not connect");
        }
    }

    public void play() {
        try {
            String response = serverConnection.sendAction(ServerAction.SPIEL, null);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
