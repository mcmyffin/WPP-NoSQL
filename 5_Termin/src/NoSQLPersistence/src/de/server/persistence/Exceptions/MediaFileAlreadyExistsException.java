package de.server.persistence.Exceptions;

/**
 *
 * @author nosql
 */
public class MediaFileAlreadyExistsException extends Exception{
    
    public MediaFileAlreadyExistsException(){
        super();
    }
    
    public MediaFileAlreadyExistsException(String message){
        super(message);
    }
    
}
