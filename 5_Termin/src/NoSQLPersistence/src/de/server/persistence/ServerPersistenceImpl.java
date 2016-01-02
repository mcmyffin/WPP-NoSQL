package de.server.persistence;

import static com.google.common.base.Preconditions.checkNotNull;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.List;
import de.server.persistence.result.*;
import de.server.persistence.client.*;


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
    public synchronized void messageSend(long t, String usr, String msg){
        checkNotNull(msg);
        checkNotNull(usr);
        
        
        DBObject insertObj   = new BasicDBObject("_id", t);
        insertObj.put("username", usr);
        insertObj.put("message", msg);
        m_client.getChatCollection().insert(insertObj);
    }

    @Override
    public long getMessageCount() {
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
    public List<MessageData> getMessages(int from, int to) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void dropChatCollection() {
        m_client.getChatCollection().drop();
    }
}
