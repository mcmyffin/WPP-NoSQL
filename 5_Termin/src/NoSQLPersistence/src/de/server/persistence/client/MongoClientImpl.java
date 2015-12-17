package de.server.persistence.client;

import static com.google.common.base.Preconditions.checkNotNull;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import de.server.persistence.client.Exeptions.DataBaseNotFoundException;
import java.net.UnknownHostException;

/**
 *
 * @author nosql
 */
public class MongoClientImpl implements MongoClient{
    
    private Mongo client;
    private DB database;
    private DBCollection userCollection;
    private DBCollection contactCollection;
    private DBCollection chatCollection;
    private final MongoConfiguration conf;

    public MongoClientImpl(MongoConfiguration conf) throws UnknownHostException, DataBaseNotFoundException {
        checkNotNull(conf);
        this.conf   = conf;
        this.client = new Mongo(conf.getAdress());
        if(!isDBExists(conf.getDB())) throw new DataBaseNotFoundException();
        init();
    }
    
    private void init(){
        this.database = client.getDB(conf.getDB());
        if(database.collectionExists(conf.getUserCollectionName()))   
            userCollection   = database.getCollection(conf.getUserCollectionName());
        if(database.collectionExists(conf.getContactCollectionName()))
            contactCollection= database.getCollection(conf.getContactCollectionName());
        if(database.collectionExists(conf.getChatCollectionName()))   
            chatCollection   = database.getCollection(conf.getChatCollectionName());

        if(userCollection    == null) userCollection   = database.createCollection(conf.getUserCollectionName(),null);
        if(contactCollection == null) contactCollection= database.createCollection(conf.getContactCollectionName(),null);
        if(chatCollection    == null) chatCollection   = database.createCollection(conf.getChatCollectionName(),null);
    }
    
    private boolean isDBExists(String databaseName){
        return client.getDatabaseNames().contains(databaseName);
    }

    @Override
    public void reconnect() throws UnknownHostException{
        this.client = new Mongo(conf.getAdress());
        init();
    }
    
    @Override
    public boolean isConnected() {
        return client.getConnector().isOpen();
    }

    @Override
    public void disconnect() {
        client.getConnector().close();
    }

    @Override
    public Mongo getMongo() {
        return client;
    }

    @Override
    public DB getDataBase() {
        return database;
    } 
   
    @Override
    public DBCollection getUserCollection() {
        return userCollection;
    }

    @Override
    public DBCollection getContactCollection() {
        return contactCollection;
    }

    @Override
    public DBCollection getChatCollection() {
        return chatCollection;
    }
}
