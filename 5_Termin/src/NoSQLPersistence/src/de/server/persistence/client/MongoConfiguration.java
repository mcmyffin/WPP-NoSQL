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
     * @return UserCollection Name
     */
    public String getUserCollectionName();
    
    /**
     * MongoDB Collection Name.
     * 
     * @return ContactCollection Name
     */
    public String getContactCollectionName();
    
    /**
     * MongoDB Collection Name.
     * 
     * @return ChatCollection Name
     */
    public String getChatCollectionName();
}
