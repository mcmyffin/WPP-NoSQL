package de.server.persistence.Exceptions;

/**
 *
 * @author nosql
 */
public class MediaFileTooBigException extends Exception{
    
    public MediaFileTooBigException(){
        super();
    }
    
    public MediaFileTooBigException(String message){
        super(message);
    }
    
}
