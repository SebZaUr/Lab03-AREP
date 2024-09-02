package arep.lab3.webServer;

import arep.lab3.springECI.IoCContainer;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class SimpleWebServer {
    private static final int PORT = 8080;
    public static String WEB_ROOT = "src/main/resources/webroot";
    private static SimpleWebServer instance;
    private static boolean running;
    private static IoCContainer iocContainer;

    private SimpleWebServer() throws IOException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = new ServerSocket(PORT);
        running = true;

        try {
            iocContainer = IoCContainer.getInstance("arep.lab3"); // Reemplaza con tu paquete
        } catch (Exception e) {
            throw new IOException("Failed to initialize IoC container", e);
        }

        while (true) {
            Socket clientSocket = serverSocket.accept();
            threadPool.submit(new ClientHandler(clientSocket));
        }
    }

    public static SimpleWebServer getInstance() throws IOException {
        if (instance == null) {
            instance = new SimpleWebServer();
        }
        return instance;
    }

    public static boolean isRunning() {
        return running;
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream())) {

            String requestLine = in.readLine();
            if (requestLine == null) return;

            String[] tokens = requestLine.split(" ");
            if (tokens.length < 2) return;  // Validación adicional
            String method = tokens[0];
            String fileRequested = tokens[1];

            if (!isAppRoute(fileRequested)) {
                getFile(fileRequested, out, dataOut);
            } else {
                handleAppRoute(method, fileRequested, out);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isSportRoute(String fileRequested) {
        String[] splitRequest = fileRequested.split("\\?");
        return Objects.equals(splitRequest[0], "/App/sport");
    }

    private boolean isAppRoute(String fileRequested) {
        String[] splitRequest = fileRequested.split("/");
        return splitRequest.length > 1 && Objects.equals(splitRequest[1], "App");
    }


    private void handleAppRoute(String method, String fileRequested, PrintWriter out) throws IOException {
        Service serve = LambdaServer.findHandler(method, fileRequested);
        if (serve != null) {
            String response = serve.getValue(fileRequested);
            sendHttpResponse(out, "200 OK", "text/html", response);
        }
    }

    private void getFile(String fileRequested, PrintWriter out, BufferedOutputStream dataOut) throws IOException {
        String content = getContentType(fileRequested);
        File file = new File(SimpleWebServer.WEB_ROOT, fileRequested);
        int fileLength = (int) file.length();

        if (file.exists()) {
            byte[] fileData = readFileData(file, fileLength);
            sendHttpResponse(out, "200 OK", content, fileLength, dataOut, fileData);
        } else {
            sendHttpResponse(out, "404 Not Found", "text/html", "<html><body><h1>File Not Found</h1></body></html>");
        }
    }


    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".html")) {
            SimpleWebServer.WEB_ROOT = "src/main/resources/webroot/templates";
            return "text/html";
        }
        else if (fileRequested.endsWith(".css")) {
            SimpleWebServer.WEB_ROOT = "src/main/resources/webroot/static/css";
            return "text/css";
        }
        else if (fileRequested.endsWith(".js")) {
            SimpleWebServer.WEB_ROOT = "src/main/resources/webroot/static/js";
            return "application/javascript";
        }
        else if (fileRequested.endsWith(".png")) {
            SimpleWebServer.WEB_ROOT = "src/main/resources/webroot/static/image";
            return "image/png";
        }
        else if (fileRequested.endsWith(".jpg")) {
            SimpleWebServer.WEB_ROOT = "src/main/resources/webroot/static/image";
            return "image/jpeg";
        }
        else if (fileRequested.endsWith(".json")) {
            SimpleWebServer.WEB_ROOT = "src/main/resources/webroot/static/json";
            return "application/json";
        }
        return "text/plain";
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null) fileIn.close();
        }
        return fileData;
    }

    private void sendHttpResponse(PrintWriter out, String status, String contentType, String content) {
        out.println("HTTP/1.1 " + status);
        out.println("Content-type: " + contentType);
        out.println();
        out.println(content);
        out.flush();
    }

    private void sendHttpResponse(PrintWriter out, String status, String contentType, int contentLength, BufferedOutputStream dataOut, byte[] fileData) throws IOException {
        out.println("HTTP/1.1 " + status);
        out.println("Content-type: " + contentType);
        out.println("Content-length: " + contentLength);
        out.println();
        out.flush();
        dataOut.write(fileData, 0, contentLength);
        dataOut.flush();
    }
}
