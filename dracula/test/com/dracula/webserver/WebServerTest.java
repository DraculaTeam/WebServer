package com.dracula.webserver;

import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServerTest {

    @Test
    public void shouldConnectToTheServer() throws IOException, ParserConfigurationException, SAXException {
        WebServer server = new WebServer(new ServerSocket(8081));
        WebServer mock = Mockito.mock(WebServer.class);
        Mockito.doNothing().when(mock).connect();
        mock.connect();
        Socket client = new Socket("localhost", 8081);
        Mockito.verify(mock).connect();
    }

    @Test(expected = ConnectException.class)
    public void shouldThrowExceptionWhileConnectingWithDifferentPort() throws IOException, ParserConfigurationException, SAXException {
        WebServer server = new WebServer(new ServerSocket(5776));
        WebServer mock = Mockito.mock(WebServer.class);
        Mockito.doNothing().when(mock).connect();
        mock.connect();
        Socket client = new Socket("localhost", 9099);
        Mockito.verify(mock).connect() ;
    }

    @Test(expected = IOException.class)
    public void shouldReturnIOException() throws Exception {
        WebServer server = PowerMock.createPartialMock(WebServer.class,"handleRequest");
        PowerMock.expectPrivate(server,"handleRequest","./src/com/dracula/webserver/wrong.xml").andThrow(new IOException());
        new Thread(server).start();
    }
}