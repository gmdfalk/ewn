package com.playground.ewnclient.server;

public enum ServerAction {
    HILFE("hilfe"), LOGIN("login"), LOGOUT("logout"), LISTE("liste"), WERBINICH("werBinIch"), SPIEL("spiel");

    private final String actionString;

    private ServerAction(String actionString) {
        this.actionString = actionString;
    }

    public String toString() {
        return this.actionString;
    }
}
