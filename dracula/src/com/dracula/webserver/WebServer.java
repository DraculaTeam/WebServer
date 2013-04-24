package com.dracula.webserver;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.StringTokenizer;

public class WebServer implements Runnable{
    private ServerSocket serverSocket;
    private Socket socket;

    public WebServer(ServerSocket socket) {
        this.serverSocket = socket;
    }

    public void connect() throws IOException, ParserConfigurationException, SAXException {
        socket = serverSocket.accept();
    }

    private void handleRequest(String configFile) throws IOException, ParserConfigurationException, SAXException {
        ConfigReader configReader = new ConfigReader(configFile);
        configReader.createDocumentObject();
        String url = getUrl();

        if (isStatic(configReader, url)) {
            handleStaticRequest(configReader, url);
        } else {
            handleDynamicRequest(configReader, url);
        }
        socket.close();
    }

    private void handleDynamicRequest(ConfigReader configReader, String url) throws IOException {

    }

    private boolean isStatic(ConfigReader configReader, String url) {
        return url.contains(configReader.getUrlPattern(PatternType.STATIC));
    }

    private void handleStaticRequest(ConfigReader configReader, String url) throws IOException, SAXException, ParserConfigurationException {
        if (isExtensionPresent(configReader, url)) {
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

    private boolean isExtensionPresent(ConfigReader configReader, String url) throws IOException, SAXException, ParserConfigurationException {
        Boolean result = false;
        Iterator fileExtensions = configReader.getFileExtensions();

        while (fileExtensions.hasNext()) {
            String extension = (String) fileExtensions.next();
            if (url.endsWith(extension)) result = true;
        }
        return result;
    }

    @Override
    public void run(){
        try {
            handleRequest("./src/com/dracula/webserver/config.xml");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}