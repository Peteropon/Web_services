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
import java.util.Date;
import java.util.StringTokenizer;

public class HTTPServer implements Runnable{

    static final int PORT = 8081;
    static final String FILE_NOT_FOUND ="404.html";
    static final String DEFAULT_FILE = "src/index.html";
    static final File WEB_ROOT = new File(".");
    static final boolean verbose = true;

    private Socket socket;

    public HTTPServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        readRequest();
        sendGetResponse();

    }

    private void sendGetResponse() {
        File file = new File(WEB_ROOT, "src/index.html");
        int fileLength = (int) file.length();
        String content = "text/html";

        try {
            byte[] fileData = readFileData(file, fileLength);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("HTTP/1.1 200 OK");
            out.println("Server: Java HTTP Server from SSaurel : 1.0");
            out.println("Date: " + new Date());
            out.println("Content-type: " + content);
            out.println("Content-length: " + fileLength);
            out.println();
            out.flush();

            BufferedOutputStream dataOut = new BufferedOutputStream(socket.getOutputStream());
            dataOut.write(fileData, 0, fileLength);
            dataOut.flush();

            System.out.println("Response sent successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private void readRequest() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input = in.readLine();
            StringTokenizer parse = new StringTokenizer(input);
            String httpMethod = parse.nextToken().toUpperCase();
            String fileName = parse.nextToken().toLowerCase();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started.\nListening for connections on port: "
                + PORT + "...");
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

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

}
