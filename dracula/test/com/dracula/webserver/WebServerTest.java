package com.dracula.webserver;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServerTest {

    @Test
    public void shouldConnectToTheServer() throws IOException {
        WebServer server = new WebServer(new ServerSocket(8081));
        WebServer mock = Mockito.mock(WebServer.class);
        Mockito.doNothing().when(mock).start();
        mock.start();
        Socket client = new Socket("localhost",8081);
        Mockito.verify(mock).start();
    }

    @Test(expected = ConnectException.class)
    public void shouldThrowExceptionWhileConnectingWithDifferentPort() throws IOException {
        WebServer server = new WebServer(new ServerSocket(5776));
        WebServer mock = Mockito.mock(WebServer.class);
        Mockito.doNothing().when(mock).start();
        mock.start();
        Socket client = new Socket("localhost",9099);
        Mockito.verify(mock).start();
    }
}