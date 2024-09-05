package arep.lab3.springECI;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * The {@code Annotations} class defines custom annotations used for building a lightweight
 * web framework. These annotations are used to mark classes and methods as REST controllers,
 * map HTTP requests to methods, and manage request parameters.
 * @author Sebastian Zamora Urrego
 * @version 1.0
 */
public class Annotations {

    /**
     * The {@code RequestMapping} annotation is used to map HTTP requests to specific handler
     * methods for any HTTP method. It is typically used to define a base URI for a controller class
     * or to specify a path for a specific method.
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RestController {
    }

    /**
     * The {@code RequestMapping} annotation is used to map HTTP requests to specific handler
     * methods for any HTTP method. It is typically used to define a base URI for a controller class
     * or to specify a path for a specific method.
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequestMapping {

        public String value();
    }

    /**
     * The {@code GetMapping} annotation is used to map HTTP GET requests to specific handler methods.
     * It provides a more specific mapping for GET requests compared to {@code @RequestMapping}.
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface GetMapping {
        public String value();
    }

    /**
     * The {@code RequestParam} annotation is used to bind HTTP request parameters to method parameters.
     * It can be used to specify a parameter name and a default value if the parameter is not present in the request.
     */
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequestParam {
        public String value();

        String defaultValue();
    }
}
