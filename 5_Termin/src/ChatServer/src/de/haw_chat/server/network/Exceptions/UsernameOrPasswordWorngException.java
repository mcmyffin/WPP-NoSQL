package de.haw_chat.server.network.Exceptions;

/**
 * Created by dima on 10.11.15.
 */
public class UsernameOrPasswordWorngException extends Exception {

    public UsernameOrPasswordWorngException(String message){
        super(message);
    }

    public UsernameOrPasswordWorngException(){
        super();
    }
}
