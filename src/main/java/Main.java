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
    final static private String path = "pdfs";
    final static GsonBuilder builder = new GsonBuilder( );

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File(path));

        try (ServerSocket serverSocket = new ServerSocket(port);) {
            String input;
            while (true) {
                try (
                        Socket socket = serverSocket.accept( );
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream( )));
                        PrintWriter printWriter = new PrintWriter(socket.getOutputStream( ));
                ) {
                    // обработка одного подключения
                    input = bufferedReader.readLine( );
                    if (input == null) {
                        break;
                    }
                    String json = getJson(engine, input);
                    printWriter.print(json);

                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace( );
        }
    }

    public static String getJson(BooleanSearchEngine engine, String input) {
        List<PageEntry> list = engine.search(input);
        Gson gson = builder.create( );
        Type listType = new TypeToken<List<PageEntry>>( ) {
        }.getType( );
        String json = gson.toJson(list, listType);
        return json;
    }
}