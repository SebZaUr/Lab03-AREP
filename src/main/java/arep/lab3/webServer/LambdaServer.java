package arep.lab3.webServer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class LambdaServer {
    public static Map<String, Service> getRoutes = new HashMap<>();
    public static Map<String, Service> postRoutes = new HashMap<>();
    public static String filesPath = "";
    public static LambdaServer instance;

    private LambdaServer(){}

    public static LambdaServer getInstance(){
        if(instance == null) {
            instance =  new LambdaServer();
        }
        return  instance;
    }

    public static void get(String url, Service s){
        getRoutes.put(url, s);
    }

    public static void post(String url, Service s){
        postRoutes.put(url,s);
    }

    public static void staticfiles(String folder){
        filesPath = "target/classes" + folder;
    }

    public static String serverStaticFile(String path){
        File file = new File(filesPath + path);
        if (file.exists()) {
            try {
                return new String(Files.readAllBytes(Paths.get(file.getPath())));
            } catch (IOException e) {
                return "Error reading file: " + e.getMessage();
            }
        } else {
            return "File not found: " + path;
        }
    }

    public static Service findHandler(String method, String path){
        if ("GET".equalsIgnoreCase(method)) {
            return getRoutes.get(path.split("\\?")[0]);
        } else if ("POST".equalsIgnoreCase(method)) {
            return postRoutes.get(path);
        } else {
            return null;
        }
    }
}
