package com.dracula.webserver;

import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.ConnectException;

import static org.junit.Assert.assertThat;

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
        Mockito.verify(mock).connect();
    }

    @Test
    public void shouldGiveFilenameFromTheGivenUrl() throws IOException {
        WebServer server = new WebServer(new ServerSocket(8051));
        assertThat(server.getFileName("forum/activityWall.html"), IsEqual.equalTo("activityWall.html"));
    }
}