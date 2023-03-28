package ChatSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements Runnable {
    private final Socket clientSocket;

    public TCPServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = inputReader.readLine()) != null) {
                System.out.println("Received message from client: " + inputLine);

       

                if (inputLine.equals("exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling TCP connection: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try (
            ServerSocket serverSocket = new ServerSocket(ChatSystem.PORT);
        ) {
            System.out.println("TCP server started on port " + ChatSystem.PORT);

            while (true) {
                System.out.println("Waiting for a client to connect...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                TCPServer tcpServer = new TCPServer(clientSocket);
                Thread tcpServerThread = new Thread(tcpServer);
                tcpServerThread.start();
            }
        } catch (IOException e) {
            System.err.println("Error starting TCP server: " + e.getMessage());
        }
    }
}
