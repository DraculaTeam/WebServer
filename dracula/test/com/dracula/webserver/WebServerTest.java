package com.dracula.webserver;

import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class WebServerTest {
    private String filePath = "./src/com/dracula/webserver/config.xml";

    @Test
    public void shouldConnectToTheServer() throws IOException, ParserConfigurationException, SAXException {
        WebServer server = new WebServer(new ServerSocket(8081), filePath);
        WebServer mock = Mockito.mock(WebServer.class);
        Mockito.doNothing().when(mock).connect();
        mock.connect();
        Socket client = new Socket("localhost", 8081);
        Mockito.verify(mock).connect();
    }

    @Test(expected = ConnectException.class)
    public void shouldThrowExceptionWhileConnectingWithDifferentPort() throws IOException, ParserConfigurationException, SAXException {
        WebServer server = new WebServer(new ServerSocket(5776), filePath);
        WebServer mock = Mockito.mock(WebServer.class);
        Mockito.doNothing().when(mock).connect();
        mock.connect();
        Socket client = new Socket("localhost", 9099);
        Mockito.verify(mock).connect();
    }

    @Test
    public void shouldCallHandleRequestMethod() throws IOException, ParserConfigurationException, SAXException {
        WebServer server = new WebServer(new ServerSocket(5777), filePath);
        WebServer spy = Mockito.spy(server);
        doNothing().when(spy).handleRequest();
        spy.handleRequest();
        verify(spy).handleRequest();
    }

    @Test(expected = IOException.class)
    public void shouldThrowIOExceptionWhenHandleRequestIsCalled() throws IOException, ParserConfigurationException, SAXException {
        WebServer server = new WebServer(new ServerSocket(5771), filePath);
        WebServer spy = Mockito.spy(server);
        doThrow(IOException.class).when(spy).handleRequest();
        spy.handleRequest();
    }

    @Test(expected = SAXException.class)
    public void shouldThrowISAXExceptionWhenHandleRequestIsCalled() throws IOException, ParserConfigurationException, SAXException {
        WebServer server = new WebServer(new ServerSocket(5772), filePath);
        WebServer spy = Mockito.spy(server);
        doThrow(SAXException.class).when(spy).handleRequest();
        spy.handleRequest();
    }

    @Test(expected = ParserConfigurationException.class)
    public void shouldThrowIParserConfigurationExceptionWhenHandleRequestIsCalled() throws IOException, ParserConfigurationException, SAXException {
        WebServer server = new WebServer(new ServerSocket(5572), filePath);
        WebServer spy = Mockito.spy(server);
        doThrow(ParserConfigurationException.class).when(spy).handleRequest();
        spy.handleRequest();
    }
}