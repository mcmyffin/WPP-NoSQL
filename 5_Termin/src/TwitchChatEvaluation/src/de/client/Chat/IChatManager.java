package de.client.Chat;

import de.server.persistence.result.MessageData;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author dima
 */
public interface IChatManager {

    public Number getDataCount();

    public String getTimeRange();

    public Number getWordCount();

    public Number getUserCount();

    public List<String> getUsernames();

    public void startCalc();

    public Thread getThread();

    public List<MessageData> getServerList();

    public Map<Integer, Long> getMessagePerHourMap();

    public Queue<String> getCountWordsQueue();

    public Map<String, Long> getCoutWordsMap();

    
}
