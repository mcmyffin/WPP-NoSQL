package de.haw_chat.server.network.Exceptions;

/**
 * Created by dima on 10.11.15.
 */
public class MediaFileAlreadyExistsException extends Exception {

    public MediaFileAlreadyExistsException(String message){
        super(message);
    }

    public MediaFileAlreadyExistsException(){
        super();
    }
}
