package de.haw_chat.server.network.implementations;

import de.haw_chat.server.model.Chat;
import de.haw_chat.server.network.Exceptions.*;
import de.haw_chat.server.network.interfaces.ClientThread;
import de.server.persistence.Exceptions.ChatNotFoundException;
import de.server.persistence.Exceptions.UserNotFoundException;
import de.server.persistence.ServerPersistence;
import de.server.persistence.ServerPersistenceImpl;
import de.server.persistence.client.*;
import de.server.persistence.client.Exeptions.DataBaseNotFoundException;
import de.server.persistence.result.ChatData;
import de.server.persistence.result.ContactData;
import de.server.persistence.result.OwnContactData;

import java.net.UnknownHostException;
import java.util.*;
import static com.google.common.base.Preconditions.*;


/**
 * Created by Dima on 14.12.2015
 */
public final class ServerData {

    private static ServerData           instance;


    private Map<String,ClientThread>    takenUsernames;
    private Map<Long,Chat>              chats;
    private ServerPersistence           serverPersistence;

    private ServerData() {
        this.takenUsernames    = new HashMap();
        this.chats             = new HashMap();
        initServerPersistence();
    }

    public static ServerData getInstance() {

        if(instance == null) instance = new ServerData();
        return instance;
    }

    private void initServerPersistence(){
        try {
            serverPersistence = DeviceFactory.getServerPersistence();
        } catch (DataBaseNotFoundException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    private boolean isUsernameLoggedIn(String username){
        checkNotNull(username);
        return takenUsernames.containsKey(username);
    }

    private void checkClientSession(ClientThread clientThread) throws ClientNotLoggedInException {
        // is client logged in
        if(!clientThread.getData().isLoggedIn()) throw new ClientNotLoggedInException();
        // is username logged in
        if(!isUsernameLoggedIn(clientThread.getData().getUsername())){
            clientThread.getData().setLogout();
            throw new ClientNotLoggedInException();
        }
    }

    public synchronized void login(String username, String password, String os,ClientThread clientThread) throws UsernameAlreadyUsedException, ClientAlreadyLoggedInException, UsernameOrPasswordWorngException {
        checkNotNull(username);
        checkNotNull(password);
        checkNotNull(clientThread);

        if(clientThread.getData().getUsername() != null) throw new ClientAlreadyLoggedInException();// client already logged in
        if(!isUsernameLoggedIn(username)) throw new UsernameAlreadyUsedException();// username already taken
        if(!serverPersistence.loginUser(username, password)) throw new UsernameOrPasswordWorngException();// username or password wrong

        String ipAdress = clientThread.getClientSocket().getInetAddress().getHostAddress();
        long timeStamp  = System.currentTimeMillis();
        takenUsernames.put(username,clientThread);
        serverPersistence.addLoginSession(username,password,timeStamp,ipAdress,os);
    }

    public synchronized void logout(ClientThread clientThread) throws UserNotLoggedInException {
        checkNotNull(clientThread);

        if(!clientThread.getData().isLoggedIn()) throw new UserNotLoggedInException();
        takenUsernames.remove(clientThread.getData().getUsername());
        clientThread.getData().setLogout();
    }

//    public long createChat()

    public synchronized List<OwnContactData> getOwnContacts(ClientThread clientThread) throws ClientNotLoggedInException, UserNotFoundException {
        checkNotNull(clientThread);
        checkClientSession(clientThread);

        List<OwnContactData> contactDataList = serverPersistence.getOwnContacts(clientThread.getData().getUsername());
        return contactDataList;
    }

    public synchronized List<ContactData> getAllContacts(ClientThread clientThread) throws ClientNotLoggedInException {
        checkNotNull(clientThread);
        checkClientSession(clientThread);

        return serverPersistence.getAllContacts();
    }

    private synchronized boolean containsUser(String user){
        return takenUsernames.containsKey(user);
    }

    public synchronized ClientThread getClientThread(String username) throws UserNotFoundException {
        checkNotNull(username);
        if(!takenUsernames.containsKey(username)) throw new UserNotFoundException();
        return takenUsernames.get(username);
    }

    public synchronized Chat getChat(long id, ClientThread clientThread) throws ChatNotFoundException, ClientNotLoggedInException {
        checkNotNull(clientThread);
        checkClientSession(clientThread);

        if(chats.containsKey(id)) return chats.get(id);
        ChatData chatData = serverPersistence.getOwnChat(id);

        Chat c = new Chat(chatData.getChatID(),chatData.getUser1(),chatData.getUser2(),chatData.getTimeStamp());
        chats.put(id,c);
        return c;
    }

    public synchronized Set<ChatData> getOwnChats(ClientThread clientThread) throws ClientNotLoggedInException, UserNotFoundException {
        checkNotNull(clientThread);
        checkClientSession(clientThread);

        return serverPersistence.getOwnChats(clientThread.getData().getUsername());
    }
}
