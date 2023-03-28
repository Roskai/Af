package ChatSystem;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {


    public static void main(String[] args) throws IOException, InterruptedException {
        // creates of a server socket and binds it to the port number ChatSystem.PORT:
        final ServerSocket serverSocket = new ServerSocket(ChatSystem.PORT);

        //Once a ServerSocket instance is created, call accept() to start listening for incoming client requests:
        final Socket clientSocket = serverSocket.accept();

        // Initial message of our Server 
        System.out.println("Client Connected");

        // Use to read the data send from the client
        final DataInputStream serverDataInputStream = new DataInputStream(clientSocket.getInputStream());

        try {

            while (true) {
                //Read message from the client socket 
                final String clientMessage = serverDataInputStream.readUTF();
                System.out.println("client says: " + clientMessage);

                // breaking the infinite loop
                if (clientMessage.equalsIgnoreCase("exit")) {
                    break;
                }
            }

        } catch (final IOException e) {
            // Exceptions to be handle
            System.out.println(e);
        } finally {
            // Closing the socket. 
            serverSocket.close();
        }
    }
}
