package de.haw_chat.server.network.implementations;

import de.haw_chat.server.network.interfaces.*;
import de.server.persistence.ServerPersistence;
import de.server.persistence.ServerPersistenceImpl;
import de.server.persistence.client.*;
import de.server.persistence.client.Exeptions.DataBaseNotFoundException;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Created by Dima on 14.12.2015
 */
public final class DeviceFactory {
    private static ServerPersistence serverPersistence;

    public static ServerConfiguration createChatServerConfiguration(int serverPort, int maxThreads, boolean sslEnabled) {
        return ServerConfigurationImpl.create(serverPort, maxThreads, sslEnabled);
    }

    public static ServerConfiguration createChatServerConfiguration(Properties properties) {
        return ServerConfigurationImpl.create(properties);
    }

    public static Server createChatServer(ServerConfiguration configuration) {
        return ServerImpl.create(configuration);
    }

    static ClientThread createChatClientThread(int clientId, Socket clientSocket, Server server) {
        return ClientThreadImpl.create(clientId, clientSocket, server);
    }

    public static ServerPersistence getServerPersistence() throws DataBaseNotFoundException, UnknownHostException {
        if(serverPersistence != null) return serverPersistence;

        String               redisAdress = "localhost";
        String               mongoAdress = "localhost";
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

    static ServerData createChatServerData() {
        return ServerData.getInstance();
    }

    static ClientData createChatClientData() {
        return ClientDataImpl.create();
    }
}
