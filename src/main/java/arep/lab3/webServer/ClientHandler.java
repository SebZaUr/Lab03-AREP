package arep.lab3.webServer;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.util.*;

import arep.lab3.springECI.IoCContainer;
import arep.lab3.springECI.Annotations.*;


/**
 * The {@code ClientHandler} class is responsible for handling HTTP requests from clients. It reads the incoming requests,
 * determines if the request is for a file or a dynamic handler, processes the request accordingly, and sends back the
 * appropriate HTTP response.
 * It manages different types of requests, including static file requests and dynamic content requests, by interacting
 * with an Inversion of Control (IoC) container to invoke the appropriate controller methods.
 *
 * @author Sebastian Zamora Urrego
 * @version 2.0
 */
public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Constructs a {@code ClientHandler} instance with the specified socket, input, and output streams.
     *
     * @param socket the socket for communication with the client.
     * @param in the input stream to read requests from the client.
     * @param out the output stream to send responses to the client.
     */
    public ClientHandler(Socket socket, BufferedReader in, PrintWriter out) {
        this.clientSocket = socket;
        this.in = in;
        this.out = out;
    }

    /**
     * Handles the client request by reading the request line, determining if it is for a file or a dynamic handler,
     * and delegating the processing to the appropriate method.
     */
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String requestLine = in.readLine();
            if (requestLine == null) return;

            String[] tokens = requestLine.split(" ");
            if (tokens.length < 2) return;
            String fileRequested = tokens[1];
            if (fileRequested.split("\\.").length > 1) {
                getFile(fileRequested, out, new BufferedOutputStream(clientSocket.getOutputStream()));
            } else {
                handler(fileRequested, out);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the request for a dynamic content or file.
     *
     * @param fileRequested the requested file or handler.
     * @param out the output stream to send the response.
     * @throws Exception if an error occurs while processing the request.
     */
    private void handler(String fileRequested, PrintWriter out) throws Exception {
        if(fileRequested.equals("/")) {
            String response = getResponse(fileRequested);
            sendHttpResponse(out, "200 OK", "text/html", response);
        }
        else handlerElectives(fileRequested, out);
    }

    /**
     * Handles requests for elective content based on the requested file.
     *
     * @param fileRequested the requested file or handler.
     * @param out the output stream to send the response.
     * @throws Exception if an error occurs while processing the request.
     */
    private void handlerElectives(String fileRequested, PrintWriter out) throws Exception {
        if(fileRequested.split("\\?").length > 1){
            String response = getResponse(fileRequested);
            getFile(response, out, new BufferedOutputStream(clientSocket.getOutputStream()));
        } else {
            getFile(fileRequested + ".html", out, new BufferedOutputStream(clientSocket.getOutputStream()));
        }
    }

    /**
     * Retrieves the response for a dynamic request by invoking the appropriate controller method.
     *
     * @param fileRequested the requested file or handler.
     * @return the response content as a {@code String}.
     * @throws Exception if an error occurs while invoking the controller method.
     */
    private String getResponse(String fileRequested) throws Exception {
        List<Object> listParam = new ArrayList<>();
        String response = "";
        Method method = IoCContainer.getInstance(SimpleWebServer.filesUrl).getHandler(fileRequested.split("\\?")[0]);
        if (method != null) {
            Object controllerInstance = IoCContainer.getInstance(SimpleWebServer.filesUrl).getController(method.getDeclaringClass().getName());
            IoCContainer ioCContainer = IoCContainer.getInstance(SimpleWebServer.filesUrl);
            for (Parameter parameter : method.getParameters()) {
                if (parameter.isAnnotationPresent(RequestParam.class)) {
                    RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                    String paramName = requestParam.value();
                    String paramValue = getParameterValueFromRequest(fileRequested, paramName);
                    if (paramValue == null) paramValue = requestParam.defaultValue();
                    listParam.add(ioCContainer.convertToRequiredType(parameter.getType(), paramValue));
                }
            }

            if (listParam.isEmpty()) {
                response = (String) method.invoke(controllerInstance);
            } else {
                response = (String) method.invoke(controllerInstance, listParam.toArray());
            }
        }
        return response;
    }

    /**
     * Extracts the value of a specified parameter from the requested file string.
     *
     * @param fileRequested the requested file or handler.
     * @param paramName the name of the parameter to retrieve.
     * @return the parameter value as a {@code String}, or {@code null} if not found.
     */
    public String getParameterValueFromRequest(String fileRequested, String paramName) {
        try {
            String[] parts = fileRequested.split("\\?");
            if (parts.length > 1) {
                String[] params = parts[1].split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue[0].equals(paramName)) {
                        return keyValue[1];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sends a file to the client.
     *
     * @param fileRequested the requested file.
     * @param out the output stream to send the response.
     * @param dataOut the output stream to send the file data.
     * @throws IOException if an error occurs while reading or sending the file.
     */
    private void getFile(String fileRequested, PrintWriter out, BufferedOutputStream dataOut) throws IOException {
        String content = getContentType(fileRequested);
        File file = new File(SimpleWebServer.WEB_ROOT, fileRequested);
        int fileLength = (int) file.length();

        if (file.exists()) {
            byte[] fileData = readFileData(file, fileLength);
            sendHttpResponse(out, "200 OK", content, fileLength, dataOut, fileData);
        } else {
            sendHttpResponse(out, "404 Not Found", "text/html", "<html><body><h1>File Not Found</h1></body></html>");
        }
    }

    /**
     * Determines the content type of the requested file based on its extension.
     *
     * @param fileRequested the requested file.
     * @return the content type as a {@code String}.
     */
    public String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".html")) {
            SimpleWebServer.WEB_ROOT = "src/main/resources/webroot/templates";
            return "text/html";
        }
        else if (fileRequested.endsWith(".css")) {
            SimpleWebServer.WEB_ROOT = "src/main/resources/webroot/static/css";
            return "text/css";
        }
        else if (fileRequested.endsWith(".js")) {
            SimpleWebServer.WEB_ROOT = "src/main/resources/webroot/static/js";
            return "application/javascript";
        }
        else if (fileRequested.endsWith(".png")) {
            SimpleWebServer.WEB_ROOT = "src/main/resources/webroot/static/image";
            return "image/png";
        }
        else if (fileRequested.endsWith(".jpg")) {
            SimpleWebServer.WEB_ROOT = "src/main/resources/webroot/static/image";
            return "image/jpeg";
        }
        else if (fileRequested.endsWith(".json")) {
            SimpleWebServer.WEB_ROOT = "src/main/resources/webroot/static/json";
            return "application/json";
        }
        return "text/plain";
    }

    /**
     * Reads the data from the specified file into a byte array.
     *
     * @param file the file to read.
     * @param fileLength the length of the file in bytes.
     * @return a byte array containing the file data.
     * @throws IOException if an error occurs while reading the file.
     */
    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null) fileIn.close();
        }
        return fileData;
    }

    /**
     * Sends an HTTP response with the specified status, content type, and content.
     *
     * @param out the output stream to send the response.
     * @param status the HTTP status code and message.
     * @param contentType the content type of the response.
     * @param content the response content as a {@code String}.
     */
    public void sendHttpResponse(PrintWriter out, String status, String contentType, String content) {
        out.println("HTTP/1.1 " + status);
        out.println("Content-type: " + contentType);
        out.println();
        out.println(content);
        out.flush();
    }

    /**
     * Sends an HTTP response with the specified status, content type, content length, and file data.
     *
     * @param out the output stream to send the response.
     * @param status the HTTP status code and message.
     * @param contentType the content type of the response.
     * @param contentLength the length of the response content.
     * @param dataOut the output stream to send the file data.
     * @param fileData the file data to send.
     * @throws IOException if an error occurs while sending the response.
     */
    private void sendHttpResponse(PrintWriter out, String status, String contentType, int contentLength, BufferedOutputStream dataOut, byte[] fileData) throws IOException {
        out.println("HTTP/1.1 " + status);
        out.println("Content-type: " + contentType);
        out.println("Content-length: " + contentLength);
        out.println();
        out.flush();
        dataOut.write(fileData, 0, contentLength);
        dataOut.flush();
    }
}