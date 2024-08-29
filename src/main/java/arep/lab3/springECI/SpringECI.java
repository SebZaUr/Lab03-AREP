package arep.lab3;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SpringECI {
    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException, InvocationTargetException, IllegalAccessException {
        Class c = Class.forName(args[0]);
        Map<String, Method> services = new HashMap<>();
        //Cargar componentes
        if (c.isAnnotationPresent(RestController.class)) {
            Method[] methods = c.getDeclaredMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(GetMapping.class)) {
                    String key = m.getAnnotation(GetMapping.class).value();
                    services.put(key, m);
                }
            }
        }
        URL servicesurl1 = new URL("http://localhost:8080/App/Hello");
        URL servicesurl2 = new URL("http://localhost:8080/App/AREP");
        URL servicesurl3 = new URL("http://localhost:8080/App/IETI");
        URL servicesurl4 = new URL("http://localhost:8080/App/ARSW");
        URL[] urls = {servicesurl1,servicesurl2,servicesurl3,servicesurl4};
        for(int i = 0; i< urls.length;i++) {
            String path = urls[i].getPath();
            System.out.println(path);
            String servicename = path.substring(4);
            System.out.println(servicename);
            Method ms = services.get(servicename);
            System.out.println(ms.invoke(null, null));
        }
    }
}