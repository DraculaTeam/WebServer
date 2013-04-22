package com.dracula.webserver;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.assertThat;

public class ConfigReaderTest {

    private ConfigReader configReader;

    @Before
    public void setUp() throws ParserConfigurationException, SAXException, IOException {
        configReader = new ConfigReader("./src/com/dracula/webserver/config.xml");
    }

    @Test
    public void shouldReturnPortNumber() throws ParserConfigurationException, SAXException, IOException {
        assertThat(configReader.getPort(), IsEqual.equalTo(9999));
    }

    @Test
    public void shouldReturnPathOfStaticFiles(){
        assertThat(configReader.getStaticPath(), IsEqual.equalTo("./src/com/dracula/static"));
    }

    @Test
    public void shouldReturnURLofExternalServer(){
        assertThat(configReader.getURL(),IsEqual.equalTo("http://qaserver.com:8080"));
    }
}