package client;

import java.io.*;
import java.net.Socket;
import java.util.Date;

import static client.HTTPServer.*;
import static client.Head.getBytes;

public class Get extends HTTPMethod {

    public void execute(String request, Socket clientSocket) {

        int index = request.length()>8 ? 7 : 2;
        String first = request.substring(0, index);
        if (first.equalsIgnoreCase("feature")) {
            System.out.println(first);
            String param = request.substring(request.lastIndexOf("=") + 1);
            System.out.println(param);
            for (Feature f : features) {
                //if(f.getClass().getSimpleName().equalsIgnoreCase("feature1")){
                f.createHTML(param);
                File file = f.getHtmlFile();
                int fileLength = (int) file.length();
                printResponse("text/html", file, fileLength, clientSocket);
                //}
            }
        } else {
            File file = new File(WEB_ROOT, request);
            System.out.println("Requested type: " + request);
            int fileLength = (int) file.length();
            String content = getContentType(request);
            printResponse(content, file, fileLength, clientSocket);
        }
    }

    private void printResponse(String content, File file, int fileLength, Socket clientSocket){

        try {
            byte[] fileData = readFileData(file, fileLength);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
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

        } catch (FileNotFoundException fnf){
            fileNotFound(content, clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getContentType(String request) {
        if(request.endsWith(".html") || request.endsWith(".htm")) return "text/html";
        else if(request.endsWith(".css")) return "text/css";
        else if(request.endsWith(".jpg")) return "image/jpg";
        else if (request.endsWith(".json")) return "application/json";
        else if (request.endsWith(".js")) return "text/javascript";
        else if (request.endsWith(".pdf")) return "application/pdf";
        else return "text/html";
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        return getBytes(file, fileLength);
    }

    private void fileNotFound(String request, Socket clientSocket){
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

}
