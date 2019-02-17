package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;

public class HTTPServer implements Runnable, MethodHandler{

    static final int PORT = 8081;
    static final String FILE_NOT_FOUND ="404.html";
    static final String DEFAULT_FILE = "src/index.html";
    static final File WEB_ROOT = new File(".");

    static List<HTTPMethod> httpMethods = new ArrayList();
    static List<Feature> features = new ArrayList();

    private Socket socket;
    HTTPMethod Method = new HTTPMethod();
    private Object HTTPMethod;

    public HTTPServer(Socket socket) {
        this.socket = socket;
    }

    public HTTPServer(Object HTTPMethod) {
        this.HTTPMethod = HTTPMethod;
    }

    public void run() {

        readRequest();
        System.out.println(Thread.currentThread().getName());
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread();
        thread.start();

    }

    void startWork(String request, Socket clientSocket, HTTPMethod target) {
        try {
            Class<?> requestType = Class.forName(request);
            MethodHandler response = (MethodHandler) requestType.newInstance();
            response.setTarget(target);
            response.handleRequest();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readRequest() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input = in.readLine();
            StringTokenizer parse =  new StringTokenizer(input);
            String httpMethod = parse.nextToken().toUpperCase();
            String request = parse.nextToken().toLowerCase();
            System.out.println("HTTP method: " + httpMethod);

//            Class<?> requestType = Class.forName(httpMethod);
//            Class<?> httpType = requestType.getClass();
//            Constructor c = requestType.getConstructor(httpType);
//            HTTPMethod method = (HTTPMethod) c.newInstance(httpType);
//            //HTTPMethod response = (HTTPMethod) requestType.newInstance();
//            method.execute(request, socket);



            for (HTTPMethod method: httpMethods) {
                if(method.getClass().getSimpleName().toUpperCase().equals(httpMethod)){
                    method.execute(request, socket);
                }
            }

        } catch (IOException  e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setTarget(HTTPMethod target) {
        if (Method.getClass().isInstance(target)){
            Method = target;
        }
    }

    @Override
    public void handleRequest() {
        Thread thread = new Thread(HTTPMethod instanceof Runnable ? (Runnable) Method : this);
        thread.start();
    }
}
