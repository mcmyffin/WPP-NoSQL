package de.server.persistence;

import static com.google.common.base.Preconditions.checkNotNull;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import de.server.persistence.Exceptions.UserNotFoundException;
import de.server.persistence.result.*;
import de.server.persistence.client.*;
import de.server.persistence.client.Exeptions.DataBaseNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;

/**
 *
 * @author nosql
 */
public class ServerPersistenceImpl implements ServerPersistence{
    
    private final MongoClient m_client;
    
    public ServerPersistenceImpl(MongoClient mongoClient){
        checkNotNull(mongoClient);
        
        this.m_client = mongoClient;
    }
    
    
    private DBObject createDBObject(String json){
        return (DBObject) JSON.parse(json);
    }

    @Override
    public ServerPersistence clone() {
        try{
            MongoConfiguration conf = m_client.getConfiguration();
            MongoClient c = ClientFactory.createMongoClient(conf);
            return new ServerPersistenceImpl(c);
            
        }catch(DataBaseNotFoundException|UnknownHostException ex){
            ex.printStackTrace();
        }
        return null;
    }
    
    @Override
    public synchronized void messageSend(long t, String usr, String msg){
        checkNotNull(msg);
        checkNotNull(usr);
        
        
        DBObject insertObj   = new BasicDBObject("_id", t);
        insertObj.put("username", usr);
        insertObj.put("message", msg);
        m_client.getChatCollection().insert(insertObj);
    }

    @Override
    public synchronized long getMessageCount() {
        return m_client.getChatCollection().count();
    }
 
    @Override
    public synchronized List<MessageData> getAllMessages(){
        
        List<MessageData> messageDataList = new ArrayList(100);
        DBCursor dBCursor = m_client.getChatCollection().find();
        
        while(dBCursor.hasNext()){
            DBObject o = dBCursor.next();
            String user = o.get("username").toString();
            long timeStamp = Long.parseLong(o.get("_id").toString());
            String message = o.get("message").toString();
            
            MessageData m = new MessageDataImpl(user, timeStamp, message);
            messageDataList.add(m);
        }
        return messageDataList;
    }

    @Override
    public synchronized List<MessageData> getMessages(int from, int to) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dropChatCollection() {
        m_client.getChatCollection().drop();
    }

    @Override
    public synchronized String getDataTimeRange() {
        
        DBObject search = new BasicDBObject();
        DBObject prenstation = new BasicDBObject("_id", true);
        
        DBObject orderByFirst = new BasicDBObject("_id", 1);
        DBObject orderByLast  = new BasicDBObject("_id", -1);
        
        DBCursor cursor = m_client.getChatCollection().find(search,prenstation);
        
        // sort
        DBCursor firstCursor = cursor.sort(orderByFirst);
        
        String from = "";
        String to   = "";
        if(firstCursor.hasNext()){
            from = firstCursor.next().get("_id").toString();
        }
        cursor = m_client.getChatCollection().find(search,prenstation);
        DBCursor lastCursor  = cursor.sort(orderByLast);
        if(lastCursor.hasNext()){
            to = lastCursor.next().get("_id").toString();
        }
        
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Long.parseLong(from));
        Date fromDate = c.getTime();
        
        c.setTimeInMillis(Long.parseLong(to));
        Date toDate = c.getTime();
        String result = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(fromDate);
        result += "  -  ";
        result += new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(toDate);
        return result;
    }

    private boolean userExists(String username){
        DBObject search = new BasicDBObject("username", username);
        DBObject result = m_client.getChatCollection().findOne(search);
        return result != null;
    }
    
    @Override
    public synchronized UserData getUserData(String username) throws UserNotFoundException{
        checkNotNull(username);
        if(!userExists(username)) throw new UserNotFoundException();
        
        // INIT search Argument
        DBObject searchArgument = new BasicDBObject("username", username);
        // GET result Cursor
        DBCursor cursor = m_client.getChatCollection().find(searchArgument);
        
        // INIT Information Container
        List<MessageData> messageData = new ArrayList();
        HourActivity hourActivity = new HourActivityImpl();
        Map<String,Long> countWordMap = new HashMap();
        
        while(cursor.hasNext()){
        
            DBObject line = cursor.next();
            
            // get Arguments
            long time = Long.parseLong(line.get("_id").toString());
            String name = line.get("username").toString();
            String message = line.get("message").toString();
            
            // add Words
            for(String word : message.split(" ")){
                if(countWordMap.containsKey(word)){
                    countWordMap.put(word,countWordMap.get(word)+1);
                }else{
                    countWordMap.put(word, 1L);
                }
            } 
            
            // create MessageObj
            MessageData md = new MessageDataImpl(name, time, message);
            messageData.add(md);
        }
        return new UserDataImpl(username, countWordMap, messageData);
    }

    @Override
    public synchronized HourActivity getHourActivityFromUser(String username) throws UserNotFoundException {
        checkNotNull(username);
        if(!userExists(username)) throw new UserNotFoundException();
        
        // INIT search Argument
        DBObject searchArgument = new BasicDBObject("username", username);
        DBObject presentation   = new BasicDBObject("_id", true);
        // GET result Cursor
        DBCursor cursor = m_client.getChatCollection().find(searchArgument,presentation);
        HourActivity hourActivity = new HourActivityImpl();
        
        while(cursor.hasNext()){
            DBObject line = cursor.next();
            
            // get Arguments
            long time = Long.parseLong(line.get("_id").toString());
            
            // create hour
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            
            int hour = c.get(Calendar.HOUR_OF_DAY);
            try {
                // try to add hour
                hourActivity.addActivity(hour);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return hourActivity;
    }
    
    
    
}
