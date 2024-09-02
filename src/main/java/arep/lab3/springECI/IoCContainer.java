package arep.lab3.springECI;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class IoCContainer {

    private Map<String, Method> endpointMappings = new HashMap<>();
    private static IoCContainer instance ;

    private IoCContainer(String packageName) throws Exception {
        scanForComponents(packageName);
    }

    private void scanForComponents(String packageName) throws Exception {
        Class<?> clazz = Class.forName("com.example.GreetingController");
        if (clazz.isAnnotationPresent(RestController.class)) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    GetMapping getMapping = method.getAnnotation(GetMapping.class);
                    endpointMappings.put(getMapping.value(), method);
                }
            }
        }
    }

    public Method getHandler(String path) {
        return endpointMappings.get(path);
    }

    public static IoCContainer getInstance(String packageName) throws Exception {
        if(instance == null){
            instance = new IoCContainer(packageName);
        }
        return instance;
    }
}

