package de.haw_chat.server.network.interfaces;

import java.util.Set;

/**
 * Created by Dima on 14.12.2015
 */
public interface ClientData {

    public String getUsername();
    public void setUsername(String username);
    public boolean isLoggedIn();
}
