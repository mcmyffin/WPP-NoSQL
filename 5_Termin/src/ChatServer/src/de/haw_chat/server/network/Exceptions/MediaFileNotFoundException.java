package de.haw_chat.server.network.Exceptions;

/**
 * Created by dima on 10.11.15.
 */
public class MediaFileNotFoundException extends Exception {

    public MediaFileNotFoundException(String message){
        super(message);
    }

    public MediaFileNotFoundException(){
        super();
    }
}
