package de.server.persistence.client.Exeptions;

/**
 *
 * @author nosql
 */
public class KeyNotFoundExeption extends Exception{
    
    public KeyNotFoundExeption(){
        super();
    }
    
    public KeyNotFoundExeption(String message){
        super(message);
    }
    
}
