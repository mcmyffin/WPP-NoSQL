package de.haw_chat.server.network.Exceptions;

/**
 * Created by dima on 10.11.15.
 */
public class InvalidPasswordExeption extends Exception {

    public InvalidPasswordExeption(String message){
        super(message);
    }

    public InvalidPasswordExeption(){
        super();
    }
}
