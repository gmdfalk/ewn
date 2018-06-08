package com.playground.ewnclient.server;

public enum ServerResponseCode {
    B("Success"),
    M("Message"),
    Z("Move"),
    Q("GameRequest"),
    E001("UnknownCommand"),
    E101("LoginRequest"),
    E102("NameConflict"),
    E103("LoginNameMissing"),
    E104("AlreadyLoggedIn"),
    E201("GameRequestRejected"),
    E202("RequestNameMissing"),
    E203("PlayerNotFound"),
    E204("PlayerNotAvailable"),
    E205("SelfRequestForbidden"),
    E206("GameRequestPending"),
    E207("AlreadyRequested"),
    E208("Cancelled"),
    E301("MoveTimout"),
    E302("IdleTimeout"),
    E303("GameRequestTimeout");

    private final String description;

    private ServerResponseCode(String description) {
        this.description = description;
    }

    public String toString() {
        return this.description;
    }
}
