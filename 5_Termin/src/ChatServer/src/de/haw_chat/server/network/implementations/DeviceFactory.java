package de.haw_chat.server.network.implementations;

import de.haw_chat.server.network.interfaces.*;

import java.net.Socket;
import java.util.Properties;

/**
 * Created by Dima on 14.12.2015
 */
public final class DeviceFactory {
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

    static ServerData createChatServerData() {
        return ServerData.getInstance();
    }

    static ClientData createChatClientData() {
        return ClientDataImpl.create();
    }
}
