package de.client.Chat;

import de.server.persistence.ServerPersistence;
import de.server.persistence.result.MessageData;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dima
 */
public class TwitchChat implements IEval, Runnable{

    private final Map<String,Long>  wordCountMap;
    private final Map<String,Long>  messageCountUserMap;
    private final Map<Integer,Long>  messageInHourMap;
    private long  wordCount = 0L;
    
    private final List<MessageData> list;
    private final int fromIndex;
    private final int toIndex;
    
    private final Thread thread;
    
    public TwitchChat(List<MessageData> list, int fromIndex, int toIndex){
        
        this.wordCountMap        = new LinkedHashMap(toIndex-fromIndex);
        this.messageCountUserMap = new LinkedHashMap(toIndex-fromIndex);
        this.messageInHourMap    = new LinkedHashMap(toIndex-fromIndex);
        this.list = list;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.thread = new Thread(this);
    }
    
    
    public TwitchChat(List<MessageData> list){
        
        this.fromIndex = 0;
        this.toIndex = list.size();
        this.wordCountMap        = new LinkedHashMap(toIndex);
        this.messageCountUserMap = new LinkedHashMap(toIndex);
        this.messageInHourMap    = new LinkedHashMap(toIndex);
        this.list = list;
        this.thread = new Thread(this);
    }
    
    
    
    
    public void calcEvaluation_(){
        
        for(int i = fromIndex; i < toIndex; i++){
            MessageData data = list.get(i);
            calcWordCount(data);
            calcUserMessageCount(data);
            calcTimeWithMostMessages(data);
        }
        getWordCount();
    }
    
    private void calcWordCount(MessageData md){
        
        String message  = md.getMessage();

        // split message by WhiteSpace
        String[] splittedMessage = message.split(" ");

        // count words
        wordCount += splittedMessage.length;

        for(String word : splittedMessage){
            if(wordCountMap.containsKey(word)){
                long count = wordCountMap.get(word);
                wordCountMap.put(word, count+1);
            }else{
                wordCountMap.put(word, 1L);
            }
        }
    }
    
    private void calcUserMessageCount(MessageData md){
        String username = md.getUser();
        if(messageCountUserMap.containsKey(username)){
            long count = messageCountUserMap.get(username);
            messageCountUserMap.put(username, count+1);
        }else{
            messageCountUserMap.put(username, 1L);
        }
    }
    
    private void  calcTimeWithMostMessages(MessageData data){
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(data.getTimeStamp());
        
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        
        if(messageInHourMap.containsKey(hour)){
            long count = messageInHourMap.get(hour);
            messageInHourMap.put(hour, count+1);
        }else{
            messageInHourMap.put(hour, 1L);
        }
    }
    
    
    
    
// 
//    private void getMessageInHourCount(){
//        
//        // inti Queue
//        messageInHourQueue = new PriorityQueue(messageInHourMap.keySet().size(), new ValueStringComparator(messageInHourMap));
//        messageInHourQueue.addAll(messageInHourMap.keySet());
//        
//        int hours = 20;
//        if(messageInHourQueue.size() < hours) hours = messageInHourQueue.size();
//        
//        System.out.println("=== First "+hours+" Hours with most common messages");
//        for(int i = 1; i <= hours; i++){
//            
//            String time   = messageInHourQueue.poll();
//            long   count  = messageInHourMap.get(time);
//            String line   = "#"+i+" : '"+time+"', count = '"+count+"'";
//            
//            System.out.println(line);
//        }
//        System.out.println("===###===");
//    }
    
    

    @Override
    public Thread getThread() {
        return thread;
    }

    @Override
    public void run() {
        calcEvaluation_();
    }

    @Override
    public void calcEvaluation() {
        this.thread.start();
    }

    @Override
    public boolean isRunning() {
        return thread.isAlive();
    }

    @Override
    public Map<String, Long> getCountWordMap() {
        return wordCountMap;
    }

    @Override
    public Map<String, Long> getCountUserMassageMap() {
        return messageCountUserMap;
    }

    @Override
    public Map<Integer, Long> getMessagePerHourMap() {
        return messageInHourMap;
    }

    @Override
    public long getWordCount() {
        return wordCount;
    }
}

class ValueStringComparator implements Comparator<String>{

    private final Map<String,Long> map;
    public ValueStringComparator(Map<String,Long> map){
        this.map = map;
    }
    
    @Override
    public int compare(String o1, String o2) {
        long o1Long = map.get(o1);
        long o2Long = map.get(o2);
        return Long.compare(o2Long, o1Long);
    }
}

class ValueIntegerComparator implements Comparator<Integer>{

    private final Map<Integer,Long> map;
    public ValueIntegerComparator(Map<Integer,Long> map){
        this.map = map;
    }
    
    @Override
    public int compare(Integer o1, Integer o2) {
        long o1Long = map.get(o1);
        long o2Long = map.get(o2);
        return Long.compare(o2Long, o1Long);
    }
}
