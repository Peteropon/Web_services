package client;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class HTTPServer implements Runnable{

    static final int PORT = 8081;
    static final String FILE_NOT_FOUND ="404.html";
    static final String DEFAULT_FILE = "src/index.html";
    static final String METHOD_NOT_SUPPORTED = "not_supported.html";
    static final File WEB_ROOT = new File(".");

    static List<HTTPMethod> httpMethods = new ArrayList();

    private Socket socket;

    public HTTPServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        System.out.println(Thread.currentThread().getName());
        readRequest();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readRequest() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input = in.readLine();
            StringTokenizer parse =  new StringTokenizer(input);
            String httpMethod = parse.nextToken().toUpperCase();
            String request = parse.nextToken().toLowerCase();
            System.out.println(request);
            for (HTTPMethod method: httpMethods) {
                if(method.getClass().getSimpleName().toUpperCase().equals(httpMethod)){
                    method.execute(request, socket);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
