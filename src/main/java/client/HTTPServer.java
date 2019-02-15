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
    static final File WEB_ROOT = new File(".");
    static final boolean verbose = true;
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

//    private void sendResponse() {
//
//        File file = new File(  "src/KottbullarRecept.html");
//        int fileLength = (int) file.length();
//        String content = "text/html";
//
//        try {
//            byte[] fileData = readFileData(file, fileLength);
//            PrintWriter out = new PrintWriter(socket.getOutputStream());
//            out.println("HTTP/1.1 200 OK");
//            out.println("Server: Java HTTP Server from Mr Johansson's : 1.0");
//            out.println("Date: " + new Date());
//            out.println("Content-type: " + content);
//            out.println("Content-length: " + fileLength);
//            out.println();
//            out.flush();
//
//            BufferedOutputStream dataOut = new BufferedOutputStream(socket.getOutputStream());
//            dataOut.write(fileData, 0, fileLength);
//            dataOut.flush();
//
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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
                System.out.println(method.getClass().getSimpleName());
                System.out.println(httpMethods.size());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started.\nListening for connections on port: "
                + PORT + "...");
            httpMethods.addAll(Arrays.asList(new Get(), new Head(), new Post()));
            while (true){
                HTTPServer server = new HTTPServer(serverSocket.accept());

                if(verbose) System.out.println("Connection established. " + new Date());

                Thread thread = new Thread(server);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Server connection error" + e.getMessage());
        }
    }


}
