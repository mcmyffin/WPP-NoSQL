package de.client.Chat;

import java.util.Map;

/**
 *
 * @author dima
 */
public interface IEval {
    
    
    public void calcEvaluation();
    
    public boolean isRunning();
    public Thread getThread();
    
    public Map<String,Long> getCountWordMap();
    public Map<String,Long> getCountUserMassageMap();
    public Map<Integer,Long> getMessagePerHourMap();
    
    public long getWordCount();
}
