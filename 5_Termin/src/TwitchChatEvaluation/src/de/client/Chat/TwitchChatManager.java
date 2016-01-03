package de.client.Chat;

import static com.google.common.base.Preconditions.checkNotNull;
import de.client.UI.Dialog.LoadingDialog;
import de.client.UI.Dialog.LogDialog;
import de.server.persistence.ServerPersistence;
import de.server.persistence.result.MessageData;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Hour;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

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
    private long wordCount;
    
    private Queue<String> countWordQueue;
    private Queue<String> countUserMessageQueue;
    
    private final ServerPersistence serverPersistence;
    private final List<TwitchChat>  chats;
    private List<MessageData>       serverList;
    
    public TwitchChatManager(ServerPersistence serverPersistence, int maxElemPerThread){
        checkNotNull(serverPersistence);
        this.serverPersistence = serverPersistence;
        this.thread = new Thread(this);
        
        maxListElemPerThread = maxElemPerThread;
        chats                = new ArrayList();
        countWordMap         = new LinkedHashMap();
        countUserMessageMap  = new LinkedHashMap();
        countMessagePerHourMap = new LinkedHashMap();
    }
    private void initMaps(){
        countMessagePerHourMap.clear();
        countUserMessageMap.clear();
        countWordMap.clear();
        
        for(int i = 0; i < 24; i++){
            countMessagePerHourMap.put(i, 0L);
        }
    }
    private void initClass(){
        
        if(serverList == null){
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
    
    private void showCountWord(Queue<String> queue){
        int words = queue.size();
        if(queue.size() < words) words = queue.size();
        
        System.out.println("=== Most most common "+words+" Words of "+wordCount+"===");
        for(String word : queue){
            
            long   count= countWordMap.get(word);
            String line = "#"+countWordMap.get(word)+" : word = '"+word+"'";
            
            System.out.println(line);
        }
        System.out.println("===###===");
    }
    
    private void showCountUserMessage(Queue<String> queue){
        
        int users = 10;
        if(queue.size() < users) users = queue.size();
        
        System.out.println("=== Users who have sent the most common messages ("+queue.size()+" = Users)");
        for(int i = 1; i <= users; i++){
        
            String user = queue.poll();
            long   count= countUserMessageMap.get(user);
            String line = "#"+i+" : username = '"+user+"', count = '"+count+"'";
            
            System.out.println(line);
        }
        System.out.println("===###===");
    }

    
    private void test(){
        
        String titel = "Common Message Per Hour";
        String xAxisTitel = "Hour";
        String yAxisTitel = "Message Count";
        
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart(titel, xAxisTitel, yAxisTitel, createDataSet());
        ChartFrame frame = new ChartFrame("Chart", chart);
        frame.setBounds(new Rectangle(1200, 800));
        frame.setVisible(true);
    }
    
    private void showCountMessagePerHour(Queue<String> queue){
        int hours = 20;
        if(queue.size() < hours) hours = queue.size();
        
        System.out.println("=== First "+hours+" Hours with most common messages");
        for(int i = 1; i <= hours; i++){
            
            String time   = queue.poll();
            long   count  = countMessagePerHourMap.get(time);
            String line   = "#"+i+" : '"+time+"', count = '"+count+"'";
            
            System.out.println(line);
        }
        System.out.println("===###===");
    }

    private XYDataset createDataSet(){
        TimeSeries series = new TimeSeries("MessageCountPerHour");
        
        int year  = Calendar.getInstance().get(Calendar.YEAR);;
        int month = Calendar.getInstance().get(Calendar.MONTH)+1;
        int day   = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        
        for(int hour : countMessagePerHourMap.keySet()){
            RegularTimePeriod tp = new Hour(hour,day,month,year);
            series.add(tp, countMessagePerHourMap.get(hour));
        }
                
        return new TimeSeriesCollection(series);
    }
    
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        initMaps();
        initClass();
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
    
    
    
    
}
