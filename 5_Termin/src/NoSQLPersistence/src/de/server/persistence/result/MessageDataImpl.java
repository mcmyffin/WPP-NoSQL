package de.server.persistence.result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author nosql
 */
public class MessageDataImpl implements MessageData{

    private final long timeStamp;
    private final String user;
    private final String message;

    public MessageDataImpl(String user, long timeStamp, String message) {
        checkNotNull(user);
        checkNotNull(message);
        
        this.user = user;
        this.timeStamp = timeStamp;
        this.message = message;
    }
    
    
    
    @Override
    public String getUser() {
        return user;
    }

    @Override
    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String getMessage() {
        return message;
    }    
}
