package de.haw_chat.server.network.Exceptions;

/**
 * Created by dima on 10.11.15.
 */
public class ChatroomNotFoundExeption extends Exception {

    public ChatroomNotFoundExeption(String message){
        super(message);
    }

    public ChatroomNotFoundExeption(){
        super();
    }
}
