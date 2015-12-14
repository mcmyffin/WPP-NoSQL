package de.haw_chat.server.network.packets.server_packets;

import de.haw_chat.common.operation.implementations.Status;

import static de.haw_chat.common.operation.implementations.Status.*;
import static de.haw_chat.common.operation.implementations.OperationDataManager.getOperationData;

/**
 * Created by Andreas on 31.10.2015.
 */
public class MessageSendedPacket extends AbstractServerPacket {
    private String chatname;
    private String username;
    private String message;
    private long timestamp;

    public MessageSendedPacket(String chatname, String username, String message, long timestamp) {
        this.chatname = chatname;
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    @Override
    public String toMessageString() {
        return "" + getOperationCode() + " " +chatname + " " + timestamp + " " + username + " " + message;
    }
}
