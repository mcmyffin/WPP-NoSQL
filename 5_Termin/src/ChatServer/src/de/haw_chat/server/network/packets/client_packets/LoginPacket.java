package de.haw_chat.server.network.packets.client_packets;

import de.haw_chat.server.network.Exceptions.IllegalArgumentPacketException;
import de.haw_chat.server.network.implementations.ServerData;
import de.haw_chat.server.network.interfaces.ClientThread;
import de.haw_chat.server.network.packets.server_packets.LoginResponsePacket;

import static de.haw_chat.common.operation.implementations.Status.*;

/**
 * Created by Dima on 14.12.2015.
 */
public class LoginPacket extends AbstractClientPacket {
    private String username;
    private String password;

    public LoginPacket(ClientThread clientThread, String messageString) throws IllegalArgumentPacketException {
        super(clientThread);

        String[] splitedMessageString = messageString.split(" ");
        if(splitedMessageString.length != 3) throw new IllegalArgumentPacketException();

        username = splitedMessageString[1];
        password = splitedMessageString[2];
    }

    @Override
    public void process() {
        // TODO
    }
}
