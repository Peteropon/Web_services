package client;

import java.io.*;
import java.net.Socket;
import java.util.Date;

import static client.HTTPServer.FILE_NOT_FOUND;
import static client.HTTPServer.WEB_ROOT;

public class Post extends HTTPMethod {
    public void startWork(String request, Socket clientSocket, HTTPMethod target) {

    }

    public void printResponse(String content, File file, int fileLength, Socket clientSocket){
        PrintWriter out;
        try {
            byte[] fileData = readFileData(file, fileLength);
            out = new PrintWriter(clientSocket.getOutputStream());
            out.println("HTTP/1.1 200 OK");
            out.println("Server: Java HTTP Server from Mr Johansson's : 1.0");
            out.println("Date: " + new Date());
            out.println("Content-type: " + content);
            out.println("Content-length: " + fileLength);
            out.println();
            out.flush();
            BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream());
            dataOut.write(fileData, 0, fileLength);
            dataOut.flush();
            dataOut.close();

            System.out.println("Response sent successfully.");
        }
        catch (FileNotFoundException fnf){
            fileNotFound(content, clientSocket);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getContentType(String request) {
        if(request.endsWith(".html") || request.endsWith(".htm")) return "text/html";
        else if(request.endsWith(".png") || request.endsWith(".jpg")) return "text/jpg";
        else if(request.endsWith(".css")) return "text/css";
        else if (request.endsWith(".json")) return "text/json";
        else if (request.endsWith(".js")) return "text/js";
        else if (request.endsWith(".pdf")) return "text/pdf";
        else return "text/plain";
    }


    public void fileNotFound(String request, Socket clientSocket) {
        try {
            File file = new File(WEB_ROOT, FILE_NOT_FOUND);
            int fileLength = (int) file.length();
            String content = "text/html";
            byte[] fileData = readFileData(file, fileLength);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            out.println("HTTP/1.1 404 File Not Found");
            out.println("Server: Java HTTP Server from Mr Johansson's : 1.0");
            out.println("Date: " + new Date());
            out.println("Content-type: " + content);
            out.println("Content-length: " + fileLength);
            out.println();
            out.flush();
            BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream());
            dataOut.write(fileData, 0, fileLength);
            dataOut.flush();
            dataOut.close();

        }catch (IOException io){
            System.err.println("Error with file not found exception : " + io.getMessage());
        }

        System.out.println("File " + request + " not found");

    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        return getBytes(file, fileLength);
    }

    static byte[] getBytes(File file, int fileLength) throws IOException {
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
