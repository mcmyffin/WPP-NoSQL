package de.server.persistence.result;

/**
 *
 * @author nosql
 */
public interface MessageData {
    
    public long   getChatID();
    public String getFromUser();
    public String getToUser();
    
    public long getTimeStamp();
    public String getMessage();
    public long getMediaID();
    
    // Meta Data
    public String getFromUserLocation();
    public String getFromUserWeather();
    
    public String getToUserLocation();
    public String getToUserWeather();
}
