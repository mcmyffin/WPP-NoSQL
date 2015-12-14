package de.haw_chat.server.network.interfaces;

import de.haw_chat.server.network.implementations.ServerData;

import java.util.concurrent.Semaphore;

/**
 * Created by Dima on 14.12.2015
 */
public interface Server extends Runnable {

    public ServerConfiguration getConfiguration();
    public ServerData getData();

    public Semaphore getClientThreadsSemaphore();
}
