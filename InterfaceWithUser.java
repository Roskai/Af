package ChatSystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class InterfaceWithUser extends WelcomeInterface implements ActionListener {
    private final ChatSystem chatSystem = ChatSystem.getInstance();
    private final RemoteUser selectedUser;
    private static Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public InterfaceWithUser(RemoteUser XselectedUser) {
        this.selectedUser = XselectedUser;
        initInterface();
        initSocket();
        getChatArea().setText("Start of the chat with " + selectedUser.getNickname() + "\n");
    }

    private void initInterface() {

        setTitle("You (" + chatSystem.getUserNickname() + ") chat with " + selectedUser.getNickname());
        setSize(1000, 1000);
        super.getSendButton().setEnabled(true);
        super.getDownloadButton().setEnabled(true);
        super.getMessageField().setEnabled(true);
        super.getRemoteUserJList().setEnabled(false);
        super.getCloseConvButton().setEnabled(true);

        getDownloadButton().addActionListener(this);
        getCloseConvButton().addActionListener(this);
    }

    private void initSocket() {
        try {

            socket = new Socket(selectedUser.getAddress(), ChatSystem.PORT);
            System.out.println("Connecté à "+selectedUser.getAddress());
            if (socket.isConnected()) {
                // la socket est connectée au serveur, on peut lire les données
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("socket"+socket);
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                // lire les données depuis le flux d'entrée
            } else {
                // la socket n'est pas connectée au serveur
                System.out.println("La socket n'est pas connectée au serveur");
            }

            

        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        new Thread(new IncomingReader()).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getSendButton()) {
            sendMessage();
        } else if (e.getSource() == getLogoutButton()) {
            disconnection();
        } else if (e.getSource() == getCloseConvButton()) {
            closeConverstation();
        } else if (e.getSource() == getDownloadButton()) {
            System.out.println("TODO");
        }
    }

    private void closeConverstation() {
        final String messageFermeture = chatSystem.getUserNickname() + "has left the chat.\n";
        getChatArea().append(messageFermeture);
        try {
            out.write(messageFermeture);
            System.out.println("Message fermé");
            out.newLine();
            out.flush();
           
        } catch (final IOException e) {
            e.printStackTrace();
        }
        try {
            closeSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new WelcomeInterface();
        this.dispose();

    }

    private void sendMessage() {
        getChatArea().append("You: " + getMessageField().getText() + "\n");
        try {
            out.write(getMessageField().getText());
            getMessageField().setText("");
            out.newLine();
            out.flush();

        } catch (final IOException e) {
            JOptionPane.showMessageDialog(this, "IO error. See log.");
            e.printStackTrace();
        }
    }


    @Override
    public void changeUser() {
        // Get selected remote user from list
        final RemoteUser selectedUser = chatSystem.getRemoteUserByNickname(getRemoteUserJList().getSelectedValue());
        if (selectedUser != null) {
            // Open new window to chat with selected remote user
            try {
                closeSocket();
            } catch (final IOException e) {
                e.printStackTrace();
            }
            final InterfaceWithUser interfaceWithUser = new InterfaceWithUser(selectedUser);
            interfaceWithUser.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Remote user unknown");
        }
    }

    private void closeSocket() throws IOException {
        if (socket != null) {
            try {
                socket.close();
            } catch (final IOException ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (final IOException ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (final IOException ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
    }

    @Override
    public void disconnection() {
        try {
            closeSocket();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Close ChatInterface window and return to ChatDialog
        chatSystem.sendGoodbye(chatSystem.getUserNickname());
        this.dispose();
        final ConnectionInterface connectionInterface = new ConnectionInterface();
        connectionInterface.setVisible(true);
    }

    private class IncomingReader implements Runnable {
        /**
         *  A private inner class that implements the Runnable interface and is responsible for
         *  continuously reading incoming messages from the server and displaying them on the chat area.
         */


        @Override
        public void run() {
            /**
             * Continuously reads incoming messages from the server and displays them on the chat area.
             */
            try {
                System.out.println("In run");
                while (true ) {
                    System.out.println("In run while");
                    System.out.println(in);
                    System.out.println(in.read());
                    String message = in.readLine();
                    System.out.println(message);
                        // The message contains a regular chat message
                        getChatArea().append(selectedUser.getNickname() + ": " + message + "\n");
                    }
            }catch (final IOException ex) {
                if (!socket.isClosed()) {
                    // An error occurred while reading data
                    ex.printStackTrace();
                }
            } finally {
                try {
                    // Close the socket and streams
                    closeSocket();
                } catch (final IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * @return the socket
     */
    public static Socket getSocket() {
        return socket;
    }

    /**
     * @param socket the socket to set
     */
    public static void setSocket(Socket socket) {
        InterfaceWithUser.socket = socket;
    }

}
