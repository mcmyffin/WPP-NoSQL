package deu.client.Chat;

import de.server.persistence.result.MessageData;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dima
 */
public class TwitchChat implements IEval, Runnable{

    private long  wordCount = 0L;
    
    private final List<MessageData> list;
    private final int fromIndex;
    private final int toIndex;
    
    private final Thread thread;
    private final TwitchChatManager manager;
    
    public TwitchChat(List<MessageData> list, int fromIndex, int toIndex, TwitchChatManager m){
        
        this.manager = m;
        this.list = list;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.thread = new Thread(this);
    }
    
    
    public TwitchChat(List<MessageData> list, TwitchChatManager m){
        
        this.manager = m;
        this.fromIndex = 0;
        this.toIndex = list.size();
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
        String[] splittedMessage = message.split("\\s+");

        // count words
        wordCount += splittedMessage.length;

        for(String word : splittedMessage){
            manager.addWordCount(word);
        }
    }
    
    private void calcUserMessageCount(MessageData md){
        String username = md.getUser();
        manager.addUserMessageCount(username);
    }
    
    private void  calcTimeWithMostMessages(MessageData data){
        String username = data.getUser();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(data.getTimeStamp());
        
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        manager.addUserActivity(username, hour);
        manager.addMessagePerHourCount(hour);
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
    public long getWordCount() {
        return wordCount;
    }
    
    public static ValueStringComparator getValueStringComperator(Map<String,Long> map){
        return new ValueStringComparator(map);
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
