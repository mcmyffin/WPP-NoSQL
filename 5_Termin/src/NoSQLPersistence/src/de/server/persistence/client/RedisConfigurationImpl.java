package de.server.persistence.client;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author nosql
 */
public class RedisConfigurationImpl implements RedisConfiguration{

    private final String serverAdress;
    
    /**
     * Create Redis Configuration.
     * 
     * @param serverAdress Host Adress from Redis Server
     */
    RedisConfigurationImpl(String serverAdress){
        checkNotNull(serverAdress);
        this.serverAdress = serverAdress;
    }
    
    @Override
    public String getAdress() {
        return this.serverAdress;
    }
    
}
