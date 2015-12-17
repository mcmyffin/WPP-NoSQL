package de.server.persistence.client;

import static com.google.common.base.Preconditions.checkNotNull;
import de.server.persistence.client.Exeptions.KeyAlreadyExistsException;
import de.server.persistence.client.Exeptions.KeyNotFoundExeption;
import de.server.persistence.client.Exeptions.ValueIsTooBigException;
import java.net.UnknownHostException;
import redis.clients.jedis.Jedis;

/**
 *
 * @author nosql
 */
public class RedisClientImpl implements RedisClient{

    private final Jedis client;
    private final RedisConfiguration configuration;
    
    private final String ok = "OK";
    private final String not_fount = "nil";
    private final long maxValueSize= 1073741824;
    
    RedisClientImpl(RedisConfiguration configuration){
        this.configuration = configuration;
        client = new Jedis(configuration.getAdress());
    }

    
    @Override
    public synchronized boolean isConnected() {
        return client.isConnected();
    }

    @Override
    public synchronized void connect() {
        client.connect();
    }

    @Override
    public synchronized void disconnect() {
        client.disconnect();
    }

    @Override
    public synchronized String get(String key) throws KeyNotFoundExeption {
        checkNotNull(key);
        if(!containsKey(key)) throw new KeyNotFoundExeption();
        return client.get(key);
    }

    @Override
    public synchronized boolean put(String key, String value) throws ValueIsTooBigException {
        checkNotNull(key);
        checkNotNull(value);
        
        // contains key
        if(containsKey(key)) return false;
        if(value.getBytes().length > maxValueSize) throw new ValueIsTooBigException("Max size in Bytes '"+maxValueSize+"' !");
        
        String reply =  client.set(key,value);
        return reply.equals(ok);
    }

    @Override
    public synchronized boolean containsKey(String key) {
        checkNotNull(key);
        return client.exists(key);
    }

    @Override
    public boolean flushAll() {
        String reply = client.flushAll();
        return reply.equals(ok);
    }
}
