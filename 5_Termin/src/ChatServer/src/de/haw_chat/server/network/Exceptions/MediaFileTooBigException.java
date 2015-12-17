package de.haw_chat.server.network.Exceptions;

/**
 * Created by dima on 10.11.15.
 */
public class MediaFileTooBigException extends Exception {

    public MediaFileTooBigException(String message){
        super(message);
    }

    public MediaFileTooBigException(){
        super();
    }
}
