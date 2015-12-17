package de.server.persistence.client;

import de.server.persistence.client.Exeptions.KeyAlreadyExistsException;
import de.server.persistence.client.Exeptions.KeyNotFoundExeption;
import de.server.persistence.client.Exeptions.ValueIsTooBigException;

/**
 *
 * @author nosql
 */
public interface RedisClient {

    public boolean isConnected();
    public void connect();
    public void disconnect();
    
    public String get(String key) throws KeyNotFoundExeption;
    public boolean put(String key, String value) throws ValueIsTooBigException;
    
    public boolean flushAll();
    public boolean containsKey(String key);
}
