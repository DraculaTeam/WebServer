package com.dracula.webserver;

import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;

public class Dracula {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String filePath = "./src/com/dracula/webserver/config.xml";
        ConfigReader configReader = new ConfigReader(filePath);
        configReader.createDocumentObject();
        ServerSocket socket = new ServerSocket(configReader.getPort());
        while (true) {
            WebServer server = new WebServer(socket, filePath);
            DraculaThread draculaThread = new DraculaThread(server);
            server.connect();
            new Thread(draculaThread).start();
        }
    }
}
