package de.haw_chat.server.network.implementations;

import com.sun.javaws.exceptions.InvalidArgumentException;
import de.haw_chat.common.operation.implementations.OperationDataManager;
import de.haw_chat.common.operation.interfaces.OperationData;
import de.haw_chat.server.network.Exceptions.IllegalArgumentPacketException;
import de.haw_chat.server.network.interfaces.ClientData;
import de.haw_chat.server.network.interfaces.ClientThread;
import de.haw_chat.server.network.interfaces.Server;
import de.haw_chat.server.network.packets.client_packets.AbstractClientPacket;
import de.haw_chat.server.network.packets.client_packets.LogoutPacket;
import de.haw_chat.server.network.packets.server_packets.AbstractServerPacket;
import de.haw_chat.server.network.packets.server_packets.InvalidPacket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import static com.google.common.base.Preconditions.checkNotNull;
import static de.haw_chat.common.operation.implementations.OperationDataManager.getOperationData;
import static de.haw_chat.server.network.implementations.DeviceFactory.createChatClientData;

/**
 * Created by Dima on 14.12.2015
 */
final class ClientThreadImpl implements ClientThread {

    private final int clientId;
    private final Socket clientSocket;
    private final Server server;

    private BufferedReader inFromClient;
    private DataOutputStream outToClient;

    private boolean workerServiceRequested = true;
    private ClientData clientData;

    private ClientThreadImpl(int clientId, Socket clientSocket, Server server) {
        checkNotNull(clientSocket);
        checkNotNull(server);

        this.clientId = clientId;
        this.clientSocket = clientSocket;
        this.server = server;
        this.inFromClient = null;
        this.outToClient = null;
        this.workerServiceRequested = true;
        this.clientData = createChatClientData();
    }

    public static ClientThread create(int clientId, Socket clientSocket, Server server) {
        return new ClientThreadImpl(clientId, clientSocket, server);
    }

    @Override
    public int getClientId() {
        return clientId;
    }

    @Override
    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public boolean isWorkerServiceRequested() {
        return workerServiceRequested;
    }

    @Override
    public void setWorkerServiceRequested(boolean workerServiceRequested) {
        this.workerServiceRequested = workerServiceRequested;
    }

    @Override
    public ClientData getData() {
        return clientData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientThreadImpl)) return false;

        ClientThreadImpl that = (ClientThreadImpl) o;

        return getClientId() == that.getClientId();

    }

    @Override
    public int hashCode() {
        return getClientId();
    }

    @Override
    public String toString() {
        return "ClientThread{" +
                "clientId=" + clientId +
                ", clientSocket=" + clientSocket +
                ", server=" + server +
                ", workerServiceRequested=" + workerServiceRequested +
                ", clientData=" + clientData +
                '}';
    }

    private AbstractClientPacket readFromClient() throws IOException {
        String request = inFromClient.readLine();
        System.out.println("TCP Worker Thread " + clientId + " detected job: " + request);

        final String PACKET_CLASS_PREFIX = AbstractClientPacket.class.getPackage().getName() + ".";
        final String PACKET_CLASS_POSTFIX = "Packet";

        int operationCode = -1;
        try {
            operationCode = Integer.parseInt(request.split(" ")[0]);
        }catch (NumberFormatException ex){}
        OperationData operationData = getOperationData(operationCode);
        if (!operationData.isClientOperation()) {
            System.err.println("[WARNING] client tried to send server packet! request '" + request + "' ignored!");
            return null;
        }

        String operationName = operationData.getOperationName();
        String className = PACKET_CLASS_PREFIX + operationName + PACKET_CLASS_POSTFIX;

        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> ctor = clazz.getConstructor(ClientThread.class, String.class);
            AbstractClientPacket object = (AbstractClientPacket) ctor.newInstance(this, request);
            return object;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Throwable throwable = e.getCause();
            if(throwable instanceof IllegalArgumentPacketException){
                String messageString = ((IllegalArgumentPacketException) throwable).getMessage();
                AbstractServerPacket packet = new InvalidPacket(messageString);
                writeToClient(packet);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        System.err.println("[WARNING] error while creating client packet! request '" + request + "' ignored!");
        return null;
    }

    @Override
    public synchronized void writeToClient(AbstractServerPacket serverPacket){
        checkNotNull(serverPacket, "serverPacket is null!");
        String reply = serverPacket.toMessageString();
        try {
            outToClient.writeBytes(reply + '\r' + '\n');
            System.out.println("TCP Worker Thread " + clientId + " has written the message: " + reply);
        } catch (IOException e) {
            // will be already cleanup in logout
            System.out.println("TCP connection lost, packet can not be written");
        }

    }

    @Override
    public void run() {
        try {
            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outToClient = new DataOutputStream(clientSocket.getOutputStream());

            while (workerServiceRequested) {
                AbstractClientPacket clientPacket = readFromClient();
                if (clientPacket != null)
                    clientPacket.process();
            }

            clientSocket.close();
        } catch (IOException|NullPointerException  e) {
            // simulate logout by client connection lost
            LogoutPacket packet = new LogoutPacket(this, Integer.toString(OperationDataManager.getOperationData("Logout").getOperationCode()));
            packet.process();
            System.out.println("TCP Thread " + clientId + " close connection");
        } finally {
            server.getClientThreadsSemaphore().release();
        }
    }
}
