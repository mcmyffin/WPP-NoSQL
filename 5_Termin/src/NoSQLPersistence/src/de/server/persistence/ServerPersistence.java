package de.server.persistence;

import de.server.persistence.result.MessageData;
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
    
    public List<MessageData> getMessages(int from, int to);
}
