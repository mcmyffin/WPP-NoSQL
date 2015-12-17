package de.server.persistence.Exceptions;

/**
 *
 * @author nosql
 */
public class UserNotFoundException extends Exception{
    
    public UserNotFoundException(){
        super();
    }
    
    public UserNotFoundException(String message){
        super(message);
    }
    
}
