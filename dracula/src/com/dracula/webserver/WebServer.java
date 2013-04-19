package com.dracula.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer{
    private ServerSocket serverSocket;
    private Socket socket;

    public WebServer(ServerSocket socket) {
        this.serverSocket = socket;
    }

    public void start() throws IOException {
        socket = serverSocket.accept();
    }

    public void handleRequest(Socket socket){
        System.out.println("handling request");
    }
}