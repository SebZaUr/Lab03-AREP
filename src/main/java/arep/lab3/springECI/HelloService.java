package arep.lab3;

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
}
