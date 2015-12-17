package de.server.persistence.client.Exeptions;

/**
 *
 * @author nosql
 */
public class DataBaseNotFoundException extends Exception{
    
    public DataBaseNotFoundException(){
        super();
    }
    
    public DataBaseNotFoundException(String message){
        super(message);
    }
    
}
