package ChatSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements Runnable {
    private final Socket clientSocket;

    public ChatServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
            String inputLine;
            while ((inputLine = inputReader.readLine()) != null) {
                System.out.println("Received message from client: " + inputLine);

                if (inputLine.equals("exit")) {
                    break;
                }
            }
        } catch (final IOException e) {
            System.err.println("Error handling TCP connection: " + e.getMessage());
        } finally {
            try {
                InterfaceWithUser.getSocket().close();
            } catch (final IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(ChatSystem.PORT);) {
            System.out.println("TCP server started on port " + ChatSystem.PORT);

            while (true) {
                System.out.println("Waiting for a client to connect...");
                InterfaceWithUser.setSocket(serverSocket.accept());
                System.out.println("Client connected: " + InterfaceWithUser.getSocket().getInetAddress());
                final ChatServer tcpServer = new ChatServer(InterfaceWithUser.getSocket());
                final Thread tcpServerThread = new Thread(tcpServer);
                tcpServerThread.start();
            }
        } catch (final IOException e) {
            System.err.println("Error starting TCP server: " + e.getMessage());
        }
    }
}
