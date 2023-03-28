package ChatSystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class InterfaceWithUser extends WelcomeInterface implements ActionListener {
    private RemoteUser selectedUser;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;


    public InterfaceWithUser(RemoteUser XselectedUser) {
        this.selectedUser = XselectedUser;
        initInterface();
        initSocket();
        getChatArea().setText("Start of the chat with "+selectedUser.getNickname()+"\n");
    }

    private void initInterface() {

        setTitle("You ("+ ChatSystem.getUserNickname()+") chat with " + selectedUser.getNickname());
        setSize(1000, 1000);
        super.getSendButton().setEnabled(true);
        super.getDownloadButton().setEnabled(true);   
        super.getMessageField().setEnabled(true);
    }

    private void initSocket() {
        try {

            socket = new Socket(selectedUser.getAddress(), ChatSystem.PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));;
            new Thread(new IncomingReader()).start();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getSendButton()) {
            sendMessage();
        } else if (e.getSource() == getLogoutButton()) {
            disconnection();
        } else if (e.getSource() == getRemoteUserJList()) {
            changeUser();
        }
    }
    private void sendMessage() {
        getChatArea().append("You: "+getMessageField().getText());
        try {
            out.write(getMessageField().getText());
            getMessageField().setText("");
            out.newLine();
            out.flush();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "IO error. See log.");
            e.printStackTrace();
        }
    }

    @Override
    public void changeUser() {
        // Get selected remote user from list

        final RemoteUser selectedUser = ChatSystem.getRemoteUserByNickname(getRemoteUserJList().getSelectedValue(), getRemoteUserList());
        if (selectedUser != null) {
            // Open new window to chat with selected remote user
            closeSocket();
            final InterfaceWithUser interfaceWithUser = new InterfaceWithUser(selectedUser);
            interfaceWithUser.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Remote user uknow");
        }

    }
    private void closeSocket() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void disconnection() {
        closeSocket();
        this.dispose();
        ConnectionInterface connectionInterface = new ConnectionInterface();
        connectionInterface.setVisible(true);
    }

    private class IncomingReader implements Runnable {
        /**
         *  A private inner class that implements the Runnable interface and is responsible for     
         *  continuously reading incoming messages from the server and displaying them on the chat area.
        */
        public void run() {
            /**
             * Continuously reads incoming messages from the server and displays them on the chat area.
            */
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    getChatArea().append(selectedUser.getNickname() + ": " + message + "\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
