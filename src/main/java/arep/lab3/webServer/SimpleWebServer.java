package arep.lab3.webServer;

import arep.lab3.springECI.IoCContainer;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * The {@code SimpleWebServer} class represents a basic web server that listens for HTTP requests on a specific port,
 * handles multiple connections concurrently, and uses an Inversion of Control (IoC) container to manage request handlers.
 *
 * @author Sebastian Zamora Urrego
 * @version 2.0
 */
public class SimpleWebServer {
    private static final int PORT = 8080;
    public static String WEB_ROOT = "src/main/resources/webroot";
    private static SimpleWebServer instance;
    private static boolean running = false;
    private static IoCContainer iocContainer;
    public static final String filesUrl = "arep.lab3.springECI.controllers";

    /**
     * Private constructor of the Web Server.
     *
     * @throws IOException if an error occurs while initializing the server or the IoC container.
     */
    private SimpleWebServer() throws IOException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = new ServerSocket(PORT);
        running = true;

        try {
            iocContainer = IoCContainer.getInstance(filesUrl);
        } catch (Exception e) {
            throw new IOException("Failed to initialize IoC container", e);
        }

        while (true) {
            Socket clientSocket = serverSocket.accept();
            threadPool.submit(new ClientHandler(clientSocket, new BufferedReader(new InputStreamReader(clientSocket.getInputStream())), new PrintWriter(clientSocket.getOutputStream(), true)));
        }
    }

    /**
     * Retrieves the unique instance of the web server.
     *
     * @return the unique instance of the web server.
     * @throws IOException if an error occurs while initializing the server.
     */
    public static SimpleWebServer getInstance() throws IOException {
        if (instance == null) {
            instance = new SimpleWebServer();
        }
        return instance;
    }

    /**
     * Checks if the web server is running.
     *
     * @return {@code true} if the server is running, {@code false} otherwise.
     */
    public static boolean isRunning() {
        return running;
    }
}
