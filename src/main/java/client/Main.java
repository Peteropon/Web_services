package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Date;

import static client.HTTPServer.*;

public class Main {

    static final boolean verbose = true;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started.\nListening for connections on port: "
                    + PORT + "...");
            httpMethods.addAll(Arrays.asList(new Get(), new Head(), new Post()));
            Feature feature1 = new Feature();
            features.add(feature1);

            while (true){
                HTTPServer server = new HTTPServer(serverSocket.accept());
                server.readRequest();
                if(verbose) System.out.println("Connection established. " + new Date());

                Thread thread = new Thread(server);
                thread.start();

            }

        } catch (IOException e) {
            System.err.println("Server connection error" + e.getMessage());
        }


    }

}
