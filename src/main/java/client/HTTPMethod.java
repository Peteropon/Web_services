package client;

import java.net.Socket;

public interface HTTPMethod {

    void execute(String request, Socket clientSocket);

}
