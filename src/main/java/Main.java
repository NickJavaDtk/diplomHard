import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Main {
    final static private int port = 8989;

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept( );
             PrintWriter printWriter = new PrintWriter(socket.getOutputStream( ), true);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream( )))) {
            while (true) {
                String input = bufferedReader.readLine( );
                if (input == null) {
                    break;
                }
                List<PageEntry> list = engine.search(input);
                GsonBuilder builder = new GsonBuilder( );
                Gson gson = builder.create( );
                Type listType = new TypeToken<List<PageEntry>>( ) {
                }.getType( );
                String json = gson.toJson(list, listType);
                printWriter.println(json);
            }

        } catch (IOException e) {
            e.printStackTrace( );
        }
    }
}