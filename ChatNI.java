/**
 * 
 */
package ChatSystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ChatNI {
    private DatagramSocket socket;
    private Thread listenerThread;


    public ChatNI() {
        try {
            socket = new DatagramSocket(ChatSystem.PORT, InetAddress.getByName("0.0.0.0"));
            listenerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    listenForHello();
                }
            });
            listenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TCPServer.main(null);
    }

    private void listenForHello() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (!socket.isClosed()) {
            try {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                if (!message.isEmpty()) {
                    String[] parts = message.split(":");
                    if (parts[0].equals("hello")) {
                        String remoteNickname = parts[1];
                        System.out.println("Received hello from " + remoteNickname + " (" + address.getHostAddress() + ")");
                        // send response
                        String nickname = ChatSystem.getUserNickname();
                        String response = "hello:" + nickname;
                        byte[] responseBytes = response.getBytes();
                        DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length, address, port);
                        socket.send(responsePacket);
                        // update remote users list
                        RemoteUser remoteUser = new RemoteUser(remoteNickname, address);
                        RemoteUser.getRemoteUsers().add(remoteUser);
                        WelcomeInterface.addRemoteUserToListModel(remoteUser);
                    }
                }
            } catch (IOException e) {
                if (!socket.isClosed()) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendHello(String nickname) {
        try {
            DatagramSocket sendSocket = new DatagramSocket();
            sendSocket.setBroadcast(true);
            String message = "hello:" + nickname;
            byte[] messageBytes = message.getBytes();
            DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, InetAddress.getByName("255.255.255.255"), ChatSystem.PORT);
            sendSocket.send(packet);
            sendSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

