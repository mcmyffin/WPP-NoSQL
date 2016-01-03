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
    private final HourActivity activity;
    
    public UserDataImpl(String name, Map<String,Long> countWordMap, 
                        List<MessageData> list, HourActivity activity){
        checkNotNull(name);
        checkNotNull(list);
        checkNotNull(activity);
        checkNotNull(countWordMap);
        
        this.name = name;
        this.countWordMap = countWordMap;
        this.messageList = list;
        this.activity = activity;
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
    public HourActivity getActivity() {
        return this.activity;
    }

    @Override
    public Map<String, Long> getCountWordMap() {
        return this.countWordMap;
    }
}
