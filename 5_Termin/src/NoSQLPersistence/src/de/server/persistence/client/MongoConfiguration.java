package de.server.persistence.client;

/**
 *
 * @author nosql
 */
public interface MongoConfiguration {
    
    /**
     * MongoDB Server Adress
     * 
     * @return serverAdress
     */
    public String getAdress();
    
    /**
     * MongoDB Database Name.
     * 
     * @return dataBase Name
     */
    public String getDB();
    
   
    /**
     * MongoDB Collection Name.
     * 
     * @return ChatCollection Name
     */
    public String getChatCollectionName();
}
