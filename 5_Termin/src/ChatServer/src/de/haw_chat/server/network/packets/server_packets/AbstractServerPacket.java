package de.haw_chat.server.network.packets.server_packets;

import static de.haw_chat.common.operation.implementations.OperationDataManager.getOperationData;

/**
 * Created by Andreas on 31.10.2015.
 */
public abstract class AbstractServerPacket {
    private String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    }

    protected final int getOperationCode() {
        String operationName = replaceLast(this.getClass().getSimpleName(), "Packet", "");
        return getOperationData(operationName).getOperationCode();
    }

    public abstract String toMessageString();
}
