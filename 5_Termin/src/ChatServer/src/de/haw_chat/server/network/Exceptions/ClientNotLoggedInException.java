package de.haw_chat.server.network.Exceptions;

/**
 * Created by dima on 10.11.15.
 */
public class ClientNotLoggedInException extends Exception {

    public ClientNotLoggedInException(String message){
        super(message);
    }

    public ClientNotLoggedInException(){
        super();
    }
}
