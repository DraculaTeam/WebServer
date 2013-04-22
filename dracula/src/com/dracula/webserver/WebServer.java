package com.dracula.webserver;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.StringTokenizer;

public class WebServer{
    private ServerSocket serverSocket;
    private Socket socket;

    public WebServer(ServerSocket socket) {
        this.serverSocket = socket;
    }

    public void start() throws IOException {
        socket = serverSocket.accept();
    }

    public void handleRequest(String configFile) throws IOException, ParserConfigurationException, SAXException {
        ConfigReader configReader = new ConfigReader(configFile);
        String url = getUrl();
        if(isExtensionPresent(configReader, url)){
            System.out.println("present");
        }
    }

    private String getUrl() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        stringTokenizer.nextToken();
        return stringTokenizer.nextToken();
    }

    private boolean isExtensionPresent(ConfigReader configReader, String url) throws IOException, SAXException, ParserConfigurationException {
        Boolean result = false;
        Iterator fileExtensions = configReader.getFileExtensions();

        while(fileExtensions.hasNext()){
            String extension= (String) fileExtensions.next();
            if(url.endsWith(extension)) result = true;
        }

        return result;
    }
}