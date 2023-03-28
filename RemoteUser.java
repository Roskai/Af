package ChatSystem;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class RemoteUser {
    private String nickname;
    private InetAddress address;

    // Liste statique des utilisateurs distants
    private static List<RemoteUser> remoteUsers = new ArrayList<>();

    public RemoteUser(String nickname, InetAddress address) {
        this.nickname = nickname;
        this.address = address;
        remoteUsers.add(this);
    }

    public String getNickname() {
        return nickname;
    }

    public InetAddress getAddress() {
        return address;
    }

    // Accesseur à la liste des utilisateurs distants
    public static List<RemoteUser> getRemoteUsers() {
        return remoteUsers;
    }
}
