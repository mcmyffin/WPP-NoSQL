package de.server.persistence.result;

import java.util.Map;

/**
 *
 * @author dima
 */
public interface HourActivity {

    public Map<Integer,Long> getMessagePerHourMap();
    
    public void addActivity(int hour) throws Exception;
}
