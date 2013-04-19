import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ReverseWebServerTry {
    public static void main(String[] args) throws IOException {
        try {
            ServerSocket server = new ServerSocket(5776);
            Socket sock = server.accept();


        }
        catch (IOException e) {
            System.err.println(e);
        }

    }
}
