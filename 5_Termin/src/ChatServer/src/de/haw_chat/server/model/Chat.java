package de.haw_chat.server.model;

import de.haw_chat.common.operation.implementations.Status;
import de.haw_chat.server.network.Exceptions.PermissionDeniedExeption;
import de.haw_chat.server.network.implementations.DeviceFactory;
import de.haw_chat.server.network.implementations.ServerData;
import de.haw_chat.server.network.interfaces.ClientThread;
import de.haw_chat.server.network.packets.server_packets.*;
import de.server.persistence.Exceptions.UserNotFoundException;
import de.server.persistence.ServerPersistence;
import de.server.persistence.client.Exeptions.DataBaseNotFoundException;
import de.server.persistence.result.MessageData;

import java.net.UnknownHostException;
import java.util.*;

import static com.google.common.base.Preconditions.*;

/**
 * Created by dima on 04.11.15.
 */
public class Chat {

    private final long id;
    private final String user1;
    private final String user2;
    private final long timeStamp;
    private ServerPersistence serverPersistence;


    public Chat(long chatID, String username1, String username2, long timeStamp){
        checkNotNull(username1);
        checkNotNull(username2);

        this.id = chatID;
        this.user1 = username1;
        this.user2 = username2;
        this.timeStamp = timeStamp;
    }

    private void initServerpersistence(){
        try {
            this.serverPersistence = DeviceFactory.getServerPersistence();
        } catch (DataBaseNotFoundException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public MessageSendResponsePacket sendMessage(ClientThread client, MessageData messageData) throws PermissionDeniedExeption{
        checkNotNull(client);
        checkNotNull(messageData);


        try {
            ClientThread toUser = client.getServer().getData().getClientThread(messageData.getToUser());
            // TODO send message to user
        } catch (UserNotFoundException e) {
            // TODO user is offline, message can not be sended
            // TODO need a new Statuscode for "user is offline"
        }
        return null;
    }



}
