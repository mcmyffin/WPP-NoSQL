package de.haw_chat.server.network.interfaces;

import de.haw_chat.server.network.packets.server_packets.AbstractServerPacket;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Dima on 14.12.2015
 */
public interface ClientThread extends Runnable {

    public int getClientId();
    public Socket getClientSocket();
    public Server getServer();
    public boolean isWorkerServiceRequested();
    public void setWorkerServiceRequested(boolean workerServiceRequested);

    public ClientData getData();
    public void writeToClient(AbstractServerPacket serverPacket);
}
