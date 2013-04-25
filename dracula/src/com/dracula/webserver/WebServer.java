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
    private String requestMethod;
    private BufferedReader requestHeader;

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
        URL dynamicUrl = new URL("http://" + configReader.getServerAddress() + ":" + configReader.getDynamicServerPort() + url);
        HttpURLConnection connection = (HttpURLConnection) dynamicUrl.openConnection();
        connection.setRequestMethod(requestMethod);
        connection.connect();
        try {
            passExternalServerResponse(connection);
        } catch (Exception e) {
            e.getCause();
        }

    }

    private void passExternalServerResponse(HttpURLConnection connection) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line);
        }

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.print(builder.toString());
        printWriter.flush();
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
        } else {
            sendResponse("./src/com/dracula/static/fileNotFound.html");
        }
    }

    private void sendResponse(String file) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        FileInputStream fileInputStream = new FileInputStream(file);
        writeResponse(dataOutputStream, fileInputStream, file);
    }

    private int contentLength(FileInputStream fileInputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int NoOfBytes=0,bytes;
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            NoOfBytes = NoOfBytes+bytes;
        }
        return NoOfBytes;
    }

    private void writeResponse(DataOutputStream dataOutputStream, FileInputStream fileInputStream,String file) throws IOException {
        byte[] buffer = new byte[1024];
        int bytes = 0;
        String fileExtension=file.substring(file.lastIndexOf('.')+1);
        dataOutputStream.writeBytes("HTTP/1.1 200 OK\r\n");
        dataOutputStream.writeBytes("version: HTTP/1.1\r\n");
        dataOutputStream.writeBytes("server: dracula\r\n");
        dataOutputStream.writeBytes("Content-Type:"+configReader.getContentType(fileExtension)+"\r\n\n");

        while ((bytes = fileInputStream.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytes);
        }

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
        return new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF8"));
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