package deu.client.UI;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import static com.google.common.base.Preconditions.checkNotNull;

import deu.client.Chat.Analysis.*;
import deu.client.Chat.IChatManager;
import deu.client.UI.Dialog.ConnectionDialog;
import deu.client.UI.Frame.MainFrame;
import deu.client.Chat.Analysis.Typ;
import deu.client.Chat.Analysis.UserAnalysis;
import deu.client.Chat.TwitchChatManager;
import deu.client.UI.Dialog.AnalysisDialog;
import deu.client.UI.Dialog.LoadingDialog;
import deu.client.UI.Dialog.LogDialog;
import deu.client.UI.Dialog.TableDialog;
import deu.client.UI.Dialog.UserAnalysisDialog;
import de.server.persistence.Exceptions.UserNotFoundException;
import de.server.persistence.ServerPersistence;
import de.server.persistence.ServerPersistenceImpl;
import de.server.persistence.client.ClientFactory;
import de.server.persistence.client.Exeptions.DataBaseNotFoundException;
import de.server.persistence.client.MongoClient;
import de.server.persistence.client.MongoConfiguration;
import de.server.persistence.result.HourActivity;
import de.server.persistence.result.MessageData;
import de.server.persistence.result.UserData;
import deu.client.Chat.TwitchChat;
import java.awt.Container;
import java.awt.Rectangle;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Hour;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;


/**
 *
 * @author dima
 */
public class PersistenceUI implements IPersistenceUI{

    // UI
    private final MainFrame mainFrame;
    private final ConnectionDialog conDialog;
    private final AnalysisDialog anDialog;
    private final LogDialog     logDialog;
    private final TableDialog   tableDialog;
    
    // SERVER
    private ServerPersistence serverPersistence;
    private IChatManager manager;
    
    // LANGUAGE
    private boolean isLanguageInitialized = false;
    
    public PersistenceUI(){
        this.mainFrame = MainFrame.startThread(this);
        this.logDialog     = LogDialog.getInstance(mainFrame);
        LogDialog.addLogLine("== INIT UI PERSITENCE ==");
        LogDialog.addLogLine(">> MAIN FRAME ... OK");
        this.conDialog = new ConnectionDialog(mainFrame, true);
        this.anDialog  = new AnalysisDialog(mainFrame, true);
        this.tableDialog   = new TableDialog(mainFrame, true);
        LogDialog.addLogLine(">> DIALOGUES ... OK ");   
    }
    
    private void initLangDetection(){
        if(isLanguageInitialized) return;
        try {
            LogDialog.addLogLine(">> INIT LANGUAGE DETECTION");
            DetectorFactory.loadProfile(anDialog.getLangPacketPath());
            LogDialog.addLogLine(">> DONE");
            isLanguageInitialized = true;
        } catch (LangDetectException ex) {
            LogDialog.addLogLine(">> FAIL");
            LogDialog.addLogLine(">> START STACKTRACE");
            LogDialog.addLogLine(ex.getMessage());
            for(StackTraceElement s : ex.getStackTrace()){
                LogDialog.addLogLine(s.toString());
            }
            
            LogDialog.addLogLine(">> END STACKTRACE");
        }
    }
    
    @Override
    public int connect() {
        LogDialog.addLogLine("== CONNECTION ==");
        LogDialog.addLogLine(">> TRY BUILD CONNECTION");
        if(serverPersistence != null){
            LogDialog.addLogLine(">> FAIL, ... CONNECTION ALREADY EXISTS");
            return 1;
        }
        initLangDetection();
        String serverAdress = conDialog.getServerAdress();
        String db = conDialog.getDatabase();
        LogDialog.addLogLine(">> SERVER ADRESS:\t"+serverAdress);
        LogDialog.addLogLine(">> DB:\t\t"+db);
        LogDialog.addLogLine(">> BUILD CONFIGURATION");
        try {
            MongoConfiguration conf = ClientFactory.createMongoDBConfigruation(serverAdress, db, "chatCollection");
            LogDialog.addLogLine(">> CREATE CLIENT ...");
            MongoClient mClient = ClientFactory.createMongoClient(conf);
            LogDialog.addLogLine(">> DONE");
            LogDialog.addLogLine(">> CREATE SERVER PERSISTENCE ...");
            serverPersistence = new ServerPersistenceImpl(mClient);
            LogDialog.addLogLine(">> DONE");
            return 0;
            
        } catch (UnknownHostException ex) {
            LogDialog.addLogLine(">> FAIL");
            LogDialog.addLogLine(">> START STACKTRACE");
            for(StackTraceElement s : ex.getStackTrace()) LogDialog.addLogLine(s.toString());
            LogDialog.addLogLine(">> END STACKTRACE");
            return 3;
        } catch (DataBaseNotFoundException ex) {
            LogDialog.addLogLine(">> FAIL");
            LogDialog.addLogLine(">> START STACKTRACE");
            for(StackTraceElement s : ex.getStackTrace()) LogDialog.addLogLine(s.toString());
            LogDialog.addLogLine(">> END STACKTRACE");
            return 2;
        }
    }

    @Override
    public boolean disconnect() {
        LogDialog.addLogLine("== DISCONNECT ==");
        if(serverPersistence == null){
            LogDialog.addLogLine(">> FAIL ... CLIENT ALREADY DISCONNECTED");
            return false;
        }
        LogDialog.addLogLine(">> DONE");
        serverPersistence = null;
        return true;
    }

    @Override
    public void exit() {
        // TODO
        System.exit(0);
    }

    @Override
    public void openConnectionDialog() {
        conDialog.setVisible(true);
    }

    @Override
    public void openLogDialog() {
        logDialog.showLog();
    }

    @Override
    public void showInfoFail(String titel, String message) {
        JOptionPane.showConfirmDialog(mainFrame, message, titel, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public Number getDataCount() {
        return manager.getDataCount();
    }

    @Override
    public String getTimeRange() {
        return manager.getTimeRange();
    }

    @Override
    public Number getWordCount() {
        return manager.getWordCount();
    }

    @Override
    public Number getUserCount() {
        return manager.getUserCount();
    }

    @Override
    public void showTimeDiagram() {
        int x = anDialog.getXVal();
        int y = anDialog.getYVal();
        int gmt = anDialog.getGMT();
        JFrame frame = getMessagePerHourChart(manager.getMessagePerHourMap(),x, y, gmt);
        Container p = frame.getContentPane();
        JDialog d = new JDialog(mainFrame,true);
        d.setContentPane(p);
        d.setBounds(new Rectangle(x, y));
        d.setLocationRelativeTo(mainFrame);
        d.setTitle(frame.getTitle());
        d.pack();
        d.setVisible(true);
    }

    @Override
    public void showListTopWords() {
        Queue<String>    cwq = manager.getCountWordsQueue();
        Map<String,Long> cwm = manager.getCoutWordsMap();
        
        tableDialog.showTable(cwq, cwm, "Wort", "Anzahl");
    }

    @Override
    public void showListTopUser() {
        Queue<String> countUserMessageQueue = manager.getCountUserMessageQueue();
        Map<String,Long> countUserMessageMap = manager.getCountUserMessageMap();
        
        tableDialog.showTable(countUserMessageQueue, countUserMessageMap, "Benutzer", "Anzahl Nachrichten");
    }

    @Override
    public void showInfoUser(final String username) {
        checkNotNull(username);
        final IPersistenceUI instance = this;
        final LoadingDialog loadingDialog = LoadingDialog.getInstance(mainFrame);
        LogDialog.addLogLine("== SHOW INFO USER ==");
        LogDialog.addLogLine(">> USERNAME:\t"+username);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadingDialog.setNewStatus("GET SERVER DATA");
                    LogDialog.addLogLine(">> GET SERVER DATA");
                    
                    Map<Integer,Long> act = manager.getUserActivities().get(username).getMessagePerHourMap();
                    UserData userData = serverPersistence.getUserData(username);
                    loadingDialog.setNewStatus("DETECT LANGUAGE");
                    LogDialog.addLogLine(">> DETECT LANGUAGE");
                    
                    String lang = "unknown";
                    try{
                        Detector detector = DetectorFactory.create();
                        for(String word : userData.getCountWordMap().keySet()) detector.append(word);
                        lang = detector.detect();
                    }catch(LangDetectException ex){
                        LogDialog.addLogLine(">> FAIL");
                        LogDialog.addLogLine(">> START STACKTRACE");
                        LogDialog.addLogLine(ex.getMessage());
                        for(StackTraceElement s : ex.getStackTrace()) LogDialog.addLogLine(s.toString());
                        LogDialog.addLogLine(">> END STACKTRACE");
                    }
                    loadingDialog.setNewStatus("DONE");
                    LogDialog.addLogLine(">> DONE");
                    long wordcount = userData.getCountWordMap().keySet().size();

                    Typ typ = UserAnalysis.getUserTyp(act);
                    UserAnalysisDialog uad = new UserAnalysisDialog(mainFrame, username, wordcount, lang, typ, instance, userData);
                    loadingDialog.closeLoadingView();
                    uad.showDialog();
                } catch (UserNotFoundException ex) {
                    LogDialog.addLogLine(">> FAIL");
                    LogDialog.addLogLine(">> START STACKTRACE");
                    for(StackTraceElement s : ex.getStackTrace()) LogDialog.addLogLine(s.toString());
                    LogDialog.addLogLine(">> END STACKTRACE");
                    showInfoFail("FAIL", "Benutzername '"+username+"' nicht gefunden!");
                }finally{
                    loadingDialog.closeLoadingView();
                }
            }
        });
        
        t.start();
    }

    @Override
    public List<String> getUsernames() {
        return manager.getUsernames();
    }
    
    @Override
    public void calc() throws Exception{
        if(serverPersistence == null) throw new Exception();
        final LoadingDialog loadingDialog = LoadingDialog.getInstance(mainFrame);
        LogDialog.addLogLine("== CALC ==");
        final Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    manager = new TwitchChatManager(serverPersistence,conDialog.getMaxElemPerThread());
                    manager.startCalc();
                    manager.getThread().join();
                    loadingDialog.closeLoadingView();
                    mainFrame.initMainPanel();
                } catch (InterruptedException ex) {
                    LogDialog.addLogLine(">> FAIL");
                    LogDialog.addLogLine(">> START STACKTRACE");
                    for(StackTraceElement s : ex.getStackTrace()) LogDialog.addLogLine(s.toString());
                    LogDialog.addLogLine(">> END STACKTRACE");
                    
                }
            }
        });
        t.start();
    }

    @Override
    public void showAnalyseDialog() {
        anDialog.setVisible(true);
    }

    @Override
    public void showUserMessagePerHourDiagramm(String username, JDialog focus) {
        int x = anDialog.getXVal();
        int y = anDialog.getYVal();
        int gmt = anDialog.getGMT();
        Map<Integer,Long> map = manager.getUserActivities().get(username).getMessagePerHourMap();
        JFrame frame = getMessagePerHourChart(map, x, y, gmt);
        Container p = frame.getContentPane();
        JDialog d = new JDialog(focus, true);
        d.setContentPane(p);
        d.setBounds(new Rectangle(x, y));
        d.setLocationRelativeTo(focus);
        d.setTitle(frame.getTitle());
        d.setVisible(true);
    }

    @Override
    public void showPieChart() {
        final long startTime = System.currentTimeMillis();
        
        final int x = anDialog.getXVal();
        final int y = anDialog.getYVal();
        
        final LoadingDialog loadingDialog = LoadingDialog.getInstance(mainFrame);
        final List<String> usernames = manager.getUsernames();
        final List<TypeCount> listTypeCount = new ArrayList();
        final int listSize = usernames.size();
        
        
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                LogDialog.addLogLine("== show PieChart ==");
                
                Map<Typ,Long> countUserTypes = new HashMap();
                Map<String,HourActivity> userActivities = manager.getUserActivities();
                for(String username : usernames){
                    HourActivity hourActivity = userActivities.get(username);
                    Typ t = UserAnalysis.getUserTyp(hourActivity.getMessagePerHourMap());
                    if(countUserTypes.containsKey(t)){
                        countUserTypes.put(t, countUserTypes.get(t)+1);
                    }else countUserTypes.put(t, 1L);
                }
                
                LoadingDialog.setStatus("DONE");
                LoadingDialog.setStatus("DRAWING ...");
                
                JFrame frame = getTypePieChart(countUserTypes, x, y);
                Container p = frame.getContentPane();
                JDialog d = new JDialog(mainFrame, true);
                d.setContentPane(p);
                d.setBounds(new Rectangle(x, y));
                d.setLocationRelativeTo(mainFrame);
                d.setTitle(frame.getTitle());
                LoadingDialog.setStatus("DONE");
                loadingDialog.closeLoadingView();
                LogDialog.addLogLine("=== FINISHED IN "+(System.currentTimeMillis()-startTime)/1000.0+" sec ===");
                d.setVisible(true);
            }
        });
        t.start();
    }
    
    /****************/
    private JFrame getMessagePerHourChart(Map<Integer,Long> map, int width, int height, int ofset){
        String titel = "Anzahl der Nachrichten die Stunde";
        String xAxisTitel = "Stunde";
        String yAxisTitel = "Nachrichten";
        
        // create DATASET
        TimeSeries series = new TimeSeries("MessageCountPerHour");
        
        int year  = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH)+1;
        int day   = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        
        for(int hour : map.keySet()){
            
            int h =  ((hour-1)+ofset)%24;
            RegularTimePeriod tp = new Hour(h,day,month,year);
            series.add(tp, map.get(hour));
        }
                
        XYDataset set = new TimeSeriesCollection(series);
        
        /*****/
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart(titel, xAxisTitel, yAxisTitel, set);
        ChartFrame frame = new ChartFrame("Chart", chart);
        frame.setBounds(new Rectangle(width, height));
        frame.setTitle("GMT "+(ofset > 0 ? "+"+ofset : ofset));
        return frame;
    }
    
    private JFrame getTypePieChart(Map<Typ,Long> map, int width, int height){
        
        String titel = "Anzahl User pro Typ";
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        
        for(Typ t : map.keySet()){
            long val = map.get(t);
            pieDataset.setValue(t.toString(), val);
        }
        
        JFreeChart chart = ChartFactory.createPieChart(titel, pieDataset);
        ChartFrame frame = new ChartFrame(titel, chart);
        frame.setBounds(new Rectangle(width, height));
        return frame;
    }
}

class TypeCount implements Runnable{
    
    private Map<String,HourActivity> userActivities;
    private List<String> usernames;
    private Map<Typ,Long> countUserTypes;
    private Thread t;
    
    public TypeCount(List<String> usernames, Map<String,HourActivity> map) {
        this.usernames = usernames;
        this.userActivities = map;
        this.countUserTypes = new HashMap();
        t = new Thread(this);
    }
    
    
    @Override
    public void run() {
        for(String username : usernames){
            HourActivity hourActivity = userActivities.get(username);
            Typ t = UserAnalysis.getUserTyp(hourActivity.getMessagePerHourMap());
            if(countUserTypes.containsKey(t)){
                countUserTypes.put(t, countUserTypes.get(t)+1);
            }else countUserTypes.put(t, 1L);
        }
    }
    
    public Map<Typ,Long> getCountUserTypes(){
        return this.countUserTypes;
    }
    
    public void calc(){
        t.start();
    }
    
    public Thread getThread(){
        return this.t;
    }
}
