package chatty.sniff;

import de.server.persistence.ServerPersistence;
import de.server.persistence.ServerPersistenceImpl;
import de.server.persistence.client.*;
import de.server.persistence.client.Exeptions.DataBaseNotFoundException;

import java.net.UnknownHostException;

/**
 * Created by dima on 18.12.15.
 */
public class Sniff {

    private static ServerPersistence serverPersistence = null;
    private static ServerPersistence getServerPersistence() throws DataBaseNotFoundException, UnknownHostException {
        if(serverPersistence != null) return serverPersistence;

        String               redisAdress = "localhost";
        String               mongoAdress = "127.0.0.1";
        String               mongo_db    = "test";
        String               mongo_c_user    = "userCollection";
        String               mongo_c_contacts= "contactsCollection";
        String               mongo_c_chat    = "chatCollection";


        RedisConfiguration r_conf = ClientFactory.createRedisDBConfiguration(redisAdress);
        MongoConfiguration m_conf = ClientFactory.createMongoDBConfigruation(mongoAdress,mongo_db,mongo_c_user,mongo_c_contacts,mongo_c_chat);

        RedisClient r_client = ClientFactory.createRedisClient(r_conf);
        MongoClient m_client = ClientFactory.createMongoClient(m_conf);

        serverPersistence = new ServerPersistenceImpl(r_client,m_client);
        return serverPersistence;
    }

    public synchronized static void addData(long timeStamp, String user, String message){


        try {
            ServerPersistence serverPersistence = getServerPersistence();
            serverPersistence.messageSend(timeStamp,user,message);
        } catch (DataBaseNotFoundException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
