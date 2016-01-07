package deu.client.Chat;

import static com.google.common.base.Preconditions.checkNotNull;

import deu.client.UI.Dialog.LoadingDialog;
import de.server.persistence.ServerPersistence;
import de.server.persistence.result.HourActivity;
import de.server.persistence.result.HourActivityImpl;
import de.server.persistence.result.MessageData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import deu.client.UI.Dialog.LogDialog;
import java.util.HashMap;

/**
 *
 * @author dima
 */
public class TwitchChatManager implements Runnable, IChatManager{

    private final int maxListElemPerThread;
    private final Thread thread;
    private final Map<String,Long>  countWordMap;
    private final Map<String,Long>  countUserMessageMap;
    private final Map<Integer,Long> countMessagePerHourMap;
    private Map<String,HourActivity> userActivities;
    private long wordCount;
    
    private Queue<String> countWordQueue;
    private Queue<String> countUserMessageQueue;
    
    private final ServerPersistence serverPersistence;
    private final List<TwitchChat>  chats;
    private static List<MessageData> serverList = new ArrayList();
    
    public TwitchChatManager(ServerPersistence serverPersistence, int maxElemPerThread){
        checkNotNull(serverPersistence);
        this.serverPersistence = serverPersistence;
        this.thread = new Thread(this);
        
        maxListElemPerThread = maxElemPerThread;
        chats                = new ArrayList();
        countWordMap         = new HashMap();
        countUserMessageMap  = new HashMap();
        countMessagePerHourMap = new HashMap();
        userActivities       = new HashMap();
    }
    private void initMaps(){
        countMessagePerHourMap.clear();
        countUserMessageMap.clear();
        countWordMap.clear();
        
        for(int i = 0; i < 24; i++){
            countMessagePerHourMap.put(i, 0L);
        }
    }
    private void initThreads(){
        long dataCount = serverPersistence.getMessageCount();
        
        if(serverList.size() < dataCount){
            LogDialog.addLogLine(">> [GET SERVER DATA]");
            List<MessageData> list = serverPersistence.getAllMessages();
            LogDialog.addLogLine("<< "+list.size());
            LogDialog.addLogLine(">> CACHE SERVER DATA");
            serverList = list;
        }else{
            LogDialog.addLogLine(">> [GET CACHED SERVER DATA]");
            LogDialog.addLogLine("<< "+serverList.size());
        }
        
        int fromIndex = 0;
        int toIndex = maxListElemPerThread;
        wordCount = 0L;
        
        LogDialog.addLogLine(">> [INIT THREADS]");
        while(fromIndex < serverList.size()){
            
            if(toIndex > serverList.size()) toIndex = serverList.size();
            List<MessageData> sublist = serverList.subList(fromIndex, toIndex);
            TwitchChat chat = new TwitchChat(sublist,this);
//            TwitchChat chat = new TwitchChat(list,fromIndex,toIndex);
            chat.calcEvaluation();
            chats.add(chat);
            fromIndex = toIndex;
            toIndex  += maxListElemPerThread;
        }
        LogDialog.addLogLine("<< "+chats.size()+" NOW RUNNING");
    }

    synchronized void addWordCount(String key){
        if(countWordMap.containsKey(key)){
            long count = countWordMap.get(key);
            countWordMap.put(key, count+1);
        }else{
            countWordMap.put(key, 1L);
        }
    }
    synchronized void addUserMessageCount(String key){
        if(countUserMessageMap.containsKey(key)){
            long count = countUserMessageMap.get(key);
            countUserMessageMap.put(key, count+1);
        }else{
            countUserMessageMap.put(key, 1L);
        }
    }
    
    synchronized void addMessagePerHourCount(Integer key){
        if(countMessagePerHourMap.containsKey(key)){
            long count = countMessagePerHourMap.get(key);
            countMessagePerHourMap.put(key, count+1);
        }else{
            countMessagePerHourMap.put(key, 1L);
        }
    }
    
    synchronized void addUserActivity(String username, int hour){
        if(userActivities.containsKey(username)){
            HourActivity ha = userActivities.get(username);
            try {
                ha.addActivity(hour);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else{
            HourActivity ha = new HourActivityImpl();
            try {
                ha.addActivity(hour);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            userActivities.put(username, ha);
        }
    }
    
    private void getSortData(){
        LogDialog.addLogLine(">> START SORT DATA");
        // init Queue countWord
        countWordQueue = new PriorityQueue(countWordMap.keySet().size(), new ValueStringComparator(countWordMap));
        countWordQueue.addAll(countWordMap.keySet());

        // init Queue userMessageWord
        countUserMessageQueue = new PriorityQueue(countUserMessageMap.keySet().size(), new ValueStringComparator(countUserMessageMap));
        countUserMessageQueue.addAll(countUserMessageMap.keySet());
        LogDialog.addLogLine("<< SORT DATA DONE");
    }
   
    
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        initMaps();
        LoadingDialog.setStatus("GET SERVER DATA");
        initThreads();
        LoadingDialog.setStatus("ANALYSING DATA");
        while(!thread.isInterrupted() && !chats.isEmpty()){
            
            for(TwitchChat chat : chats){
                try {
                    chat.getThread().join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    thread.interrupt();
                }
                LogDialog.addLogLine("<< THREAD ["+chat.getThread().getName()+"] finished work");
                LogDialog.addLogLine(">> COLLECT DATA");
                
                wordCount+=chat.getWordCount();
                LogDialog.addLogLine("<< FINISHED COLLECT DATA");
                LoadingDialog.setStatus("DONE");
            }
            LogDialog.addLogLine("<< ALL THREADS DONE");
            getSortData();
            LogDialog.addLogLine("=== FINISHED IN "+(System.currentTimeMillis()-start)/1000.0+" sec ===");
            thread.interrupt();
        }
    }
    
    /*** IChatManager ***/
    
    @Override
    public void startCalc(){
        thread.start();
    }
    
    @Override
    public Thread getThread(){
        return this.thread;
    }
    
    @Override
    public Number getUserCount(){
        return countUserMessageMap.keySet().size();
    }
    
    @Override
    public Number getWordCount(){
        return wordCount;
    }
    
    @Override
    public String getTimeRange(){
        return  serverPersistence.getDataTimeRange();
    }
    
    @Override
    public Number getDataCount(){
        return serverPersistence.getMessageCount();
    }

    @Override
    public List<String> getUsernames() {
        List<String> list = new ArrayList(countUserMessageQueue.size());
        list.addAll(countUserMessageQueue);
        return list;
    }

    @Override
    public List<MessageData> getServerList() {
        return serverList;
    }

    @Override
    public Map<Integer, Long> getMessagePerHourMap() {
        return countMessagePerHourMap;
    }

    @Override
    public Queue<String> getCountWordsQueue() {
        Queue<String> q = new PriorityQueue<>(countWordQueue);
        return q;
    }

    @Override
    public Map<String, Long> getCoutWordsMap() {
        return countWordMap;
    }

    @Override
    public Queue<String> getCountUserMessageQueue() {
        return countUserMessageQueue;
    }

    @Override
    public Map<String, Long> getCountUserMessageMap() {
        return countUserMessageMap;
    }

    @Override
    public Map<String,HourActivity> getUserActivities() {
        return userActivities;
    }
    
    
}
