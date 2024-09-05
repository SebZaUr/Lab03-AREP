package arep.lab3.springECI;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import arep.lab3.springECI.Annotations.*;

/**
 * The {@code IoCContainer} class is an implementation of an Inversion of Control (IoC) container that manages the
 * creation and handling of REST controllers and their endpoints.
 *
 * This class scans a specified package for classes annotated with {@code @RestController} and registers the methods
 * annotated with {@code @GetMapping} and {@code @RequestMapping} to map HTTP requests to the appropriate handler methods.
 * It also provides utility methods to handle requests, obtain controllers, and convert parameter types.
 *
 * @author Sebastian Zamora Urrego
 * @version 1.0
 */
public class IoCContainer {

    private Map<String, Method> endpointMappings = new HashMap<>();
    private Map<String, Object> controllerInstances = new HashMap<>();
    private static IoCContainer instance ;

    /**
     * Private constructor to initialize the IoC container with the specified package name.
     *
     * @param packageName The package name to scan for components.
     * @throws Exception if an error occurs while scanning for components or initializing controllers.
     */
    private IoCContainer(String packageName) throws Exception {
        scanForComponents(packageName);
    }

    /**
     * Scans the specified package for classes annotated with {@code @RestController} and registers their methods
     * annotated with {@code @GetMapping} and {@code @RequestMapping}.
     *
     * @param packageName The package name to scan for components.
     * @throws Exception if an error occurs while scanning for components or registering methods.
     */
    private void scanForComponents(String packageName) throws Exception {
        Class<?>[] classes = ClassFinder.findClassesInPackage(packageName);
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(RestController.class)) {
                for (Method method : clazz.getDeclaredMethods()) {
                    Object controllerInstance = clazz.getDeclaredConstructor().newInstance();
                    controllerInstances.put(clazz.getName(), controllerInstance);
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        GetMapping getMapping = method.getAnnotation(GetMapping.class);
                        endpointMappings.put(getMapping.value(), method);
                        System.out.println("GET mapping: " + getMapping.value());
                    }else if(method.isAnnotationPresent(RequestMapping.class)){
                        RequestMapping getMapping = method.getAnnotation(RequestMapping.class);
                        endpointMappings.put(getMapping.value(), method);
                        System.out.println("Request mapping: " + getMapping.value());
                    }
                }
            }
        }
    }


    /**
     * Retrieves the handler method for the given path.
     *
     * @param path The path for which to retrieve the handler method.
     * @return The handler {@code Method} associated with the specified path, or {@code null} if not found.
     */
    public Method getHandler(String path) {
        return endpointMappings.get(path);
    }

    /**
     * Returns the singleton instance of the IoC container. If the instance does not exist, it creates one and initializes it.
     *
     * @param packageName The package name to scan for components.
     * @return The singleton instance of the {@code IoCContainer}.
     * @throws Exception if an error occurs while creating or initializing the instance.
     */
    public static IoCContainer getInstance(String packageName) throws Exception {
        if(instance == null){
            instance = new IoCContainer(packageName);
        }
        return instance;
    }

    /**
     * Retrieves the controller instance for the given class name.
     *
     * @param path The class name of the controller.
     * @return The controller instance associated with the specified class name.
     */
    public Object getController(String path){
        return controllerInstances.get(path);
    }

    /**
     * Converts a string value to the required type based on the parameter type.
     *
     * @param parameterType The required parameter type.
     * @param value The string value to convert.
     * @return The converted object of the required type, or {@code null} if conversion is not possible.
     */
    public Object convertToRequiredType(Class<?> parameterType, String value) {
        if (parameterType == String.class) {
            return value;
        } else if (parameterType == int.class || parameterType == Integer.class) {
            return Integer.parseInt(value);
        } else if (parameterType == double.class || parameterType == Double.class) {
            return Double.parseDouble(value);
        } else if (parameterType == boolean.class || parameterType == Boolean.class) {
            return Boolean.parseBoolean(value);
        }
        return null;
    }
}

/**
 * Utility class {@code ClassFinder} for finding classes in a specified package.
 */
class ClassFinder {

    /**
     * Finds all classes in the specified package.
     *
     * @param packageName The package name to search for classes.
     * @return An array of {@code Class} objects representing the classes in the specified package.
     * @throws Exception if an error occurs while finding the classes.
     */
    public static Class<?>[] findClassesInPackage(String packageName) throws Exception {
        String path = packageName.replace('.', '/');
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        File directory = new File(url.toURI());
        List<Class<?>> classes = new ArrayList<>();
        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replace(".class", "");
                classes.add(Class.forName(className));
            }
        }
        return classes.toArray(new Class[0]);
    }
}

