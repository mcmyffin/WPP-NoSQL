package de.haw_chat.server.network.implementations;

import de.haw_chat.server.network.interfaces.ClientThread;
import java.util.*;
import static com.google.common.base.Preconditions.*;


/**
 * Created by Dima on 14.12.2015
 */
public final class ServerData {
    private static ServerData instance;
    private Map<String,ClientThread>        takenUsernames;

    private ServerData() {
        this.takenUsernames = new HashMap();
    }

    public static ServerData getInstance() {

        if(instance == null) instance = new ServerData();
        return instance;
    }

    public synchronized boolean addUserClient(String username, ClientThread clientThread){
        checkNotNull(username);
        checkNotNull(clientThread);
        if(takenUsernames.containsKey(username)) return false;

        takenUsernames.put(username,clientThread);
        return true;
    }

    public synchronized boolean containsUser(String user){
        return takenUsernames.containsKey(user);
    }

    public synchronized boolean removeUserClient(ClientThread client){
        checkNotNull(client);
        if(!containsUser(client.getData().getUsername())) return false;

        takenUsernames.remove(client.getData().getUsername());
        client.getData().setUsername(null);
        return true;
    }

    public synchronized ClientThread getChatClientByUsername(String username) {
        checkNotNull(username);
        return takenUsernames.get(username);
    }
}
