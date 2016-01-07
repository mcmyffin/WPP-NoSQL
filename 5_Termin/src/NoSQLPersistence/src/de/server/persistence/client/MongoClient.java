package de.server.persistence.client;

import com.mongodb.*;
import java.net.UnknownHostException;

/**
 *
 * @author nosql
 */
public interface MongoClient {

    public void reconnect() throws UnknownHostException;
    public boolean isConnected();
    public void disconnect();
    
    public Mongo getMongo();
    public DB getDataBase();
    public DBCollection getUserCollection();
    public DBCollection getContactCollection();
    public DBCollection getChatCollection();
    public MongoConfiguration getConfiguration();
}
