package de.server.persistence.Exceptions;

/**
 *
 * @author nosql
 */
public class ChatAlreadyExistsException extends Exception{
    
    public ChatAlreadyExistsException(){
        super();
    }
    
    public ChatAlreadyExistsException(String message){
        super(message);
    }
    
}
