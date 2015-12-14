package de.haw_chat.server.network.packets.server_packets;

import de.haw_chat.common.operation.implementations.Status;

import static de.haw_chat.common.operation.implementations.Status.*;
import static de.haw_chat.common.operation.implementations.OperationDataManager.getOperationData;

/**
 * Created by Andreas on 31.10.2015.
 */
public class ChatListElemPacket extends AbstractServerPacket {
    private String chatroomName;
    private int currentUserCount;
    private int maxUserCount;

    public ChatListElemPacket(String chatroomName, int currentUserCount, int maxUserCount) {
        this.chatroomName = chatroomName;
        this.currentUserCount = currentUserCount;
        this.maxUserCount = maxUserCount;
    }

    @Override
    public String toMessageString() {
        return "" + getOperationCode() + " " + chatroomName + " " + currentUserCount + " " + maxUserCount;
    }
}
