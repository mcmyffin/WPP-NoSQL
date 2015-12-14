package de.haw_chat.server.network.packets.client_packets;

import de.haw_chat.server.network.implementations.ServerData;
import de.haw_chat.server.network.interfaces.ClientData;
import de.haw_chat.server.network.interfaces.ClientThread;
import de.haw_chat.server.network.packets.server_packets.AbstractServerPacket;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Andreas on 31.10.2015.
 */
public abstract class AbstractClientPacket {
    private final ClientThread clientThread;

    protected AbstractClientPacket(ClientThread clientThread) {
        checkNotNull(clientThread);
        this.clientThread = clientThread;
    }

    protected final ClientData getClientData() {
        checkNotNull(clientThread.getData());
        return clientThread.getData();
    }

    protected final ServerData getServerData() {
        checkNotNull(clientThread.getServer());
        checkNotNull(clientThread.getServer().getData());
        return clientThread.getServer().getData();
    }

    protected final void sendToClient(AbstractServerPacket serverPacket) {
        clientThread.writeToClient(serverPacket);
    }

    protected  final ClientThread getClientThread(){
        return this.clientThread;
    }
    public abstract void process();
}
