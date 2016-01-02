package de.haw_chat.server.network.implementations;

import de.haw_chat.server.network.interfaces.ClientData;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Andreas on 31.10.2015.
 */
final class ClientDataImpl implements ClientData {

    private String username;
    private String password;
    private ClientDataImpl() {
    }

    public static ClientData create() {
        return new ClientDataImpl();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setLoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void setLogout() {
        this.username = null;
        this.password = null;
    }

    @Override
    public boolean isLoggedIn() {
        return (getUsername() != null && getPassword() != null);
    }

}
