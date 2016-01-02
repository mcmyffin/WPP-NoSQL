package de.haw_chat.server.network.Exceptions;

/**
 * Created by dima on 10.11.15.
 */
public class UsernameAlreadyUsedException extends Exception {

    public UsernameAlreadyUsedException(String message){
        super(message);
    }

    public UsernameAlreadyUsedException(){
        super();
    }
}
