package com.dracula.webserver;

public class WebServerTest {

//    @Test
//    public void shouldConnectToTheServer() throws IOException {
//        WebServer server = new WebServer(new ServerSocket(8081));
//        WebServer mock = Mockito.mock(WebServer.class);
//        Mockito.doNothing().when(mock).connect();
//        mock.connect();
//        Socket client = new Socket("localhost", 8081);
//        Mockito.verify(mock).connect();
//    }
//
//    @Test(expected = ConnectException.class)
//    public void shouldThrowExceptionWhileConnectingWithDifferentPort() throws IOException {
//        WebServer server = new WebServer(new ServerSocket(5776));
//        WebServer mock = Mockito.mock(WebServer.class);
//        Mockito.doNothing().when(mock).connect();
//        mock.connect();
//        Socket client = new Socket("localhost", 9099);
//        Mockito.verify(mock).connect();
//    }
//
//    @Test
//    public void shouldGiveFilenameFromTheGivenUrl() throws IOException {
//        WebServer server = new WebServer(new ServerSocket(8051));
//        assertThat(server.getFileName("forum/activityWall.html"), IsEqual.equalTo("activityWall.html"));
//    }
}