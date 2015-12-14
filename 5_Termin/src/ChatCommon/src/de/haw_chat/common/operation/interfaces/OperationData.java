package de.haw_chat.common.operation.interfaces;

/**
 * Created by Andreas on 30.10.2015.
 */
public interface OperationData {
    boolean isClientOperation();
    int getOperationCode();
    String getOperationName();
}
