package client;

import java.io.File;
import java.net.Socket;

public interface HTTPResponse {
    void printResponse(String content, File file, int fileLength, Socket clientSocket);
    String getContentType(String request);
    void fileNotFound(String request, Socket clientSocket);

}
