package com.playground.ewnclient.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ServerConnection {
    public Socket socket;
    private String host = "vpf.mind-score.de";
    private int port = 1078;
    private BufferedReader reader;
    private BufferedWriter writer;

    public void connect() throws IOException {
        socket = new Socket(host, port);
        setupStreams();
    }

    private void setupStreams() throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        writer = new BufferedWriter(outputStreamWriter);

        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        reader = new BufferedReader(inputStreamReader);
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public ServerResponse sendAction(ServerAction serverAction, String parameter) throws IOException, NotConnectedToServerException {
        if (!isConnected()) {
            throw new NotConnectedToServerException("Currently not connected to the server.");
        }
        String request = serverAction.toString();
        if (parameter != null) {
            request += " " + parameter;
        }
        ServerResponse response = null;
        try {
            response = sendRequest(request);
        } catch (SocketException e) {
            System.out.println("Socket is broken. Please reconnect.");
        }
        return response;
    }

    private ServerResponse sendRequest(String request) throws IOException {
        writer.write(request + "\n");
        writer.flush();
        System.out.println("Request: " + request);
        ServerResponse response = new ServerResponse(reader.readLine());
        System.out.println("Response: " + response);
        return response;
    }
}
