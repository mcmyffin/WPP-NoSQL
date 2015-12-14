package de.haw_chat.server.network.implementations;

import de.haw_chat.server.network.interfaces.ClientThread;
import de.haw_chat.server.network.interfaces.Server;
import de.haw_chat.server.network.interfaces.ServerConfiguration;
import de.haw_chat.server.network.utils.IpChecker;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Dima on 14.12.2015
 */
final class ServerImpl implements Server {

    private static ServerImpl instance;
    private final ServerConfiguration configuration;
    private ServerData serverData;
    private Semaphore clientThreadsSemaphore;

    private ServerImpl(ServerConfiguration configuration) {
        checkNotNull(configuration);
        this.configuration = configuration;
        this.serverData = null;
        this.clientThreadsSemaphore = null;
    }

    public static Server create(ServerConfiguration configuration) {
        checkNotNull(configuration);
        if(instance == null) instance = new ServerImpl(configuration);
        return instance;
    }

    @Override
    public ServerConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public ServerData getData() {
        return serverData;
    }

    @Override
    public Semaphore getClientThreadsSemaphore() {
        return clientThreadsSemaphore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerImpl)) return false;
        ServerImpl that = (ServerImpl) o;
        if (getConfiguration() != null ? !getConfiguration().equals(that.getConfiguration()) : that.getConfiguration() != null)
            return false;
        if (serverData != null ? !serverData.equals(that.serverData) : that.serverData != null)
            return false;
        return !(getClientThreadsSemaphore() != null ? !getClientThreadsSemaphore().equals(that.getClientThreadsSemaphore()) : that.getClientThreadsSemaphore() != null);

    }

    @Override
    public int hashCode() {
        int result = getConfiguration() != null ? getConfiguration().hashCode() : 0;
        result = 31 * result + (serverData != null ? serverData.hashCode() : 0);
        result = 31 * result + (getClientThreadsSemaphore() != null ? getClientThreadsSemaphore().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Server{" +
                "configuration=" + configuration +
                ", serverData=" + serverData +
                ", clientThreadsSemaphore=" + clientThreadsSemaphore +
                '}';
    }

    @Override
    public void run() {
        if (configuration.isSslEnabled()) {
            throw new UnsupportedOperationException("SSL support currently not implemented!");
        }

        final int serverPort = configuration.getServerPort();
        clientThreadsSemaphore = new Semaphore(configuration.getMaxClients());
        serverData = ServerData.getInstance();

        ServerSocket welcomeSocket;
        Socket connectionSocket;

        boolean serviceRequested = true;
        int nextThreadNumber = 0;

        try {
            if (configuration.isSslEnabled()) {
                SSLServerSocketFactory sslserversocketfactory =
                        (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
                welcomeSocket = sslserversocketfactory.createServerSocket(serverPort);
            } else {
                welcomeSocket = new ServerSocket(serverPort);
            }

            while (serviceRequested) {
                clientThreadsSemaphore.acquire();

                System.out.println("TCP Server is waiting for connection - \n" +
                        "IP: "+ IpChecker.getIp()+"\n"+
                        "Port: " + serverPort);

                connectionSocket = welcomeSocket.accept();
                ClientThread clientThread = DeviceFactory.createChatClientThread(nextThreadNumber, connectionSocket, this);
                nextThreadNumber++;

                Thread thread = new Thread(clientThread);
                thread.start();

                String ip = connectionSocket.getInetAddress().toString();
                System.out.println("New Client [IP: "+ip+"]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
