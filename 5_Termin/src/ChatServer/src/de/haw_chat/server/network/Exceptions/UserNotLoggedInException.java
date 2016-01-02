package de.haw_chat.server.network.Exceptions;

/**
 * Created by dima on 10.11.15.
 */
public class UserNotLoggedInException extends Exception {

    public UserNotLoggedInException(String message){
        super(message);
    }

    public UserNotLoggedInException(){
        super();
    }
}
