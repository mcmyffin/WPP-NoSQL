package de.haw_chat.common.operation.implementations;

import de.haw_chat.common.operation.interfaces.OperationData;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Andreas on 30.10.2015.
 */
final class OperationDataImpl implements OperationData {
    private boolean clientOperation;
    private int operationCode;
    private String operationName;

    private OperationDataImpl(boolean clientOperation, int operationCode, String operationName) {
        checkNotNull(operationName, "operationName is null!");
        checkArgument(!operationName.isEmpty(), "operationName is empty!");

        this.clientOperation = clientOperation;
        this.operationCode = operationCode;
        this.operationName = operationName;
    }

    public static OperationData create(boolean clientOperation, int operationCode, String operationName) {
        return new OperationDataImpl(clientOperation, operationCode, operationName);
    }

    @Override
    public boolean isClientOperation() {
        return clientOperation;
    }

    @Override
    public int getOperationCode() {
        return operationCode;
    }

    @Override
    public String getOperationName() {
        return operationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperationDataImpl)) return false;

        OperationDataImpl that = (OperationDataImpl) o;

        return getOperationCode() == that.getOperationCode();

    }

    @Override
    public int hashCode() {
        return getOperationCode();
    }

    @Override
    public String toString() {
        return "OperationData{" +
                "clientOperation=" + clientOperation +
                ", operationCode=" + operationCode +
                ", operationName='" + operationName + '\'' +
                '}';
    }
}
