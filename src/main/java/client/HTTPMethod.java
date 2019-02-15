package client;

import java.io.FileNotFoundException;
import java.net.Socket;

public interface HTTPMethod {

    void execute(String request, Socket clientSocket) throws FileNotFoundException;

}
