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
        configReader = new ConfigReader("./test/com/dracula/webserver/testConfig.xml");
        configReader.createDocumentObject();
    }

    @Test
    public void shouldReturnPortNumber() throws ParserConfigurationException, SAXException, IOException {
        assertThat(configReader.getPort(), IsEqual.equalTo(9999));
    }

    @Test
    public void shouldReturnPathOfStaticFiles() throws IOException, SAXException, ParserConfigurationException {
        assertThat(configReader.getStaticPath(), IsEqual.equalTo("./src/com/dracula/static/"));
    }

    @Test
    public void shouldReturnURLofExternalServer() throws IOException, SAXException, ParserConfigurationException {
        assertThat(configReader.getServerAddress(), IsEqual.equalTo("http://10.10.5.126"));
    }

    @Test
    public void shouldReturnTheFileExtension() throws ParserConfigurationException, SAXException, IOException {
        assertTrue(matchElement());
    }

    @Test
    public void shouldGiveUrlPatternForStaticRequest() throws IOException, SAXException, ParserConfigurationException {
        assertThat(configReader.getUrlPattern(PatternType.STATIC), IsEqual.equalTo("/forum/static/"));
    }

    @Test
    public void shouldGivePortOfDynamicProxyWebServer() throws IOException, SAXException, ParserConfigurationException {
        assertThat(configReader.getDynamicServerPort(), IsEqual.equalTo(8080));
    }

    @Test
    public void shouldGiveUrlPatternForDynamicRequest() throws IOException, SAXException, ParserConfigurationException {
        assertThat(configReader.getUrlPattern(PatternType.REVERSE_PROXY), IsEqual.equalTo("/forum/"));
    }

    @Test
    public void shouldGiveContentTypeOfJsFile() throws ParserConfigurationException, SAXException, IOException {
        assertThat(configReader.getContentType("js"),IsEqual.equalTo("text/javascript"));
    }

    private boolean matchElement() throws IOException, SAXException, ParserConfigurationException {
        Boolean result = true;

        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add("js");
        expectedResult.add("jpeg");
        int i = 0;
        Iterator fileExtensions = configReader.getFileExtensions();

        while (fileExtensions.hasNext()) {
            if (!fileExtensions.next().equals(expectedResult.get(i))) result = false;
            i++;
        }

        return result;
    }
}