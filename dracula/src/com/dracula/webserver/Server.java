package com.dracula.webserver;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String filePath = "./src/com/dracula/webserver/config.xml";
        ConfigReader configReader = new ConfigReader(filePath);
        ServerSocket socket = new ServerSocket(configReader.getPort());
        while(true){
            WebServer server = new WebServer(socket);
            server.connect();
            new Thread(server).start();
        }
    }
}
