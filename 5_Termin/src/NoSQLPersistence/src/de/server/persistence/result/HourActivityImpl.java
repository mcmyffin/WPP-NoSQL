package de.server.persistence.result;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dima
 */
public class HourActivityImpl implements HourActivity{

    private final Map<Integer,Long> activity;
    
    public HourActivityImpl(){
        activity = new HashMap();
        initMap();
    }
    
    private void initMap(){
        for(int i=0; i < 24; i++){
            activity.put(i, 0L);
        }
    }
    
    
    
    @Override
    public Map<Integer, Long> getMessagePerHourMap() {
        return activity;
    }

    @Override
    public void addActivity(int hour) throws Exception{
        if(hour < 0 || hour > 23) throw new Exception("Wrong Time format. Between 0-23");
        activity.put(hour,activity.get(hour)+1);
    }
    
}
