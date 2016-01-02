package de.server.persistence.client;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author nosql
 */
public class MongoConfigurationImpl implements MongoConfiguration{

    private final String serverAdress;
    private final String serverDB;
    private final String chatCollectionName;
    
    MongoConfigurationImpl(String serverAdress, String serverDB,
                            String chatCollectionName) {
        checkNotNull(serverAdress);
        checkNotNull(serverDB);
        checkNotNull(chatCollectionName);
        
        this.serverAdress = serverAdress;
        this.serverDB     = serverDB;
        this.chatCollectionName     = chatCollectionName;
    }

    @Override
    public String getAdress() {
        return this.serverAdress;
    }

    @Override
    public String getDB() {
        return this.serverDB;
    }

    @Override
    public String getChatCollectionName() {
        return this.chatCollectionName;
    }

    
    
}
