package com.dracula.webserver;

import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

public class DraculaThreadTest {
    @Test
    public void shouldVerifyHandleRequestIsCalledInRunMethod() throws ParserConfigurationException, SAXException, IOException {
        String filePath = "./src/com/dracula/webserver/config11.xml";
        WebServer server = new WebServer(new ServerSocket(1111), filePath);
        WebServer spy = Mockito.spy(server);
        doNothing().when(spy).handleRequest();
        DraculaThread draculaThread = new DraculaThread(spy);
        draculaThread.run();

        verify(spy).handleRequest();
    }
}