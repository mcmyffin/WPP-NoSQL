package de.haw_chat.server.network.Exceptions;

/**
 * Created by dima on 10.11.15.
 */
public class IllegalArgumentPacketException extends Exception {

    public IllegalArgumentPacketException(String message){
        super(message);
    }

    public IllegalArgumentPacketException(){
        super();
    }
}
