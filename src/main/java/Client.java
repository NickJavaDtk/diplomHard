import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    final static private String word = "бизнес";

    public static void main(String[] args) throws IOException {
        try (
                Socket socket = new Socket("localhost", 8989);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream( )));
                PrintWriter out = new PrintWriter(socket.getOutputStream( ), true)) {
            out.println(word);
            String s = in.readLine( );
            System.out.println(s);
        }
    }
}
