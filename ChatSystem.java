package ChatSystem;


//import java.net.InetAddress;
//import java.net.UnknownHostException;
import java.util.List;

public class ChatSystem {

    public static final int PORT = 10000;
    public static String userNickname;
    final ChatNI CNI = new ChatNI();

    /**
     * @return the userNickname
     */
    public static String getUserNickname() {
        return userNickname;
    }

    /**
     * @param userNickname the userNickname to set
     */
    public static void setUserNickname(String userNickname) {
        ChatSystem.userNickname = userNickname;
    }

    public void sendHello (String nickname) {
        
        CNI.sendHello(getUserNickname());
     
         /*
        try {
            RemoteUser ru = new RemoteUser("Blaja", InetAddress.getByName("localhost"));
            RemoteUser ju = new RemoteUser("anne", InetAddress.getByName("localhost")); 
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        //CNI.listenerThread.start();//Lance les écoute de hello
        */
    }
    static RemoteUser getRemoteUserByNickname(String nickname, List<RemoteUser> remoteUserList) {
        for (final RemoteUser remoteUser : remoteUserList) {
            if (remoteUser.getNickname().equals(nickname)) {
                return remoteUser;
            }
        }
        return null; // Aucun utilisateur distant avec le surnom donné n'a été trouvé
    }



    public boolean  verificationNickname(String nickname) {
        // Check if nickname is valid
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
