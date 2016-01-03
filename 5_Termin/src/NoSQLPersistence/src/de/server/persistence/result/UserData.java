package de.server.persistence.result;

import java.util.List;
import java.util.Map;

/**
 *
 * @author dima
 */
public interface UserData {

    public String getName();
    public List<MessageData> getMessages();
    public Map<String,Long> getCountWordMap();
    public HourActivity getActivity();
}
