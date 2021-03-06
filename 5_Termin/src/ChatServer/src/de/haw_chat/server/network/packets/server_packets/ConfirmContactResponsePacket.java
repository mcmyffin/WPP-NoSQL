package de.haw_chat.server.network.packets.server_packets;

import de.haw_chat.common.operation.implementations.Status;

/**
 * Created by Andreas on 31.10.2015.
 */
public class ConfirmContactResponsePacket extends AbstractServerPacket {
    private Status statusCode;

    public ConfirmContactResponsePacket(Status statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toMessageString() {
        return "" + getOperationCode() + " " + statusCode.getStatusCode();
    }
}
