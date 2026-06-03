import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Simple TCP chat client.
 * Start the server first (TCPServer), then run this.
 */
public class TCPClient {
    private static final String HOST = "localhost";
    private static final int    PORT = 9090;

    public static void main(String[] args) throws IOException {
        System.out.println("Connecting to server at " + HOST + ":" + PORT + "...");

        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Connected.");

            BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter    out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            // Thread to read messages from server
            Thread reader = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println("Server: " + msg);
                        if (msg.equalsIgnoreCase("bye")) break;
                    }
                } catch (IOException e) {
                    System.out.println("Server closed the connection.");
                }
            });
            reader.start();

            System.out.println("Type messages (press Enter to send, 'bye' to quit):");
            String line;
            while ((line = console.readLine()) != null) {
                out.println(line);
                if (line.equalsIgnoreCase("bye")) break;
            }

            reader.join();
            System.out.println("Client disconnecting.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
