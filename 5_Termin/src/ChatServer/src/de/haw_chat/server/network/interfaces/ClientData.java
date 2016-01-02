package de.haw_chat.server.network.interfaces;

import java.util.Set;

/**
 * Created by Dima on 14.12.2015
 */
public interface ClientData {

    public String getUsername();
    public String getPassword();

    public void setLoginData(String username, String password);
    public void setLogout();
    public boolean isLoggedIn();
}
