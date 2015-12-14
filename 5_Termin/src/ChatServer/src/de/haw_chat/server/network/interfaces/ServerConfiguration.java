package de.haw_chat.server.network.interfaces;

/**
 * Created by Dima on 14.12.2015.
 */
public interface ServerConfiguration {

    public int getServerPort();
    public int getMaxClients();
    public boolean isSslEnabled();
}
