package de.client.Chat;

import static com.google.common.base.Preconditions.checkNotNull;
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
import org.jfree.chart.ChartFactory;
import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author dima
 */
public class TwitchChatManager implements Runnable{

    private final int maxListElemPerThread = 100000;
    private final Thread thread;
    private final Map<String,Long> countWordMap;
    private final Map<String,Long> countUserMessageMap;
    private final Map<Integer,Long> countMessagePerHourMap;
    private long wordCount = 0L;
    
    private Queue<String> countWordQueue;
    private Queue<String> countUserMessageQueue;
    
    private final ServerPersistence serverPersistence;
    private List<TwitchChat> chats;
    
    public TwitchChatManager(ServerPersistence serverPersistence){
        checkNotNull(serverPersistence);
        this.serverPersistence = serverPersistence;
        this.thread = new Thread(this);
        
        chats = new ArrayList();
        countWordMap = new LinkedHashMap();
        countUserMessageMap = new LinkedHashMap();
        countMessagePerHourMap = new LinkedHashMap();
    }
    
    public void startCalc(){
        thread.start();
    }
    
    private void init(){
        LogDialog.addLogLine(">> [GET SERVER DATA]");
        List<MessageData> list = serverPersistence.getAllMessages();
        LogDialog.addLogLine("<< "+list.size());
        
        int fromIndex = 0;
        int toIndex = maxListElemPerThread;
        
        LogDialog.addLogLine(">> [INIT THREADS]");
        while(fromIndex < list.size()){
            
            if(toIndex > list.size()) toIndex = list.size();
            List<MessageData> sublist = list.subList(fromIndex, toIndex);
            TwitchChat chat = new TwitchChat(sublist);
//            TwitchChat chat = new TwitchChat(list,fromIndex,toIndex);
            chat.calcEvaluation();
            chats.add(chat);
            fromIndex = toIndex;
            toIndex  += maxListElemPerThread;
        }
        LogDialog.addLogLine("<< "+chats.size()+" RUNNING");
    }

    public Thread getThread(){
        return this.thread;
    }
    
    private void getResults(){
        LogDialog.addLogLine(">> START SORT DATA");
        // init Queue countWord
        countWordQueue = new PriorityQueue(countWordMap.keySet().size(), new ValueStringComparator(countWordMap));
        countWordQueue.addAll(countWordMap.keySet());

        // init Queue userMessageWord
        countUserMessageQueue = new PriorityQueue(countUserMessageMap.keySet().size(), new ValueStringComparator(countUserMessageMap));
        countUserMessageQueue.addAll(countUserMessageMap.keySet());
        LogDialog.addLogLine("<< SORT DATA DONE");
        
//        showCountWord(countWordQueue);
//        showCountUserMessage(countUserMessageQueue);
//        showCountMessagePerHour();
    }
    
    private void showCountWord(Queue<String> queue){
        int words = 20;
        if(queue.size() < words) words = queue.size();
        
        System.out.println("=== Most most common "+words+" Words of "+wordCount+"===");
        for(int i = 1; i <= words; i++){
            
            String word = queue.poll();
            long   count= countWordMap.get(word);
            String line = "#"+i+" : word = '"+word+"', count = '"+count+"'";
            
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
        init();
        
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
                countWordMap.putAll(chat.getCountWordMap());
                countUserMessageMap.putAll(chat.getCountUserMassageMap());
                countMessagePerHourMap.putAll(chat.getMessagePerHourMap());
                wordCount+=chat.getWordCount();
                LogDialog.addLogLine("<< FINISHED COLLECT DATA");
            }
            LogDialog.addLogLine("<< ALL THREADS DONE");
            LogDialog.addLogLine(">> FINISHED IN "+(System.currentTimeMillis()-start)/1000.0+" sec");
            getResults();
            
            thread.interrupt();
        }
    }
    
    public Number getUserCount(){
        return countUserMessageMap.keySet().size();
    }
    
    public Number getWordCount(){
        return wordCount;
    }
    
    public String getTimeRange(){
        return  "?? - ??";
    }
    
    public Number getDataCount(){
        return serverPersistence.getMessageCount();
    }

    public Set<String> getUsernames() {
        return countUserMessageMap.keySet();
    }
}
