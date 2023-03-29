package ChatSystem;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/*
 *
 *  This class represents a remote user of the Chat System.
 *
 */
public class RemoteUser {
    /**
     * The nickname of the user
     */
    private final String nickname;
    /**
     * The ip address of the user
     */
    private final InetAddress address;
    /**
     * List of all the remote users
     */
    private static List<RemoteUser> remoteUsers = new ArrayList<>();

    /**
     * Constructor
     *
     * @param nickname The nickname of the user
     * @param address The ip address of the user
     */
    public RemoteUser(String nickname, InetAddress address) {
        this.nickname = nickname;
        this.address = address;
        remoteUsers.add(this);
    }

    /**
     * Get the nickname of the user
     *
     * @return The nickname of the user
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Get the ip address of the user
     *
     * @return The ip address of the user
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * Remove a remote user from the list
     *
     * @param remoteUser The user to remove
     */
    public static void removeRemoteUser(RemoteUser remoteUser) {
        remoteUsers.remove(remoteUser);
    }

    /**
     * Get the list of all the remote users
     *
     * @return The list of all the remote users
     */
    public static List<RemoteUser> getRemoteUsers() {
        return remoteUsers;
    }

    public static List<RemoteUser> getRemoteUsers(RemoteUser remoteUser) {
        List<RemoteUser> result = new ArrayList<>();
        for (RemoteUser user : remoteUsers) {
            if (user.getNickname().equals(remoteUser.getNickname()) && user.getAddress().equals(remoteUser.getAddress())) {
                result.add(user);
            }
        }
        return result;
}
}
