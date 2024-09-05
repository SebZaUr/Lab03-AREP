package arep.lab3;

import arep.lab3.webServer.SimpleWebServer;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

public class SpringECI {
    public static SimpleWebServer server;

    /**
     * The main method that starts the {@code SimpleWebServer} if it is not already running.
     *
     * @param args command-line arguments passed to the application.
     * @throws ClassNotFoundException if a class cannot be located.
     * @throws MalformedURLException if a URL is malformed.
     * @throws InvocationTargetException if the underlying method throws an exception.
     * @throws IllegalAccessException if access to the method is denied.
     */
    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException, InvocationTargetException, IllegalAccessException {
        try{
            if (!SimpleWebServer.isRunning())
                server = SimpleWebServer.getInstance();
                System.out.println("Inicia Servidor Web en el puerto 8080");
        }catch(Exception e){
            System.err.println("Error en el servidor");
            System.exit(1);
        }
    }
}