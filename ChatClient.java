import java.io.*;
import java.net.*;

public class ChatClient {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ChatClient(String serverAddress, int port) throws IOException {
        socket = new Socket(serverAddress, port);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String message) {
        output.println(message);
    }

    public BufferedReader getInput() {
        return input;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error closing client: " + e.getMessage());
        }
    }
}
