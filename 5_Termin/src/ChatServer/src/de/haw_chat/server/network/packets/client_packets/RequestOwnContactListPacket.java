package de.haw_chat.server.network.packets.client_packets;

import de.haw_chat.server.network.interfaces.ClientThread;

/**
 * Created by Dima on 14.12.2015.
 */
public class RequestOwnContactListPacket extends AbstractClientPacket {

    public RequestOwnContactListPacket(ClientThread clientThread, String messageString){
        super(clientThread);
    }

    @Override
    public void process() {
        // TODO
    }
}
