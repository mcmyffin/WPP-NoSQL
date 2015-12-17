package de.server.persistence.client.Exeptions;

/**
 *
 * @author nosql
 */
public class ValueIsTooBigException extends Exception{
    
    public ValueIsTooBigException(){
        super();
    }
    
    public ValueIsTooBigException(String message){
        super(message);
    }
    
}
