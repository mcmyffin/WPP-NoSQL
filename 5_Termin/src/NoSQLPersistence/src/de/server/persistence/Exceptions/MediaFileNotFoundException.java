package de.server.persistence.Exceptions;

/**
 *
 * @author nosql
 */
public class MediaFileNotFoundException extends Exception{
    
    public MediaFileNotFoundException(){
        super();
    }
    
    public MediaFileNotFoundException(String message){
        super(message);
    }
    
}
