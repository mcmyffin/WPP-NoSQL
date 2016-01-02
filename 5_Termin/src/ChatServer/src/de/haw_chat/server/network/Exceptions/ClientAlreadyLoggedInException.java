package de.haw_chat.server.network.Exceptions;

/**
 * Created by dima on 10.11.15.
 */
public class ClientAlreadyLoggedInException extends Exception {

    public ClientAlreadyLoggedInException(String message){
        super(message);
    }

    public ClientAlreadyLoggedInException(){
        super();
    }
}
