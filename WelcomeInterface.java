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

    private final ChatSystem chatSystem = ChatSystem.getInstance();
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton downloadButton;
    private JButton logoutButton;
    private JButton closeConvButton;
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
        getChatArea().setText("Welcome " + chatSystem.getUserNickname() + "\nPlease chose a remote user");
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

        remoteUserPanel.add(getLogoutButton(), BorderLayout.PAGE_END);

        mainPanel.add(remoteUserPanel, BorderLayout.WEST);
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));

        setMessageField(new JTextField());
        getMessageField().setEnabled(false);
        buttonPanel.add(getMessageField());

        setSendButton(new JButton("Send"));
        getSendButton().setEnabled(false);
        getSendButton().addActionListener(this);
        buttonPanel.add(getSendButton());

        setCloseConvButton(new JButton("Close the current conversation"));
        getCloseConvButton().setEnabled(false);
        buttonPanel.add(getCloseConvButton());

        setDownloadButton(new JButton("Upload"));
        getDownloadButton().setEnabled(false);
        buttonPanel.add(getDownloadButton());
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        addListennerChangeUser();
        setContentPane(mainPanel);
        setVisible(true);
        chatSystem.updateRemoteUserList();
    }

    // Gérer les actions de l'utilisateur
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getLogoutButton()) {
            disconnection();
        }
    }

    public RemoteUser getRemoteUserByNickname(String nickname) {
        return chatSystem.getRemoteUserByNickname(nickname);
    }

    public void disconnection() {
        // Close ChatInterface window and return to ChatDialog
        chatSystem.sendGoodbye(chatSystem.getUserNickname());
        this.dispose();
        final ConnectionInterface connectionInterface = new ConnectionInterface();
        connectionInterface.setVisible(true);
    }

    public void changeUser() {
        // Get selected remote user from list

        final RemoteUser selectedUser = getRemoteUserByNickname(getRemoteUserJList().getSelectedValue());
        if (selectedUser != null) {
            // Open new window to chat with selected remote user
            final InterfaceWithUser interfaceWithUser = new InterfaceWithUser(selectedUser);
            interfaceWithUser.setVisible(true);
            this.dispose();
        }

    }

    public void addListennerChangeUser() {
        getRemoteUserJList().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    changeUser();
                }
            }
        });
    }

    public WelcomeInterface() {
        // Envoyer un message de présentation au serveur pour récupérer la liste des utilisateurs distants
        this.remoteUserList = RemoteUser.getRemoteUsers();
        initInterface();
    }

    public void notifyConnectionFailed(String erreur) {
        JOptionPane.showMessageDialog(this, erreur);
    }

    public void emptyRemoteUserJList() {
        getRemoteUserListModel().removeAllElements();
    }

    public void addRemoteUserToListModel(RemoteUser remoteUser) {
        remoteUserListModel.addElement(remoteUser.getNickname());
    }

    public static void removeRemoteUserToListModel(RemoteUser remoteUser) {
        final int index = remoteUserListModel.indexOf(remoteUser.getNickname());
        if (index >= 0) {
            remoteUserListModel.removeElementAt(index);
        }
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

    /**
     * @return the remoteUserPanel
     */
    public JPanel getRemoteUserPanel() {
        return remoteUserPanel;
    }

    /**
     * @param remoteUserPanel the remoteUserPanel to set
     */
    public void setRemoteUserPanel(JPanel remoteUserPanel) {
        this.remoteUserPanel = remoteUserPanel;
    }

    /**
     * @return the remoteUserListModel
     */
    public static DefaultListModel<String> getRemoteUserListModel() {
        return remoteUserListModel;
    }

    /**
     * @param remoteUserListModel the remoteUserListModel to set
     */
    public static void setRemoteUserListModel(DefaultListModel<String> remoteUserListModel) {
        WelcomeInterface.remoteUserListModel = remoteUserListModel;
    }

    /**
     * @return the closeConvButton
     */
    public JButton getCloseConvButton() {
        return closeConvButton;
    }

    /**
     * @param closeConvButton the closeConvButton to set
     */
    public void setCloseConvButton(JButton closeConvButton) {
        this.closeConvButton = closeConvButton;
    }

}
