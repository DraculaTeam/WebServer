package com.dracula.webserver;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertThat;

public class ConfigReaderTest {

    private ConfigReader configReader;

    @Before
    public void setUp() throws ParserConfigurationException, SAXException, IOException {
        configReader = new ConfigReader("./test/com/dracula/webserver/config.xml");
    }

    @Test
    public void shouldReturnPortNumber() throws ParserConfigurationException, SAXException, IOException {
        assertThat(configReader.getPort(), IsEqual.equalTo(9999));
    }

    @Test
    public void shouldReturnPathOfStaticFiles() {
        assertThat(configReader.getStaticPath(), IsEqual.equalTo("./src/com/dracula/static"));
    }

    @Test
    public void shouldReturnURLofExternalServer() {
        assertThat(configReader.getURL(), IsEqual.equalTo("http://qaserver.com:8080"));
    }

    @Test
    public void shouldReturnTheFileExtension() {
        assertTrue(matchElement());
    }

    private boolean matchElement() {
        Boolean result = true;
        List<String> expectedResult=new ArrayList<String>();
        expectedResult.add("js");
        expectedResult.add("jpeg");
        int i = 0;
        Iterator fileExtensions = configReader.getFileExtensions();

        while(fileExtensions.hasNext()){
            if(!fileExtensions.next().equals(expectedResult.get(i)))result = false;
            i++;
        }

        return result;
    }
}