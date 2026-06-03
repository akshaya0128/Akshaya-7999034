import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Simple TCP chat server.
 * Run in one terminal:  javac TCPServer.java && java TCPServer
 * Run client in another: javac TCPClient.java && java TCPClient
 */
public class TCPServer {
    private static final int PORT = 9090;

    public static void main(String[] args) throws IOException {
        System.out.println("Server listening on port " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT);
             Socket clientSocket = serverSocket.accept()) {

            System.out.println("Client connected: " + clientSocket.getInetAddress());

            BufferedReader in  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter    out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            // Thread to read messages from client and print them
            Thread reader = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println("Client: " + msg);
                        if (msg.equalsIgnoreCase("bye")) break;
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });
            reader.start();

            // Main thread reads from console and sends to client
            System.out.println("Type messages (press Enter to send, 'bye' to quit):");
            String line;
            while ((line = console.readLine()) != null) {
                out.println(line);
                if (line.equalsIgnoreCase("bye")) break;
            }

            reader.join();
            System.out.println("Server shutting down.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
