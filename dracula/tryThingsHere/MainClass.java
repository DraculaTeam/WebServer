
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;

public class MainClass {
    public static void main(String args[]) throws Exception {
        FileOutputStream fouts = null;
        System.setProperty("javax.net.ssl.trustStore", "clienttrust");
        SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
        Socket s = ssf.createSocket("127.0.0.1", 5432);

        OutputStream outs = s.getOutputStream();
        PrintStream out = new PrintStream(outs);
        InputStream ins = s.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(ins));

        out.println(args[0]);
        fouts = new FileOutputStream("result.html");
//  fouts = new FileOutputStream("result.gif");
        int kk;
        while ((kk = ins.read()) != -1) {
            fouts.write(kk);
        }
        in.close();
        fouts.close();
    }
}