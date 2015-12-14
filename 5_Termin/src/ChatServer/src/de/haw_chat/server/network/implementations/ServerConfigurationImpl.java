package de.haw_chat.server.network.implementations;

import de.haw_chat.server.network.interfaces.ServerConfiguration;

import java.util.Properties;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Dima on 14.12.2015
 */
final class ServerConfigurationImpl implements ServerConfiguration {
    private final int serverPort;
    private final int maxThreads;
    private final boolean sslEnabled;

    private ServerConfigurationImpl(int serverPort, int maxThreads, boolean sslEnabled) {
        this.serverPort = serverPort;
        this.maxThreads = maxThreads;
        this.sslEnabled = sslEnabled;
    }

    public static ServerConfiguration create(int serverPort, int maxThreads, boolean sslEnabled) {
        return new ServerConfigurationImpl(serverPort, maxThreads, sslEnabled);
    }

    public static ServerConfiguration create(Properties properties) {
        checkNotNull(properties, "properties is null!");
        checkArgument(properties.containsKey("SERVER_PORT"), "properties does not contain key 'SERVER_PORT'!");
        checkArgument(properties.containsKey("MAX_THREADS"), "properties does not contain key 'MAX_THREADS'!");
        checkArgument(properties.containsKey("SSL_ENABLED"), "properties does not contain key 'SSL_ENABLED'!");

        int serverPort = Integer.parseInt(properties.getProperty("SERVER_PORT"));
        int maxThreads = Integer.parseInt(properties.getProperty("MAX_THREADS"));
        boolean sslEnabled = Boolean.parseBoolean(properties.getProperty("SSL_ENABLED"));

        return create(serverPort, maxThreads, sslEnabled);
    }

    @Override
    public int getServerPort() {
        return serverPort;
    }

    @Override
    public int getMaxClients() {
        return maxThreads;
    }

    @Override
    public boolean isSslEnabled() {
        return sslEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerConfigurationImpl)) return false;

        ServerConfigurationImpl that = (ServerConfigurationImpl) o;

        if (getServerPort() != that.getServerPort()) return false;
        if (getMaxClients() != that.getMaxClients()) return false;
        return isSslEnabled() == that.isSslEnabled();

    }

    @Override
    public int hashCode() {
        int result = getServerPort();
        result = 31 * result + getMaxClients();
        result = 31 * result + (isSslEnabled() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServerConfiguration{" +
                "serverPort=" + serverPort +
                ", maxThreads=" + maxThreads +
                ", sslEnabled=" + sslEnabled +
                '}';
    }
}
