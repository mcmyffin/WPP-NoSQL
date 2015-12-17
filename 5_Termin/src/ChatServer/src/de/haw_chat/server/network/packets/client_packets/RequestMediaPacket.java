package de.haw_chat.server.network.packets.client_packets;

import de.haw_chat.server.network.Exceptions.IllegalArgumentPacketException;
import de.haw_chat.server.network.interfaces.ClientThread;

/**
 * Created by Dima on 14.12.2015.
 */
public class RequestMediaPacket extends AbstractClientPacket {
    private long mediaID;

    public RequestMediaPacket(ClientThread clientThread, String messageString) throws IllegalArgumentPacketException {
        super(clientThread);

        String[] splitedMessageString = messageString.split(" ");
        if(splitedMessageString.length != 2) throw new IllegalArgumentPacketException();

        try {
            mediaID = Long.parseLong(splitedMessageString[1]);
        }catch (NumberFormatException ex){
            throw new IllegalArgumentPacketException("Number Format wrong!");
        }
    }

    @Override
    public void process() {
        // TODO
    }
}
