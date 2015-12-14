package de.haw_chat.server.network.Exceptions;

/**
 * Created by dima on 10.11.15.
 */
public class PermissionDeniedExeption extends Exception {

    public PermissionDeniedExeption(String message){
        super(message);
    }

    public PermissionDeniedExeption(){
        super();
    }
}
