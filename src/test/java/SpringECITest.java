import arep.lab3.springECI.controllers.ElectivasController;
import arep.lab3.webServer.ClientHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SpringECITest {

    @Mock
    private Socket mockSocket;

    @Mock
    private BufferedReader mockBufferedReader;

    @Mock
    private PrintWriter mockPrintWriter;

    private ClientHandler clientHandler;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        // Configura el comportamiento esperado de los mocks
        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("GET /index.html HTTP/1.1".getBytes()));
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        when(mockBufferedReader.readLine()).thenReturn("GET /index.html HTTP/1.1", null);

        // Inicializa ClientHandler con los mocks
        clientHandler = new ClientHandler(mockSocket, mockBufferedReader, mockPrintWriter);
    }

    @Test
    public void testGetElectives() {
        ElectivasController controller = new ElectivasController();
        String result = controller.getElectives();
        assertEquals("electivas.html", result);
    }

    @Test
    public void testGetClassesWithCarrer() {
        ElectivasController controller = new ElectivasController();
        String carrer = "computerScience";
        String result = controller.getClasses(carrer);
        assertEquals(carrer + ".json", result);
    }

    @Test
    public void testGetClassesWithoutCarrer() {
        ElectivasController controller = new ElectivasController();
        String result = controller.getClasses("");
        assertEquals(".json", result);
    }
}
