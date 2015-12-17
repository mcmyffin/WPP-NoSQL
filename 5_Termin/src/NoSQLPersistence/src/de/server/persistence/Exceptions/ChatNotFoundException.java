package de.server.persistence.Exceptions;

/**
 *
 * @author nosql
 */
public class ChatNotFoundException extends Exception{
    
    public ChatNotFoundException(){
        super();
    }
    
    public ChatNotFoundException(String message){
        super(message);
    }
    
}
