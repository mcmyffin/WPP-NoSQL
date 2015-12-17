package de.server.persistence.Exceptions;

/**
 *
 * @author nosql
 */
public class MediaFilePermissionDenied extends Exception{
    
    public MediaFilePermissionDenied(){
        super();
    }
    
    public MediaFilePermissionDenied(String message){
        super(message);
    }
    
}
