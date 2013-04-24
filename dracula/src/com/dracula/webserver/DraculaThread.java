package com.dracula.webserver;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class DraculaThread implements Runnable {
    private WebServer dracula;

    public DraculaThread(WebServer dracula) {
        this.dracula = dracula;
    }

    @Override
    public void run(){
        try {
            dracula.handleRequest();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

