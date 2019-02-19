package client;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.*;

public class HTTPServer implements Runnable, MethodHandler{

    static final int PORT = 8081;
    static final String FILE_NOT_FOUND ="404.html";
    static final String DEFAULT_FILE = "src/index.html";
    static final File WEB_ROOT = new File(".");

    static List<HTTPMethod> httpMethods = new ArrayList();
    static List<Feature> features = new ArrayList();
    static Map<String, Object> parameters = new HashMap<>();

    private Socket socket;

    public HTTPServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName());
        readRequest();
        //socket.close();

    }

    public void readRequest() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input = in.readLine();
            StringTokenizer parse = new StringTokenizer(input);
            String httpMethod = parse.nextToken().toUpperCase();
            String requestCrude = parse.nextToken().toLowerCase();
            if (requestCrude.length() <= 1) {
                System.out.println(requestCrude);
                new RequestHandler(in, httpMethod);
            } else {
                String request = requestCrude.substring(1);
                System.out.println("HTTP method: " + httpMethod);
                parseQuery(request, parameters);
                System.out.println("Here are the parameters: " + parameters.toString());


                for (HTTPMethod method : httpMethods) {
                    if (method.getClass().getSimpleName().toUpperCase().equals(httpMethod)) {
                        method.execute(request, socket);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void parseQuery(String query, Map<String, Object> params) throws UnsupportedEncodingException {

            if (query != null) {
                query = query.substring(query.indexOf("?")+1);
                String[] pairs = query.split("[&]");
                for (String pair : pairs) {
                    String[] param = pair.split("[=]");
                    String key = null;
                    String value = null;
                    if (param.length > 0) {
                        key = URLDecoder.decode(param[0],
                                System.getProperty("file.encoding"));
                    }

                    if (param.length > 1) {
                        value = URLDecoder.decode(param[1],
                                System.getProperty("file.encoding"));
                    }

                    if (params.containsKey(key)) {
                        Object obj = params.get(key);
                        if (obj instanceof List<?>) {
                            List<String> values = (List<String>) obj;
                            values.add(value);

                        } else if (obj instanceof String) {
                            List<String> values = new ArrayList<>();
                            values.add((String) obj);
                            values.add(value);
                            params.put(key, values);
                        }
                    } else {
                        params.put(key, value);
                    }
                }
            }
    }

    @Override
    public void handleRequest() {
    }
}
