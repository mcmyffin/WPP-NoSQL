package de.client.UI;

import java.net.ConnectException;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dima
 */
public interface IPersistenceUI {

    /**
     * Try connect to Server
     * 
     * @return statuscode
     * 
     * statuscode:
     *  0 = OK
     *  1 = Already LoggedIn
     *  2 = Database not Found
     *  3 = Server Adress not Found
     */
    public int connect();
    
    public boolean disconnect();
    
    public void exit();

    public void openConnectionDialog();

    public void openLogDialog();

    public void showInfoFail(String titel, String message);

    public Number getDataCount();

    public String getTimeRange();

    public Number getWordCount();

    public Number getUserCount();

    public void showTimeDiagram();

    public void showListTopWords();

    public void showListTopUser();

    public void showInfoUser(String username);

    public void calc() throws Exception;
    
    public List<String> getUsernames();

    public void showAnalyseDialog();
    
    
}
