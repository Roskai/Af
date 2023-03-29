package ChatSystem;

/**
 * This class represents the ChatSystem and implements the Singleton design pattern
 */
public class ChatSystem {

    public static final int PORT = 10001;
    private static String userNickname;
    //private  ChatNI CNI;
    private WelcomeInterface welcomeInterface;

    /**
     *Constructs a new ChatSystem instance. This constructor is private to prevent direct instantiation.
     */
    private ChatSystem() {
        //CNI = ChatNI.getInstance();
    }

    /**
     * The SingletonHolder class holds a single instance of ChatSystem, created when the class is first loaded.
     * This ensures that only one instance is created, and that it is thread-safe.
     */
    private static class SingletonHolder {
        private static final ChatSystem INSTANCE = new ChatSystem();
    }

    /**
     * Returns the single instance of ChatSystem.
     * @return the ChatSystem instance
     */
    public static ChatSystem getInstance() {
        System.out.println("CS : " + SingletonHolder.INSTANCE);
        return SingletonHolder.INSTANCE;
    }

    public static void main(String[] args) {
        new ConnectionInterface();
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
        ChatNI.getInstance().sendHello(getUserNickname());
    }

    /**
     * Sends a "goodbye" message to inform other users of the user's departure
     * @param nickname The user's nickname
     */
    public void sendGoodbye(String nickname) {
        ChatNI.getInstance().sendGoodbye(getUserNickname());
    }

    /**
     * Updates the remote user list in the WelcomeInterface
     */
    public void updateRemoteUserList() {
        try {
            welcomeInterface.emptyRemoteUserJList();
            for (final RemoteUser remoteUser : RemoteUser.getRemoteUsers()) {
                welcomeInterface.addRemoteUserToListModel(remoteUser);
                System.out.println("Remote user  :"+remoteUser.getNickname()+" is in ! ");
            }

        } catch (final Exception e) {
            System.err.println("WelcomeInterface not initialized yet");
        }
    }

    /**
     * Returns the remote user with the given nickname
     *
     * @param nickname       The nickname of the remote user to look for
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
        System.out.println("Le nickname est \"" + nickname + "\"");
        boolean valid = true;
        if (nickname.isBlank() || nickname.isEmpty()) {
            for (int i = 0; i < nickname.length(); i++) {
                final char c = nickname.charAt(i);
                if (!Character.isLetterOrDigit(c)) {
                    valid = false;
                    break;
                }
            }
            valid = false;
        }
        return valid;

    }

}
