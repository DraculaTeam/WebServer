package com.dracula.webserver;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(9999);
        WebServer server = new WebServer(socket);
        server.start();
    }
}
