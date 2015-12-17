package de.haw_chat.server.network.implementations;

import de.haw_chat.server.network.interfaces.ClientThread;
import de.haw_chat.server.network.interfaces.ServerPersistence;

import java.util.*;
import static com.google.common.base.Preconditions.*;


/**
 * Created by Dima on 14.12.2015
 */
public final class ServerData {

    private static ServerData           instance;
    private Map<String,ClientThread>    takenUsernames;
    private ServerPersistence           serverPersistence;

    private ServerData() {
        this.takenUsernames    = new HashMap();
        this.serverPersistence = new ServerPersistenceImpl();
    }

    public static ServerData getInstance() {

        if(instance == null) instance = new ServerData();
        return instance;
    }

    // TODO
    public synchronized void login(String username, String password, ClientThread clientThread){
        checkNotNull(username);
        checkNotNull(password);
        checkNotNull(clientThread);

        if(clientThread.getData().getUsername() != null) // client already logged in
        if(containsUser(username)) // username already taken
        if(!serverPersistence.loginUser(username, password)) // username or password wrong

        // addloginSession
        //serverPersistence.addLoginSession()
        takenUsernames.put(username,clientThread);
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
