package com.playground.ewnclient.client;

public interface IClient {
    void login(String username);
    void logout();
    void connect();
    void play();
    void help();
    void exit();
}
