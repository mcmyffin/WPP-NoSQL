package de.server.persistence.result;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author nosql
 */
public class MessageDataImpl implements MessageData{

    private final long chatID;
    private final String fromUser;
    private final String toUser;
    private final long timeStamp;
    private final String message;
    private final long mediaID;
    
    // Meta Data
    private final String fromUserLocation;
    private final String fromUserWeather;
    
    private final String toUserLocation;
    private final String toUserWeather;

    public MessageDataImpl(long chatID, String fromUser, String toUser, long timeStamp, String message, long mediaID,
                                String fromUserLoc, String fromUserWeather, String toUserLoc, String toUserWeather) {
        checkNotNull(fromUser);
        checkNotNull(toUser);
        checkNotNull(message);
        
        this.chatID = chatID;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.timeStamp = timeStamp;
        this.message = message;
        this.mediaID = mediaID;
        
        this.fromUserLocation = fromUserLoc;
        this.fromUserWeather  = fromUserWeather;
        this.toUserLocation   = toUserLoc;
        this.toUserWeather    = toUserWeather;
    }
    
    
    
    @Override
    public String getFromUser() {
        return fromUser;
    }

    @Override
    public long getChatID() {
        return chatID;
    }

    @Override
    public String getToUser() {
        return toUser;
    }

    @Override
    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public long getMediaID() {
        return mediaID;
    }

    @Override
    public String getFromUserLocation() {
        return fromUserLocation;
    }

    @Override
    public String getFromUserWeather() {
        return fromUserWeather;
    }

    @Override
    public String getToUserLocation() {
        return toUserLocation;
    }

    @Override
    public String getToUserWeather() {
        return toUserWeather;
    }
    
}
