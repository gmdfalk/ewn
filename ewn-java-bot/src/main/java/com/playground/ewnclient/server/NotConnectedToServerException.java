package com.playground.ewnclient.server;

public class NotConnectedToServerException extends Exception {
    NotConnectedToServerException(String message) {
        super(message);
    }
}
