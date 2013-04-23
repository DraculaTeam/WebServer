package com.dracula.webserver;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfigReader {
    private String filePath;
    Document document;

    public ConfigReader(String filePath) throws IOException, SAXException, ParserConfigurationException {
        this.filePath = filePath;
        this.document = parseToDoc();
    }

    public int getPort() throws IOException, SAXException, ParserConfigurationException {
        return Integer.parseInt((document.getElementsByTagName("listen").item(0).getTextContent()));
    }

    private Document parseToDoc() throws ParserConfigurationException, IOException, SAXException {
        File file = new File(filePath);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(file);
    }

    public String getStaticPath() {
        return document.getElementsByTagName("root").item(0).getTextContent();
    }

    public String getServerAddress() {
        return document.getElementsByTagName("upstream-url").item(0).getTextContent();
    }

    public Iterator getFileExtensions() {
        String extensions = document.getElementsByTagName("static-file-pattern").item(0).getTextContent();
        extensions = extensions.replace("*.","");
        String[] extensionArray = extensions.split(", ");
        List extensionList = new ArrayList();

        for (String extension : extensionArray) {
            extensionList.add(extension);
        }

        return extensionList.iterator();
    }

    public String getUrlPattern(PatternType type) {
        String staticPattern = document.getElementsByTagName("url-pattern").item(0).getTextContent();
        String dynamicPattern = document.getElementsByTagName("url-pattern").item(1).getTextContent();

        return type == PatternType.STATIC ? staticPattern : dynamicPattern;
    }

    public int getDynamicServerPort() {
        return Integer.parseInt(document.getElementsByTagName("port").item(0).getTextContent());
    }
}