package client;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class Get implements HTTPMethod {
    public void execute(String request, Socket clientSocket) {


        int index = request.endsWith("/") ? request.indexOf("/", 1) : 1;
        String first = request.substring(1, index);
        if(first.equalsIgnoreCase("function")){
            String functionName = request.substring(index + 1, request.indexOf("/", index + 1));
            System.out.println(functionName);
            System.out.println(first);
        }
        else {
            File file = new File("." + request);
            int fileLength = (int) file.length();
            String content = getContentType(request);
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

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String getContentType(String request) {
        if(request.endsWith(".html") || request.endsWith(".htm")) return "text/html";
        else if(request.endsWith(".png") || request.endsWith(".jpg")) return "text/image";
        else if (request.endsWith(".json")) return "text/json";
        else if (request.endsWith(".pdf")) return "text/pdf";
        return "404.html";
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
