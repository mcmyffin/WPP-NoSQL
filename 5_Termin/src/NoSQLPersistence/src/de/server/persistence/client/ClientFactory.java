package de.server.persistence.client;

import static com.google.common.base.Preconditions.checkNotNull;
import de.server.persistence.client.Exeptions.DataBaseNotFoundException;
import java.net.UnknownHostException;

/**
 *
 * @author nosql
 */
public class ClientFactory {
    
    /**
     * Create MongoDB Configuration.
     * 
     * @param serverAdress  HostName or IP from Host
     * @param serverDB      DataBase Name from MongoDB
     * @param chatCollectionName    ChatCollection name from MongoDB
     * @return MongoConfiguration
     */
    public static MongoConfiguration createMongoDBConfigruation(String serverAdress,
                                                         String serverDB,
                                                         String chatCollectionName){
        checkNotNull(serverAdress);
        checkNotNull(serverDB);
        checkNotNull(chatCollectionName);
        return new MongoConfigurationImpl(serverAdress, serverDB, chatCollectionName);
    }
    
    public static MongoClient createMongoClient(MongoConfiguration conf) throws UnknownHostException, DataBaseNotFoundException{
        checkNotNull(conf);
        return new MongoClientImpl(conf);
    }
    
}
