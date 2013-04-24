package com.dracula.webserver;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.StringTokenizer;

public class WebServer{
    private ServerSocket serverSocket;
    private Socket socket;
    private ConfigReader configReader;

    public WebServer(ServerSocket socket, String configFile) throws ParserConfigurationException, SAXException, IOException {
        this.serverSocket = socket;
        this.configReader = new ConfigReader(configFile);
    }

    public void connect() throws IOException, ParserConfigurationException, SAXException {
        socket = serverSocket.accept();
    }

    public void handleRequest() throws IOException, ParserConfigurationException, SAXException {
        String url = getUrl();
        if (isStatic(url)) {
            handleStaticRequest(url);
        } else {
            handleDynamicRequest( url);
        }

        socket.close();
    }

    private void handleDynamicRequest(String url) throws IOException {

    }

    private boolean isStatic(String url) throws ParserConfigurationException, SAXException, IOException {
        return url.contains(configReader.getUrlPattern(PatternType.STATIC));
    }

    private void handleStaticRequest(String url) throws IOException, SAXException, ParserConfigurationException {
        if (isExtensionPresent(url)) {
            String filePath = configReader.getStaticPath() + getFileName(url);

            try {
                sendResponse(filePath);
            } catch (FileNotFoundException e) {
                sendResponse("./src/com/dracula/static/fileNotFound.html");
            }
        }
    }

    private void sendResponse(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[1024];
        int bytes = 0;

        while ((bytes = fileInputStream.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytes);
        }
    }

    String getFileName(String url) {
        int beginIndex = url.lastIndexOf("/") + 1;
        return url.substring(beginIndex);
    }

    private String getUrl() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        stringTokenizer.nextToken();
        return stringTokenizer.nextToken();
    }

    private boolean isExtensionPresent(String url) throws IOException, SAXException, ParserConfigurationException {
        Boolean result = false;
        Iterator fileExtensions = configReader.getFileExtensions();

        while (fileExtensions.hasNext()) {
            String extension = (String) fileExtensions.next();
            if (url.endsWith(extension)) result = true;
        }

        return result;
    }
}