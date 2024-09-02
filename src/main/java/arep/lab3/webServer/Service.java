package arep.lab3.webServer;

import java.io.IOException;

@FunctionalInterface
public interface Service {
    public String getValue(String request) throws IOException;
}
