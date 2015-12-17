package de.server.persistence.Exceptions;

/**
 *
 * @author nosql
 */
public class MediaUploadFileFailedException extends Exception{
    
    public MediaUploadFileFailedException(){
        super();
    }
    
    public MediaUploadFileFailedException(String message){
        super(message);
    }
    
}
