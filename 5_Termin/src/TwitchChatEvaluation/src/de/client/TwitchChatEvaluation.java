package de.client;

import de.client.Chat.TwitchChat;
import de.client.Chat.TwitchChatManager;
import de.client.UI.Frame.MainFrame;
import de.client.UI.IPersistenceUI;
import de.client.UI.PersistenceUI;
import de.server.persistence.ServerPersistenceImpl;
import de.server.persistence.client.ClientFactory;
import de.server.persistence.client.Exeptions.DataBaseNotFoundException;
import de.server.persistence.client.MongoClient;
import de.server.persistence.client.MongoConfiguration;
import de.server.persistence.client.RedisClient;
import de.server.persistence.client.RedisConfiguration;
import java.net.UnknownHostException;

/**
 *
 * @author dima
 */
public class TwitchChatEvaluation {

    // TODO StartUp
    public static void main(String[] args) {
        
        // create MainFrame
        IPersistenceUI persistenceUI = new PersistenceUI();
        
        
//        try{
//            String               redisAdress = "localhost";
//            String               mongoAdress = "127.0.0.1";
//            String               mongo_db    = "test";
//            String               mongo_c_user    = "userCollection";
//            String               mongo_c_contacts= "contactsCollection";
//            String               mongo_c_chat    = "chatCollection";
//
//
//            RedisConfiguration r_conf = ClientFactory.createRedisDBConfiguration(redisAdress);
//            MongoConfiguration m_conf = ClientFactory.createMongoDBConfigruation(mongoAdress,mongo_db,mongo_c_user,mongo_c_contacts,mongo_c_chat);
//
//            RedisClient r_client = ClientFactory.createRedisClient(r_conf);
//            MongoClient m_client = ClientFactory.createMongoClient(m_conf);
//
//            ServerPersistenceImpl serverPersistence = new ServerPersistenceImpl(r_client,m_client);
//            TwitchChatManager manager = new TwitchChatManager(serverPersistence);
//            
//            manager.getThread().start();
//            
//            
//        }catch(DataBaseNotFoundException|UnknownHostException ex){
//            ex.printStackTrace();
//        }
    }
}
