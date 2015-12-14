package de.haw_chat.server.network.packets.server_packets;

/**
 * Created by Dima on 31.10.2015.
 */
public class InvalidPacket extends AbstractServerPacket {

    public InvalidPacket(String message){
    }

    @Override
    public String toMessageString() {
        return "" + getOperationCode();
    }

}
