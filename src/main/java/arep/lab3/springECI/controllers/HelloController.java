package arep.lab3.springECI.controllers;

import arep.lab3.springECI.Annotations.*;

@RestController
public class HelloController {

    /**
     * Handles requests to the root endpoint ("/") and returns a greeting message.
     *
     * @return A {@code String} containing an HTML greeting message.
     */
    @RequestMapping("/")
    public String index(){
        return "<h1>Greetings from Spring Boot!</h1>";
    }

    /**
     * Handles GET requests to the "/greeting" endpoint and returns a personalized greeting message.
     *
     * @param name The name to include in the greeting. If not provided, defaults to "World".
     * @return A {@code String} containing an HTML personalized greeting message.
     */
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "<h2>Hola " + name+"</h2>";
    }
}
