package de.client.UI;

import de.client.Chat.IChatManager;
import de.client.Chat.TwitchChatManager;
import de.client.UI.Dialog.AnalyseDialog;
import de.client.UI.Dialog.ConnectionDialog;
import de.client.UI.Dialog.LoadingDialog;
import de.client.UI.Dialog.LogDialog;
import de.client.UI.Dialog.TableDialog;
import de.client.UI.Frame.MainFrame;
import de.server.persistence.ServerPersistence;
import de.server.persistence.ServerPersistenceImpl;
import de.server.persistence.client.ClientFactory;
import de.server.persistence.client.Exeptions.DataBaseNotFoundException;
import de.server.persistence.client.MongoClient;
import de.server.persistence.client.MongoConfiguration;
import de.server.persistence.result.MessageData;
import java.awt.Rectangle;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
public class PersistenceUI implements IPersistenceUI{

    // UI
    private final MainFrame mainFrame;
    private final ConnectionDialog conDialog;
    private final AnalyseDialog anDialog;
    private final LoadingDialog loadingDialog;
    private final LogDialog     logDialog;
    private final TableDialog   talbeDialog;
    
    // SERVER
    private ServerPersistence serverPersistence;
    private IChatManager manager;
    
    public PersistenceUI(){
        this.mainFrame = MainFrame.startThread(this);
        this.conDialog = new ConnectionDialog(mainFrame, true);
        this.anDialog  = new AnalyseDialog(mainFrame, true);
        this.loadingDialog = LoadingDialog.getInstance(mainFrame);
        this.talbeDialog   = new TableDialog(mainFrame, true);
        this.logDialog     = LogDialog.getInstance(mainFrame);
        
    }
    
    @Override
    public int connect() {
        if(serverPersistence != null) return 1;
        String serverAdress = conDialog.getServerAdress();
        String db = conDialog.getDatabase();
        
        try {
            MongoConfiguration conf = ClientFactory.createMongoDBConfigruation(serverAdress, db, "chatCollection");
            MongoClient mClient = ClientFactory.createMongoClient(conf);
            serverPersistence = new ServerPersistenceImpl(mClient);
            
            return 0;
            
        } catch (UnknownHostException ex) {
            return 3;
        } catch (DataBaseNotFoundException ex) {
            return 2;
        }
    }

    @Override
    public boolean disconnect() {
        if(serverPersistence == null) return false;
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
        JFrame frame = getMessagePerHourDiagramm(x, y, gmt);
        frame.setVisible(true);
    }

    @Override
    public void showListTopWords() {
        Queue<String>    cwq = manager.getCountWordsQueue();
        Map<String,Long> cwm = manager.getCoutWordsMap();
        
        talbeDialog.showTable(cwq, cwm, "Wort", "Anzahl");
    }

    @Override
    public void showListTopUser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showInfoUser(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getUsernames() {
        return manager.getUsernames();
    }
    
    @Override
    public void calc() throws Exception{
        if(serverPersistence == null) throw new Exception();
        manager = new TwitchChatManager(serverPersistence,conDialog.getMaxElemPerThread());
        manager.startCalc();
        final Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    manager.getThread().join();
                    loadingDialog.closeLoadingView();
                    mainFrame.initMainPanel();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    
                }
            }
        });
        t.start();
        loadingDialog.showLoadingView("GET SERVER DATA");
    }

    @Override
    public void showAnalyseDialog() {
        anDialog.setVisible(true);
    }
    
    /****************/
    private JFrame getMessagePerHourDiagramm(int with, int height, int ofset){
        String titel = "Anzahl der Nachrichten die Stunde";
        String xAxisTitel = "Stunde";
        String yAxisTitel = "Nachrichten";
        
        // create DATASET
        Map<Integer,Long> countMessagePerHourMap = manager.getMessagePerHourMap();
        TimeSeries series = new TimeSeries("MessageCountPerHour");
        
        int year  = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH)+1;
        int day   = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        
        for(int hour : countMessagePerHourMap.keySet()){
            
            int h =  ((hour-1)+ofset)%24;
            RegularTimePeriod tp = new Hour(h,day,month,year);
            series.add(tp, countMessagePerHourMap.get(hour));
        }
                
        XYDataset set = new TimeSeriesCollection(series);
        
        /*****/
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart(titel, xAxisTitel, yAxisTitel, set);
        ChartFrame frame = new ChartFrame("Chart", chart);
        frame.setBounds(new Rectangle(with, height));
        frame.setLocationRelativeTo(mainFrame);
        frame.setTitle("GMT "+(ofset > 0 ? "+"+ofset : ofset));
        return frame;
    }
    
}
