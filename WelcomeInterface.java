package ChatSystem;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class WelcomeInterface extends JFrame implements ActionListener {

    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton downloadButton;
    private JButton logoutButton;
    private JPanel userPanel;
    private JPanel remoteUserPanel;
    private JList<String> remoteUserJList;
    private final List<RemoteUser> remoteUserList;
    private static DefaultListModel<String> remoteUserListModel;

 
    private void initInterface() {

        setTitle("Chat Interface");
        setSize(1000, 1000);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(3, 2));

        setChatArea(new JTextArea());
        getChatArea().setEditable(false);
        getChatArea().setText("Welcome "+ ChatSystem.getUserNickname() +"\nPlease chose a remote user");
        final JScrollPane chatScroll = new JScrollPane(getChatArea());
        mainPanel.add(chatScroll, BorderLayout.CENTER);

        remoteUserPanel = new JPanel();
        remoteUserPanel.setLayout(new BorderLayout(3, 1));
        final JLabel remoteUserListLabel = new JLabel("Remote Users connected:");
        remoteUserPanel.add(remoteUserListLabel, BorderLayout.PAGE_START);
        remoteUserListModel = new DefaultListModel<>();
        for (final RemoteUser remoteUser : getRemoteUserList()) {
            remoteUserListModel.addElement(remoteUser.getNickname());
        }
        setRemoteUserJList(new JList<>(remoteUserListModel));
        final JScrollPane remoteUserScrollPane = new JScrollPane(getRemoteUserJList());
        remoteUserPanel.add(remoteUserScrollPane, BorderLayout.CENTER);
        setLogoutButton(new JButton("Disconnection"));
        getLogoutButton().addActionListener(this);
        getRemoteUserJList().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {if (!e.getValueIsAdjusting()) {changeUser(); }
            }
        });

        remoteUserPanel.add(getLogoutButton(), BorderLayout.PAGE_END);

        mainPanel.add(remoteUserPanel, BorderLayout.WEST);
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));

        setMessageField(new JTextField());
        getMessageField().setEnabled(false);
        buttonPanel.add(getMessageField());

        setSendButton(new JButton("Send"));
        getSendButton().addActionListener(this);
        getSendButton().setEnabled(false);
        buttonPanel.add(getSendButton());

        setDownloadButton(new JButton("Upload"));
        getDownloadButton().addActionListener(this);
        getDownloadButton().setEnabled(false);
        buttonPanel.add(getDownloadButton());
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }

    // Gérer les actions de l'utilisateur
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getLogoutButton()) {
            disconnection();
        }
    }

    public RemoteUser getRemoteUserByNickname(String nickname, List<RemoteUser> remoteUserList) {
        return ChatSystem.getRemoteUserByNickname(nickname, remoteUserList);
    }

    public void disconnection() {
        // Close ChatInterface window and return to ChatDialog
        this.dispose();
        final ConnectionInterface connectionInterface = new ConnectionInterface();
        connectionInterface.setVisible(true);
    }

    public void changeUser() {
        // Get selected remote user from list

        final RemoteUser selectedUser = getRemoteUserByNickname(getRemoteUserJList().getSelectedValue(), getRemoteUserList());
        if (selectedUser != null) {
            // Open new window to chat with selected remote user
            final InterfaceWithUser interfaceWithUser = new InterfaceWithUser(selectedUser);
            interfaceWithUser.setVisible(true);
            this.dispose();
        }
    }



    public WelcomeInterface() {
     // Envoyer un message de présentation au serveur pour récupérer la liste des utilisateurs distants
        this.remoteUserList = RemoteUser.getRemoteUsers();
        initInterface();
    }

    public void notifyConnectionFailed(String erreur) {
        JOptionPane.showMessageDialog(this, erreur);
    }

    public static void addRemoteUserToListModel(RemoteUser remoteUser) {
        //remoteUserListModel= (DefaultListModel<String>) getRemoteUserJList().getModel();
        remoteUserListModel.addElement(remoteUser.getNickname());
    }

    public static void main(String[] args) {

        // Créer l'interface graphique avec la liste des utilisateurs distants
        final WelcomeInterface welcomeInterface = new WelcomeInterface();
        welcomeInterface.setVisible(true);
    }

    /**
     * @return the userPanel
     */
    public JPanel getUserPanel() {
        return userPanel;
    }

    /**
     * @param userPanel the userPanel to set
     */
    public void setUserPanel(JPanel userPanel) {
        this.userPanel = userPanel;
    }

    /**
     * @return the remoteUserJList
     */
    public JList<String> getRemoteUserJList() {
        return remoteUserJList;
    }

    /**
     * @param remoteUserJList the remoteUserJList to set
     */
    public void setRemoteUserJList(JList<String> remoteUserJList) {
        this.remoteUserJList = remoteUserJList;
    }

    /**
     * @return the messageField
     */
    public JTextField getMessageField() {
        return messageField;
    }

    /**
     * @param messageField the messageField to set
     */
    public void setMessageField(JTextField messageField) {
        this.messageField = messageField;
    }

    /**
     * @return the chatArea
     */
    public JTextArea getChatArea() {
        return chatArea;
    }

    /**
     * @param chatArea the chatArea to set
     */
    public void setChatArea(JTextArea chatArea) {
        this.chatArea = chatArea;
    }

    /**
     * @return the sendButton
     */
    public JButton getSendButton() {
        return sendButton;
    }

    /**
     * @param sendButton the sendButton to set
     */
    public void setSendButton(JButton sendButton) {
        this.sendButton = sendButton;
    }

    /**
     * @return the logoutButton
     */
    public JButton getLogoutButton() {
        return logoutButton;
    }

    /**
     * @param logoutButton the logoutButton to set
     */
    public void setLogoutButton(JButton logoutButton) {
        this.logoutButton = logoutButton;
    }

    /**
     * @return the downloadButton
     */
    public JButton getDownloadButton() {
        return downloadButton;
    }

    /**
     * @param downloadButton the downloadButton to set
     */
    public void setDownloadButton(JButton downloadButton) {
        this.downloadButton = downloadButton;
    }

    /**
     * @return the remoteUserList
     */
    public List<RemoteUser> getRemoteUserList() {
        return remoteUserList;
    }

}
