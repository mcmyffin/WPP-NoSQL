package de.client.Chat;

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
    
    public void showInfoUser(String username);

    public Set<String> getUsernames();
}
