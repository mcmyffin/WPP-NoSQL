package de.server.persistence.client.Exeptions;

/**
 *
 * @author nosql
 */
public class KeyAlreadyExistsException extends Exception{
    
    public KeyAlreadyExistsException(){
        super();
    }
    
    public KeyAlreadyExistsException(String message){
        super(message);
    }
    
}
