# Laboratory 03 AREP 2024-2: TALLER DE ARQUITECTURAS DE SERVIDORES DE APLICACIONES, META PROTOCOLOS DE OBJETOS, PATRÓN IOC, REFLEXIÓN

## University Electives Consultant

***

### Description

This project has two parts: One is a RESTful API in Java that allows querying the elective courses available for university programs. The API provides endpoints to obtain detailed information about electives, including name, code, credits, type, prerequisites, and the academic program to which they belong.  
The other is a web application made with JavaScript that allows me to filter the API information by different academic programs.

### Features

* Elective Course Query: Get the complete list of available elective courses.
* Course Details: Query specific details of an elective course, including credits and prerequisites.
* Filter by Program: Filter the available elective courses according to the academic program.
* Data Format: JSON response format.

### Requirements

* Java: Version 11 or higher.
* JDK: Java Development Kit, required to compile and run the project.
* IDE: You can use any Java-compatible development environment, such as IntelliJ IDEA, Eclipse, or NetBeans.
* Browser: Any browser for open the webapp.
* Maven: For build the proyect.

> [!IMPORTANT]
> En este proyecto se utilizo la versión de Java 17 y Maven 3.9.5

### Installation

* First, download the project:

    ```bash
    git clone https://github.com/SebZaUr/Lab03-AREP.git
    ```

* Enter the project directory:

    ```bash
    cd Lab03-AREP
    ```

* Build the project using Maven:

    ```bash
    mvn clean compile
    ```

* Run this command:

    ```bash
    java -cp target/classe arep.lab3.SpringECI
    ```

The project is now running. To open it, click on this link in your browser: [ECIELECTIVAS](http://localhost:8080/electivas).

## Ejecutando las pruebas ⚙️

For run the test of the ElectivasController

```bash
mvn test
```

## Generando Javadoc 📦

For generate the project's documentation, run the following command, the Javadoc files will generate on the directory `target/site/apidocs`.

```bash
mvn site
```
---
To see the Javadoc files open the file `index.html` on the directory `target/site/` in your browser of your preferences search the **project reports** section and click on `Project Javadoc` option to see the documentation. 

---

## Application architecture 📐

This application is a web client that runs in the browser and uses JSON for message formatting. It acts as a facade server, encapsulating calls to external web services over HTTP.


The application modules are as follows:
* __SpringECI:__ Let us start the application.
* __Annotations:__ Create all the annotations that we need on the project (example: **@GetMapping**,**@RequestMapping**,**@RequestParam**).
* __IoCContainer:__ Is an implementation of an Inversion of Control (IoC) container that manages the creation and handling of REST controllers and their endpoints.
* __ClientHandler:__ Let us handing all HTTP requests from clients.
* __SimpleWebServer:__ A basic web server that listens for HTTP requests on a specific port.

---

## Author: 
Sebastian Zamora Urrego.