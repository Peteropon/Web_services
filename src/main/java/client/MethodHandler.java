package client;

import java.io.File;
import java.net.Socket;

public interface MethodHandler {
    HTTPMethod METHOD = new HTTPMethod();

    void handleRequest();

}