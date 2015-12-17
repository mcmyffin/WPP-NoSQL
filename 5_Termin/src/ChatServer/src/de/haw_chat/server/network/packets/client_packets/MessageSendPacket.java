package de.haw_chat.server.network.packets.client_packets;

import de.haw_chat.server.network.Exceptions.IllegalArgumentPacketException;
import de.haw_chat.server.network.interfaces.ClientThread;

/**
 * Created by Dima on 14.12.2015.
 */
public class MessageSendPacket extends AbstractClientPacket {
    private long    chatID;
    private String  message;
    private long    mediaID;

    public MessageSendPacket(ClientThread clientThread, String messageString) throws IllegalArgumentPacketException {
        super(clientThread);

        String[] splitedMessageString = messageString.split(" ");
        if(splitedMessageString.length < 3 || splitedMessageString.length > 4) throw new IllegalArgumentPacketException();

        try {
            chatID      = Long.parseLong(splitedMessageString[1]);
            message     = splitedMessageString[2];
            mediaID     = Long.parseLong(splitedMessageString.length == 3? "0":splitedMessageString[3]);
        }catch (NumberFormatException ex){
            throw new IllegalArgumentPacketException("Number Fromat wrong");
        }
    }

    @Override
    public void process() {
        // TODO
    }
}
