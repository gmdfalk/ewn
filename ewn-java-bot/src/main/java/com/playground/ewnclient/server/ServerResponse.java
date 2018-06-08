package com.playground.ewnclient.server;

public enum ServerResponse {
    Success("B"),
    Message("M"),
    Move("Z"),
    GameRequest("Q"),
    UnknownCommand("E001"),
    LoginRequest("E101"),
    NameConflict("E102"),
    LoginNameMissing("E103"),
    AlreadyLoggedIn("E104"),
    GameRequestRejected("E201"),
    RequestNameMissing("E202"),
    PlayerNotFound("E203"),
    PlayerNotAvailable("E204"),
    SelfRequestForbidden("E205"),
    GameRequestPending("206"),
    AlreadyRequested("E207"),
    Cancelled("E208"),
    MoveTimout("E301"),
    IdleTimeout("E302"),
    GameRequestTimeout("E303");

    private final String code;

    private ServerResponse(String code) {
        this.code = code;
    }

    public String toString() {
        return this.code;
    }
}
