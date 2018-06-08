package com.playground.ewnclient;

import java.io.*;
import java.net.Socket;

public class ServerConnection {
    protected Socket socket;
    private String host = "vpf.mind-score.de";
    private int port = 1078;
    private BufferedReader reader;
    private BufferedWriter writer;

    public void connect() {
        try {
            socket = new Socket(host, port);
            setupStreams();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupStreams() throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        writer = new BufferedWriter(outputStreamWriter);

        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        reader = new BufferedReader(inputStreamReader);
    }

    public String sendAction(ServerAction serverAction, String parameter) throws IOException {
        String request = serverAction.toString();
        if (parameter != null) {
            request += " " + parameter;
        }
        writer.write(request + "\n");
        writer.flush();
        System.out.println("Message sent to the server : " + request);
        String response = reader.readLine();
        System.out.println("Response received from the server : " + response);
        return response;
    }
}
