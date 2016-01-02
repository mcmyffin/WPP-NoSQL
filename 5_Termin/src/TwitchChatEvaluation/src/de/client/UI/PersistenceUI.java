package de.client.UI;

import de.client.Chat.TwitchChatManager;
import de.client.UI.Dialog.ConnectionDialog;
import de.client.UI.Dialog.LogDialog;
import de.client.UI.Frame.MainFrame;
import de.server.persistence.ServerPersistence;
import de.server.persistence.ServerPersistenceImpl;
import de.server.persistence.client.ClientFactory;
import de.server.persistence.client.Exeptions.DataBaseNotFoundException;
import de.server.persistence.client.MongoClient;
import de.server.persistence.client.MongoConfiguration;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/**
 *
 * @author dima
 */
public class PersistenceUI implements IPersistenceUI{

    // UI
    private final MainFrame mainFrame;
    private final LogDialog logDialog;
    private final ConnectionDialog conDialog;
    
    // SERVER
    private ServerPersistence serverPersistence;
    private TwitchChatManager manager;
    
    public PersistenceUI(){
        this.mainFrame = MainFrame.startThread(this);
        this.logDialog = LogDialog.getInstance();
        this.conDialog = new ConnectionDialog(mainFrame, true);
        
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
            manager = new TwitchChatManager(serverPersistence);
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
        LogDialog.getInstance().setVisible(true);
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showListTopWords() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public Set<String> getUsernames() {
        return manager.getUsernames();
    }
    
    @Override
    public void calc() {
        manager.startCalc();
        final Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    manager.getThread().join();
                    mainFrame.initMainPanel();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    
                }
            }
        });
        t.start();
    }
    
    
}
