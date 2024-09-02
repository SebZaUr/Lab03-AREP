package arep.lab3.springECI;

@RestController
public class HelloService {
    @GetMapping("/Hello")
    public static String hello() {
        return "Hello World";
    }

    @GetMapping("/AREP")
    public static String arep() {
        return "Hello World";
    }

    @GetMapping("/IETI")
    public static String ieti() {
        return "Hello World";
    }

    @GetMapping("/ARSW")
    public static String arsw() {
        return "Hello World";
    }

    @RequestMapping("/")
    public String index(){
        return "<h1>Greetings from Spring Boot!</h1>";
    }
}
