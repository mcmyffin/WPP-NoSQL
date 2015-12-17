package de.haw_chat.server.network.packets.client_packets;

import de.haw_chat.server.network.Exceptions.IllegalArgumentPacketException;
import de.haw_chat.server.network.interfaces.ClientThread;

/**
 * Created by Dima on 14.12.2015.
 */
public class ChatCreatePacket extends AbstractClientPacket {

    private String username;
    public ChatCreatePacket(ClientThread clientThread, String messageString) throws IllegalArgumentPacketException {
        super(clientThread);
        String[] splitedMessageString = messageString.split(" ");

        if(splitedMessageString.length != 2) throw new IllegalArgumentPacketException();
        username = splitedMessageString[1];
    }


    @Override
    public void process() {
        // TODO
    }
}
