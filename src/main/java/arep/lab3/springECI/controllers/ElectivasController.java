package arep.lab3.springECI.controllers;

import arep.lab3.springECI.Annotations.*;

/**
 * The {@code ElectivasController} class serves as a REST controller for handling requests related to electives.
 *
 * This class defines endpoints to manage and retrieve information about electives offered in different programs.
 * It uses custom annotations such as {@code @RestController}, {@code @RequestMapping}, and {@code @GetMapping} to
 * map HTTP requests to handler methods.
 *
 * Handler methods in this class are responsible for rendering HTML content and returning JSON data.
 *
 * @author Sebastian Zamora Urrego
 * @version 1.0
 **/
@RestController
public class ElectivasController {

    /**
     * Handles GET requests to the "/electivas" endpoint and returns an HTML page with information about electives.
     *
     * @return A {@code String} representing the name of the HTML file to be served.
     */
    @RequestMapping("/electivas")
    public String getElectives(){
        return "electivas.html";
    }

    /**
     * Handles GET requests to the "/electivas" endpoint with a query parameter specifying a career.
     * Returns a JSON file containing elective classes for the specified career.
     *
     * @param carrer The career for which the elective classes are to be retrieved. If not provided, it defaults to an empty string.
     * @return A {@code String} representing the name of the JSON file containing elective classes for the specified career.
     */
    @GetMapping("/electivas")
    public String getClasses(@RequestParam(value = "carrer", defaultValue = "") String carrer){
        return carrer + ".json";
    }

}
