package de.server.persistence;

import de.server.persistence.Exceptions.UserNotFoundException;
import de.server.persistence.result.HourActivity;
import de.server.persistence.result.MessageData;
import de.server.persistence.result.UserData;
import java.util.List;

/**
 *
 * @author nosql
 */
public interface ServerPersistence {
    
   /**
     * Save sended Message
     * @param timeStamp
     * @param nick
     * @param message
   */
    public void messageSend(long timeStamp, String nick, String message);
   
    public List<MessageData> getAllMessages();
    
    
    public long getMessageCount();
    
    public void dropChatCollection();
    
    public String getDataTimeRange();
    
    public UserData getUserData(String username) throws UserNotFoundException;
    
    public HourActivity getHourActivityFromUser(String username) throws UserNotFoundException;
    
    public List<MessageData> getMessages(int from, int to);
    
    public ServerPersistence clone();
}
