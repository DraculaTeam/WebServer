package com.dracula.webserver;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Iterator;
import java.util.StringTokenizer;

public class WebServer {
    private ServerSocket serverSocket;
    private Socket socket;
    private ConfigReader configReader;
    private BufferedReader requestHeader;
    private String requestMethod;

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
            handleDynamicRequest(url);
        }

        socket.close();
    }

    private void handleDynamicRequest(String url) throws IOException {
        try {
            URL dynamicUrl = new URL("http://" + configReader.getServerAddress() + ":" + configReader.getDynamicServerPort() + url);
            HttpURLConnection connection = (HttpURLConnection) dynamicUrl.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            writeResponse(new PrintWriter(socket.getOutputStream()), bufferedReader);
        } catch (Exception e) {

        }

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

    private void sendResponse(String file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
        PrintWriter printWriter = new PrintWriter(dataOutputStream);
        writeResponse(printWriter, bufferedReader);
    }

    private void writeResponse(PrintWriter printWriter, BufferedReader bufferedReader) throws IOException {
        String line;
        StringBuilder builder = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
        }

        printWriter.print(builder.toString());
        printWriter.flush();
    }

    String getFileName(String url) {
        int beginIndex = url.lastIndexOf("/") + 1;
        return url.substring(beginIndex);
    }

    private String getUrl() throws IOException {
        requestHeader = getRequestHeader();

        StringTokenizer stringTokenizer = new StringTokenizer(requestHeader.readLine());

        requestMethod = stringTokenizer.nextToken();
        return stringTokenizer.nextToken();
    }

    private BufferedReader getRequestHeader() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
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