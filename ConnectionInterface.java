package ChatSystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class ConnectionInterface extends JFrame implements ActionListener {

    private final ChatSystem chatSystem;

    private final JLabel welcomeLabel;
    private final JLabel nicknameLabel;
    private final JTextField nicknameField;

    private final JButton connectButton;

    public ConnectionInterface() {
        // Set window title
        super("Chat System");

        // Create welcome label
        welcomeLabel = new JLabel("Welcome to our Chat System");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create nickname label and text field
        nicknameLabel = new JLabel("Your nickname");
        nicknameField = new JTextField(20);
        nicknameField.setText("");

        // Create connect button
        connectButton = new JButton("Connection");
        connectButton.addActionListener(this);

        // Create panel and add components
        final JPanel panel = new JPanel();
        panel.add(welcomeLabel);
        panel.add(nicknameLabel);
        panel.add(nicknameField);
        panel.add(connectButton);

        // Add panel to frame and set window properties
        add(panel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setVisible(true);
        chatSystem = ChatSystem.getInstance();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!chatSystem.verificationNickname(nicknameField.getText())) {
            sendRetry();
        } else {
            ChatSystem.setUserNickname(nicknameField.getText());
            printWelcomeInterface();
        }
    }

    /**
     * @param
     */
    private void printWelcomeInterface() {
        chatSystem.printWelcomeInterface();
        this.dispose();

    }

    /**
     *
     */
    private void resetNicknameField() {
        nicknameField.setText("");
    }

    public void sendRetry() {
        JOptionPane.showMessageDialog(this, "Please, enter a nickname with no special caracters");
        resetNicknameField();
    }

}
