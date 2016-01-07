package de.server.persistence.result;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dima
 */
public class UserDataImpl implements UserData{

    private final String name;
    private final Map<String,Long>  countWordMap;
    private final List<MessageData> messageList;
    
    public UserDataImpl(String name, Map<String,Long> countWordMap, 
                        List<MessageData> list){
        checkNotNull(name);
        checkNotNull(list);
        checkNotNull(countWordMap);
        
        this.name = name;
        this.countWordMap = countWordMap;
        this.messageList = list;
    }
    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<MessageData> getMessages() {
        return this.messageList;
    }

    @Override
    public Map<String, Long> getCountWordMap() {
        return this.countWordMap;
    }
}
