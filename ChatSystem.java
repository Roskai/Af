package ChatSystem;

import java.util.Iterator;
import java.util.List;

/**
 * This class represents the ChatSystem and implements the Singleton design pattern
 */
public class ChatSystem {

    private static ChatSystem instance = null;
    public static final int PORT = 10001;
    private static String userNickname;
    private final ChatNI CNI = new ChatNI();
    private WelcomeInterface welcomeInterface;

    /**
     * Constructor is private to implement Singleton pattern
     */
    private ChatSystem() {
    }

    /**
     * Returns the singleton instance of the ChatSystem class
     * 
     * @return the singleton instance of the ChatSystem class
     */
    public static ChatSystem getInstance() {
        if (instance == null) {
            synchronized (ChatSystem.class) {
                if (instance == null) {
                    instance = new ChatSystem();
                }
            }
        }
        return instance;
    }
    /**
     * @return the userNickname
     */
    public String getUserNickname() {
        return userNickname;
    }

    /**
     * @param userNickname the userNickname to set
     */
    public static void setUserNickname(String userNickname) {
        ChatSystem.userNickname = userNickname;
    }

    /**
     * Sends a "hello" message to announce the user's presence
     *
     * @param nickname The user's nickname
     */
    public void sendHello(String nickname) {
        CNI.sendHello(getUserNickname());
    }

    /**
     * Sends a "goodbye" message to inform other users of the user's departure
     * @param nickname The user's nickname
     */
    public void sendGoodbye(String nickname) {
        CNI.sendGoodbye(getUserNickname());
    }

    /**
     * Updates the remote user list in the WelcomeInterface
     */
    public void updateRemoteUserList() {
        try {
            welcomeInterface.emptyRemoteUserJList();
            for (final RemoteUser remoteUser : RemoteUser.getRemoteUsers()) {
                welcomeInterface.addRemoteUserToListModel(remoteUser);
            }

        } catch (Exception e) {
            System.err.println("WelcomeInterface not initialized yet");
        }
    }

    /**
     * Returns the remote user with the given nickname
     *
     * @param nickname       The nickname of the remote user to look for
     * @param remoteUserList The list of remote users to search in
     * @return The remote user with the given nickname, or null if not found
     */
    public RemoteUser getRemoteUserByNickname(String nickname) {
        for (final RemoteUser remoteUser : RemoteUser.getRemoteUsers()) {
            if (remoteUser.getNickname().equals(nickname)) {
                return remoteUser;
            }
        }
        return null; // No remote user with given nickname found
    }

    /**
     * Prints the WelcomeInterface and sends a "hello" message to announce the user's presence
     */
    protected void printWelcomeInterface() {
        welcomeInterface = new WelcomeInterface();
        sendHello(getUserNickname());
        welcomeInterface.setVisible(true);
    }



    /**
     * Checks if a nickname is valid (i.e. consists only of letters and digits)
     *
     * @param nickname The nickname to check
     * @return true if the nickname is valid, false otherwise
     */
    public boolean verificationNickname(String nickname) {
        boolean valid = true;
        for (int i = 0; i < nickname.length(); i++) {
            char c = nickname.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
                valid = false;
                break;
            }
        }
        return valid;
    }

}
