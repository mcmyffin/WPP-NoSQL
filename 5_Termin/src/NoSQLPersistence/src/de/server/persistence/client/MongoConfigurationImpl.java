package de.server.persistence.client;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author nosql
 */
public class MongoConfigurationImpl implements MongoConfiguration{

    private final String serverAdress;
    private final String serverDB;
    private final String userCollectionName;
    private final String contactCollectionName;
    private final String chatCollectionName;
    
    MongoConfigurationImpl(String serverAdress, String serverDB, 
                            String userCollectionName, 
                            String contactCollectionName,
                            String chatCollectionName) {
        checkNotNull(serverAdress);
        checkNotNull(serverDB);
        checkNotNull(userCollectionName);
        checkNotNull(contactCollectionName);
        checkNotNull(chatCollectionName);
        
        this.serverAdress = serverAdress;
        this.serverDB     = serverDB;
        this.userCollectionName     = userCollectionName;
        this.contactCollectionName  = contactCollectionName;
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
    public String getUserCollectionName() {
        return this.userCollectionName;
    }

    @Override
    public String getContactCollectionName() {
        return this.contactCollectionName;
    }

    @Override
    public String getChatCollectionName() {
        return this.chatCollectionName;
    }

    
    
}
