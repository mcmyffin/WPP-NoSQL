package de.haw_chat.server.network.packets.client_packets;

import de.haw_chat.server.network.Exceptions.IllegalArgumentPacketException;
import de.haw_chat.server.network.interfaces.ClientThread;

/**
 * Created by Dima on 14.12.2015.
 */
public class RegisterAccountPacket extends AbstractClientPacket {

    private String username;
    private String password;
    private String email;
    private String gender;

    public RegisterAccountPacket(ClientThread clientThread, String messageString) throws IllegalArgumentPacketException {
        super(clientThread);

        String[] splitedMessageString = messageString.split(" ");
        if(splitedMessageString.length != 5) throw new IllegalArgumentPacketException();

        username = splitedMessageString[1];
        password = splitedMessageString[2];
        email    = splitedMessageString[3];
        gender   = splitedMessageString[4];
    }

    @Override
    public void process() {
        // TODO
    }
}
