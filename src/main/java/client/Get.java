package client;

import java.net.Socket;

public class Get implements HTTPMethod {
    public void execute(String request, Socket clientSocket) {
        int index = request.indexOf("/", 1);
        String first = request.substring(1, index);
        if(first.equalsIgnoreCase("function")){
            String functionName = request.substring(index + 1, request.indexOf("/", index + 1));
            System.out.println(functionName);
        }
        System.out.println(first);
    }
}
