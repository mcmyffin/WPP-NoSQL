package de.haw_chat.common.operation.implementations;

/**
 * Created by Andreas on 04.11.2015.
 */
public enum Status {
    OK,                                 // ALL PACKETS

    USERNAME_INVALID,                   // REGISTER_ACCOUNT_RESPONSE
    PASSWORD_INVALID,                   // REGISTER_ACCOUNT_RESPONSE
    USERNAME_ALREADY_TAKEN,             // REGISTER_ACCOUNT_RESPONSE


    CLIENT_ALREADY_LOGGED_IN,           // LOGIN_RESPONSE
    USERNAME_ALREADY_LOGGED_IN,         // LOGIN_RESPONSE
    PASSWORD_WRONG,                     // LOGIN_RESPONSE

    CLIENT_NOT_LOGGED_IN,               // ALLE BIS AUF LOGIN

    CHAT_NOT_FOUND,                     // MESSAGE_SEND 
    CHATROOM_PERMISSION_DENIED,         // MESSAGE_SEND, CHAT_DELETE, REQUEST_MEDIA
    CONTACT_NOT_FOUND,			        // CHAT_CREATE, CHAT_DELTE, MESSAGE_SEND
    CONTACT_NOT_CONFIRMED,		        // CHAT_CREATE
    MEDIA_NOT_FOUND;			        // REQUEST_MEDIA

    private static final int START_VALUE = 400;

    public static Status getStatus(int statusCode) {
        statusCode = statusCode - START_VALUE;
        return Status.values()[statusCode];
    }

    public static Status getStatus(String statusName) {
        return Status.valueOf(statusName);
    }

    public int getStatusCode() {
        return ordinal() + START_VALUE;
    }

    public String getStatusName() {
        return name();
    }
}
