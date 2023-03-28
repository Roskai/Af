package ChatSystem;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    ArrayList<PrintWriter> clientList;

    public static void main(String[] args) {
        try {
            new ChatServer().go();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void go() throws IOException {
        clientList = new ArrayList<PrintWriter>();
        ServerSocket serverSock = new ServerSocket(ChatSystem.PORT);
        try {
            

            while (true) {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientList.add(writer);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            serverSock.close();
        }
    }

    public void tellEveryone(String message) {
        Iterator<PrintWriter> it = clientList.iterator();

        while (it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;

        public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void run() {
            String message;

            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("Received: " + message);
                    tellEveryone(message);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
