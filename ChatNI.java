package ChatSystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Class ChatNI
 *
 * This class provides the Network Interface of the ChatSystem.
 * It is responsable for listening for broadcasted messages and sending broadcast messages.
 */

public class ChatNI {
    private DatagramSocket socket;
    private Thread listenerThread;
    private ChatSystem chatSystem;
    private static ChatNI instance;

    /**
     * Returns the singleton instance of the ChatNI class
     *
     * @return the singleton instance of the ChatNI class
     */
    public static ChatNI getInstance() {
        if (instance == null) {
            synchronized (ChatNI.class) {
                if (instance == null) {
                    instance = new ChatNI();
                }
            }
        }
        return instance;
    }

    /**
     * Constructor
     *
     * Initializes the socket and starts the thread that will listen for broadcasted messages.
     */
    private ChatNI() {
        try {
            chatSystem = ChatSystem.getInstance();
            socket = new DatagramSocket(ChatSystem.PORT, InetAddress.getByName("0.0.0.0"));

            listenerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    listenForHello();
                }
            });
            listenerThread.start();
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     *Listens for "hello" messages and sends a response message.
     *
     * Updates the list of remote users with the information from the "hello" message.
     *
     * If a "goodbye" message is received, removes the corresponding user from the list of remote users.
     */

    private void listenForHello() {
        System.out.println("Start of listen for hello");
        final byte[] buffer = new byte[1024];
        final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (!socket.isClosed()) {
            try {
                socket.receive(packet);
                final String message = new String(packet.getData(), 0, packet.getLength());
                final InetAddress address = packet.getAddress();
                final int port = packet.getPort();

                if (!message.isEmpty()) {
                    final String[] parts = message.split(":");
                    if (parts[0].equals("hello")) {
                        final String remoteNickname = parts[1];
                        System.out.println(
                                "Received hello from " + remoteNickname + " (" + address.getHostAddress() + ")");

                        // update remote users list
                        if (!RemoteUser.getRemoteUsers().contains(chatSystem.getRemoteUserByNickname(remoteNickname))) {
                            final RemoteUser remoteUser = new RemoteUser(remoteNickname, address);
                            chatSystem.updateRemoteUserList();
                        }
                         // send response
                            final String nickname = chatSystem.getUserNickname();
                            final String response = "hello:" + nickname;
                            final byte[] responseBytes = response.getBytes();
                            final DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length,
                                    address, port);
                            socket.send(responsePacket);

                        
                    } else if (parts[0].equals("goodbye")) {
                        final String remoteNickname = parts[1];
                        System.out.println(
                                "Received goodbye from " + remoteNickname + " (" + address.getHostAddress() + ")");
                        final RemoteUser remoteUser = chatSystem.getRemoteUserByNickname(remoteNickname);
                        if (remoteUser != null) {
                            RemoteUser.getRemoteUsers().removeAll(RemoteUser.getRemoteUsers(remoteUser));
                            chatSystem.updateRemoteUserList();
                        }
                    }
                }
            } catch (final IOException e) {
                if (!socket.isClosed()) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * Method sendHello
     *
     * Sends a broadcast message with the "hello" message and the local user's nickname.
     *
     * @param nickname The local user's nickname.
     */
    public void sendHello(String nickname) {
        try {
            final DatagramSocket sendSocket = new DatagramSocket();
            sendSocket.setBroadcast(true);
            final String message = "hello:" + nickname;
            final byte[] messageBytes = message.getBytes();
            final DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length,
                    InetAddress.getByName("255.255.255.255"), ChatSystem.PORT);
            sendSocket.send(packet);
            sendSocket.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method sendGoodbye
     *
     * Sends a broadcast message with the "goodbye" message and the local user's nickname.
     *
     * @param nickname The local user's nickname.
     */
    public void sendGoodbye(String nickname) {
        try {
            final DatagramSocket sendSocket = new DatagramSocket();
            sendSocket.setBroadcast(true);
            final String message = "goodbye:" + nickname;
            final byte[] messageBytes = message.getBytes();
            final DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length,
                    InetAddress.getByName("255.255.255.255"), ChatSystem.PORT);
            sendSocket.send(packet);
            sendSocket.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
