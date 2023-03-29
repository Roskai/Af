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
    private Socket socket;
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
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

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
            sendFile();
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

    private File selectFile() {
        final JFileChooser fileChooser = new JFileChooser();
        final int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    private void sendFile() {
        final File fileToSend = selectFile();

        try {
            // Ouverture du flux d'entrée pour lire le fichier
            final FileInputStream fileInputStream = new FileInputStream(fileToSend);

            // Envoi du préfixe "FILE:" pour signaler que c'est un fichier
            out.write("FILE:");

            // Lecture du fichier et envoi des données
            final byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                out.write(new String(buffer, 0, bytesRead));
            }

            // Fermeture du flux d'entrée
            fileInputStream.close();

            // Envoi d'une ligne vide pour signaler la fin du fichier
            out.newLine();
            out.flush();

            // Affichage d'un message de succès
            JOptionPane.showMessageDialog(this, "The file was sent successfully.");
            getChatArea().append("You have uploaded a file.");

        } catch (final IOException e) {
            // Affichage d'un message d'erreur en cas de problème
            JOptionPane.showMessageDialog(this, "An error occurred while sending the file. Please try again.");
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
        private void receiveFile() {
            /*
            try {
            // Lecture des données envoyées par le client
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = in.readLine()) != null && !line.isEmpty()) {
            stringBuilder.append(line);
            }
            byte[] data = stringBuilder.toString().getBytes();


            // Ecriture des données dans un fichier local
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);
            fileOutputStream.close();
            JOptionPane.showInputDialog(this, "Le fichier a été reçu avec succès.");
            }

            } catch (IOException e) {
            JOptionPane.showInputDialog(this, "Une erreur est survenue lors de la réception du fichier. Veuillez réessayer.");
            e.printStackTrace();
            }
            */}

        @Override
        public void run() {
            /**
             * Continuously reads incoming messages from the server and displays them on the chat area.
             */
            try {
                while (!socket.isClosed() && !socket.isInputShutdown()) {
                    final String message = in.readLine();
                    if (message == null) {
                        // The connection was closed by the server
                        break;
                    }
                    if (message.startsWith("FILE:")) {
                        // The message contains a file
                        receiveFile();
                    } else {
                        // The message contains a regular chat message
                        getChatArea().append(selectedUser.getNickname() + ": " + message + "\n");
                    }
                }
            } catch (final IOException ex) {
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

}
