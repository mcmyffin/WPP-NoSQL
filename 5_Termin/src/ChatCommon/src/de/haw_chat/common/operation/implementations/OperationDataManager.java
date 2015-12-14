package de.haw_chat.common.operation.implementations;

import de.haw_chat.common.operation.interfaces.OperationData;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Andreas on 30.10.2015.
 */
public final class OperationDataManager {
    private final static boolean CLIENT = true;
    private final static boolean SERVER = false;

    private static Map<Integer, OperationData> operationCodeMap;
    private static Map<String, OperationData> operationNameMap;

    static {
        operationCodeMap = new HashMap<>();
        operationNameMap = new HashMap<>();
        storeOperationDatas();
    }

    private static void storeOperationData(boolean clientOperation, int operationCode, String operationName) {
        checkArgument(!operationCodeMap.containsKey(operationCode), "duplicate operationCode '" + operationCode + "'!");
        checkArgument(!operationNameMap.containsKey(operationName), "duplicate operationName '" + operationName + "'!");

        OperationData operationData = OperationDataImpl.create(clientOperation, operationCode, operationName);
        operationCodeMap.put(operationCode, operationData);
        operationNameMap.put(operationName, operationData);
    }

    private static void storeOperationDatas() {
        // Client
        storeOperationData(CLIENT, 109, "UnknownClient");	
        storeOperationData(CLIENT, 110, "Login");
        storeOperationData(CLIENT, 111, "Logout");
        storeOperationData(CLIENT, 112, "RequestChatList");
        storeOperationData(CLIENT, 113, "ChatCreate");
        storeOperationData(CLIENT, 114, "ChatDelete");
        storeOperationData(CLIENT, 115, "MessageSend");
        storeOperationData(CLIENT, 117, "RegisterAccount");
        storeOperationData(CLIENT, 118, "RequestOwnContactList");
        storeOperationData(CLIENT, 119, "RequestMedia");
        storeOperationData(CLIENT, 120, "RequestContactList");
        storeOperationData(CLIENT, 121, "RequestConfirmContact");

        // Server
        storeOperationData(SERVER, 210, "Unknown");
        storeOperationData(SERVER, 211, "Invalid");
        storeOperationData(SERVER, 212, "LoginResponse");
        storeOperationData(SERVER, 213, "LogoutResponse");
        storeOperationData(SERVER, 214, "ChatListBegin");
        storeOperationData(SERVER, 215, "ChatListElem");
        storeOperationData(SERVER, 216, "ChatListEnd");
        storeOperationData(SERVER, 217, "ChatCreateResponse");
        storeOperationData(SERVER, 218, "ChatDeleteResponse");
        storeOperationData(SERVER, 219, "MessageSendResponse");
        storeOperationData(SERVER, 220, "MessageSended");
        storeOperationData(SERVER, 222, "RegisterAccountResponse");
        storeOperationData(SERVER, 223, "OwnContactListResponse");
        storeOperationData(SERVER, 224, "ContactOwnListBegin");
        storeOperationData(SERVER, 225, "ContactOwnListElem");
        storeOperationData(SERVER, 226, "ContactOwnListEnd");
        storeOperationData(SERVER, 227, "MediaResponse");
        storeOperationData(SERVER, 228, "ContactListResponse");
        storeOperationData(SERVER, 229, "ContactListBegin");
        storeOperationData(SERVER, 230, "ContactListElem");
        storeOperationData(SERVER, 231, "ContactListEnd");
        storeOperationData(SERVER, 232, "ConfirmContactResponse");
        storeOperationData(SERVER, 233, "ConfirmContactAdd");
    }

    public static OperationData getOperationData(int operationCode) {
        if (!operationCodeMap.containsKey(operationCode))
            return getOperationData("UnknownClient");
        return operationCodeMap.get(operationCode);
    }

    public static OperationData getOperationData(String operationName) {
        if (!operationNameMap.containsKey(operationName))
            return getOperationData("UnknownClient");
        return operationNameMap.get(operationName);
    }
}
