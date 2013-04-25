package com.dracula.webserver;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ConfigReader {
    private String filePath;
    private Document document;

    public ConfigReader(String filePath) throws IOException, SAXException, ParserConfigurationException {
        this.filePath = filePath;
    }

    public void createDocumentObject() throws ParserConfigurationException, IOException, SAXException {
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

    public Iterator<String> getFileExtensions() {
        String extensions = document.getElementsByTagName("static-file-pattern").item(0).getTextContent();
        extensions = extensions.replace("*.", "");
        String[] extensionArray = extensions.split(", ");
        List<String> extensionList = new ArrayList<String>();

        Collections.addAll(extensionList, extensionArray);

        return extensionList.iterator();
    }

    public String getUrlPattern(PatternType type) throws IOException, SAXException, ParserConfigurationException {
        createDocumentObject();
        String staticPattern = document.getElementsByTagName("url-pattern").item(0).getTextContent();
        String dynamicPattern = document.getElementsByTagName("url-pattern").item(1).getTextContent();

        return type == PatternType.STATIC ? staticPattern : dynamicPattern;
    }

    public String getContentType(String file) {
        return document.getElementsByTagName(file).item(0).getTextContent();
    }

    public int getDynamicServerPort() {
        return Integer.parseInt(document.getElementsByTagName("port").item(0).getTextContent());
    }

}