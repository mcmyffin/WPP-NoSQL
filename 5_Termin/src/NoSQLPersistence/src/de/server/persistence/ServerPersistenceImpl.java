package de.server.persistence;

import static com.google.common.base.Preconditions.checkNotNull;
import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import de.server.persistence.result.*;
import de.server.persistence.Exceptions.*;
import de.server.persistence.client.Exeptions.*;
import de.server.persistence.client.*;


/**
 *
 * @author nosql
 */
public class ServerPersistenceImpl implements ServerPersistence{

    private final RedisClient r_client;
    private final MongoClient m_client;
    
    public ServerPersistenceImpl(RedisClient redisClient, MongoClient mongoClient){
        checkNotNull(redisClient);
        checkNotNull(mongoClient);
        
        this.r_client = redisClient;
        this.m_client = mongoClient;
    }
    
    
    private DBObject createDBObject(String json){
        return (DBObject) JSON.parse(json);
    }
    
    private synchronized boolean userExist(String username){
        String searchArgument       = "{'username' : '"+username+"'}";
        String searchRepresentation = "{'username' : '1'}";
        DBObject searchObj          = createDBObject(searchArgument);
        DBObject representationObj  = createDBObject(searchRepresentation);

        DBObject replyObj = m_client.getUserCollection().findOne(searchObj,representationObj);
        return replyObj != null;
    }
    
    @Override
    public synchronized boolean loginUser(String username, String password) {
        checkNotNull(username);
        checkNotNull(password);
        
        String searchArgument       = "{'username' : '"+username+"'}";
        String searchRepresentation = "{'password' : '1'}";
        DBObject searchObj          = createDBObject(searchArgument);
        DBObject representationObj  = createDBObject(searchRepresentation);

        DBObject replyObj = m_client.getUserCollection().findOne(searchObj,representationObj);
        if(replyObj == null) return false;
        return  password.equals(replyObj.get("password"));
    }

    @Override
    public synchronized boolean addLoginSession(String username, String password, long timeStamp, String ipAdress, String os) {
        if(!loginUser(username, password)) return false;
        
        String searchArgument       = "{'username' : '"+username+"', 'password' : '"+password+"'}";
        String newLog               = "{'timeStamp' : '"+timeStamp+"', 'ipAdress' : '"+ipAdress+"', 'os' : '"+os+"'}";
        String pushArgument         = "{ $push : {'loginLog':"+newLog+"}}";
        DBObject searchObj          = createDBObject(searchArgument);
        DBObject pushObj            = createDBObject(pushArgument);
        
        // push loginLog
        m_client.getUserCollection().update(searchObj, pushObj, false, false);
        return true;
    }

    @Override
    public synchronized boolean registerUser(String username, String password, String email, String gender) {
        checkNotNull(username);
        checkNotNull(password);
        checkNotNull(email);
        checkNotNull(gender);
        
        if(userExist(username)) return false;
        String jsonUser = "{'username' : '"+username+"', 'password' : '"+password+"', 'email' : '"+email+"', 'gender' : '"+gender+"', 'loginLog' : []}";
        String jsonContact = "{'username' : '"+username+"', 'contacts' : []}";
        DBObject userObj = createDBObject(jsonUser);
        DBObject contactObj = createDBObject(jsonContact);
        
        m_client.getUserCollection().insert(userObj);
        m_client.getUserCollection().insert(contactObj);
        return true;
    }

    @Override
    public synchronized boolean addContact(String user1, String user2, long timeStamp) {
        checkNotNull(user1);
        checkNotNull(user2);
        
        if(!userExist(user1) || !userExist(user2)) return false;
        
        String searchArgumentU1       = "{'username' : '"+user1+"'}";
        String searchArgumentU2       = "{'username' : '"+user2+"'}";
        String newContactU1           = "{'username' : '"+user2+"', 'timeStamp':'"+timeStamp+"'}";
        String newContactU2           = "{'username' : '"+user1+"', 'timeStamp':'"+timeStamp+"'}";
        
        String pushArgumentU1         = "{ $push : {'contacts':"+newContactU1+"}}";
        String pushArgumentU2         = "{ $push : {'contacts':"+newContactU2+"}}";
        
        DBObject searchObjU1          = createDBObject(searchArgumentU1);
        DBObject pushObjU1            = createDBObject(pushArgumentU1);
        DBObject searchObjU2          = createDBObject(searchArgumentU2);
        DBObject pushObjU2            = createDBObject(pushArgumentU2);
        
        m_client.getContactCollection().update(searchObjU1, pushObjU1, false, false);
        m_client.getContactCollection().update(searchObjU2, pushObjU2, false, false);
        return true;
    }
    
    @Override
    public synchronized List<OwnContactData> getOwnContacts(String user) throws UserNotFoundException {
        checkNotNull(user);
        if(!userExist(user)) throw new UserNotFoundException();
        
        String searchArgument       = "{'username':'"+user+"'}";
        String presentationArgument = "{'contacts':}";
        
        DBObject searchObj          = createDBObject(searchArgument);
        DBObject presentationObj    = createDBObject(presentationArgument);
        
        List<OwnContactData> contactData = new ArrayList();
        
        List<String> ownList = new ArrayList();
        DBCursor dbCurser    = m_client.getContactCollection().find(searchObj,presentationObj);
        
        while(dbCurser.hasNext()){
            DBObject o = dbCurser.next();
            BasicDBList l = (BasicDBList) o.get("contacts");
            
            DBObject[] contactArray = l.toArray(new DBObject[0]);
            for(DBObject contactObj : contactArray){
                
                String userName = (contactObj.get("username")).toString();
                long timeStamp  = Long.parseLong(contactObj.get("timeStamp").toString());
                OwnContactData c = new ContactDataImpl(userName, timeStamp);
                
                contactData.add(c);
            }
        }
        
        return contactData;
    }

    @Override
    public synchronized List<ContactData> getAllContacts(String meUser) {
        // TODO
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized long addChat(String user1, String user2) throws ChatAlreadyExistsException, UserNotFoundException {
        checkNotNull(user1);
        checkNotNull(user2);
        
        // check user1 and user2 exists
        if(!userExist(user1) || !userExist(user2)) throw new UserNotFoundException();
        
        // todo check exists chat as (user1, user2) || (user2, user1)
        // if not, then continue
        if(chatExists(user1, user2)) throw new ChatAlreadyExistsException();
        
        long timeStamp = System.currentTimeMillis();
        String createArgument = "{'_id' : '"+timeStamp+"', 'username1' : '"+user1+"', 'username2' : '"+user2+"', 'timeStamp': '"+timeStamp+"', 'messages' : []}";
        
        DBObject createObj = createDBObject(createArgument);
        m_client.getChatCollection().insert(createObj);
        return timeStamp;
    }

    private synchronized boolean chatExists(String user1, String user2){
        
        String searchArgument = "{'$or':[{'$and':[{'username1': '"+user1+"'}, {'username2': '"+user2+"'}]}, {'$and':[{'username1': '"+user2+"'}, {'username2': '"+user1+"'}]}]}";
        String presentationArgument = "{'_id': 1}";
        
        DBObject searchObj       = createDBObject(searchArgument);
        DBObject presentationObj = createDBObject(presentationArgument);
        
        
        DBObject reply = m_client.getChatCollection().findOne(searchObj, presentationObj);
        return reply != null;
    }
    
    private synchronized boolean chatExists(long chatID){
        String searchArgument = "{'_id' : '"+chatID+"'}";
        String presentationArgument = "{'_id': 1}";
        
        DBObject searchObj       = createDBObject(searchArgument);
        DBObject presentationObj = createDBObject(presentationArgument);
        
        
        DBObject reply = m_client.getChatCollection().findOne(searchObj, presentationObj);
        return reply != null;
    }
    
    private synchronized boolean mediaExists(long mediaID){
        return r_client.containsKey(mediaID+"");
    }
    
    @Override
    public synchronized void messageSend(MessageData message) throws ChatNotFoundException, UserNotFoundException {
        checkNotNull(message);
        
        // check Chat exists
        if(!chatExists(message.getChatID())) throw new ChatNotFoundException();
        if(!userExist(message.getFromUser())) throw new UserNotFoundException();
        if(!userExist(message.getToUser())) throw new UserNotFoundException();
        
        String searchArgument = "{'_id': '"+message.getChatID()+"'}"; 
        String pushArgument =  "{$push:{'messages':{"+
                "'timeStamp': '"+message.getTimeStamp()+"',"+
                "'fromUser': '"+message.getFromUser()+"',"+
                "'toUser':'"+message.getToUser()+"', "+
                "'message':'"+message.getMessage()+"',"+
                "'mediaID': '"+message.getMediaID()+"', "+
                "'fromUserLocation': '"+message.getFromUserLocation()+"',"+
                "'fromUserWeather':'"+message.getFromUserWeather()+"',"+
                "'toUserLocation':'"+message.getToUserLocation()+"',"+
                "'toUserWeather':'"+message.getToUserWeather()+"'}}}";
        
        DBObject searchObj = createDBObject(searchArgument);
        DBObject pushObj   = createDBObject(searchArgument);
        
        m_client.getChatCollection().update(searchObj,pushObj,false,false);
    }

    @Override
    public synchronized long uploadMediaFile(MediaFile mediaFile) throws MediaFileTooBigException, ChatNotFoundException, UserNotFoundException, MediaUploadFileFailedException {
        checkNotNull(mediaFile);
        
        // check user exists
        if(!userExist(mediaFile.getOwner())) throw new UserNotFoundException();
        // check chat exists
        if(!chatExists(mediaFile.getChatID())) throw new ChatNotFoundException();
        // upload
        long timeStamp = System.currentTimeMillis();
        String value = "{"+
                            "'fileName' : '"+mediaFile.getFileName()+"'"+
                            "'fileType' : '"+mediaFile.getFileType()+"'"+
                            "'timeStamp': '"+mediaFile.getTimeStamp()+"'"+
                            "'owner'    : '"+mediaFile.getOwner()+"'"+
                            "'chatID'   : '"+mediaFile.getChatID()+"'"+
                            "'fileSize' : '"+mediaFile.getFileSize()+"'"+
                            "'fileContent' : '"+mediaFile.getFileContent()+"'}";
        try {
            boolean reply = r_client.put(timeStamp+"", value);
            if(!reply) throw new MediaUploadFileFailedException();
            return timeStamp;
        } catch (ValueIsTooBigException ex) {
            throw new MediaFileTooBigException("FileContent > 1GiB");
        }        
    }

    @Override
    public synchronized MediaFile downloadMediaFile(long mediaID, long chatID, String user) throws MediaFileNotFoundException, 
                                                                                                   ChatNotFoundException, 
                                                                                                   UserNotFoundException,
                                                                                                   MediaFilePermissionDenied{
        checkNotNull(user);
        if(!chatExists(chatID)) throw new ChatNotFoundException();
        if(!userExist(user)) throw new UserNotFoundException();
        
        try {
            String value = r_client.get(mediaID+"");
            DBObject jsonObj = createDBObject(value);
            
            String fName = jsonObj.get("fileName").toString();
            String fType = jsonObj.get("fileType").toString();
            long   tStamp= Long.parseLong(jsonObj.get("timeStamp").toString());
            String owner = jsonObj.get("owner").toString();
            long   cID   = Long.parseLong(jsonObj.get("chatID").toString());
            long   fSize = Long.parseLong(jsonObj.get("fileSize").toString());
            String fContent=jsonObj.get("fileContent").toString();
            
            MediaFile mf = new MediaFileImpl(fName, fType, tStamp, owner, cID, fSize, fContent);
            
            // check user owner of mediaFile
            if(!user.equals(mf.getOwner())) throw new MediaFilePermissionDenied();
            // check chatID is owner of mediaFile
            if(chatID != mf.getChatID()) throw new MediaFilePermissionDenied();
            return mf;
        } catch (KeyNotFoundExeption ex) {
            throw new MediaFileNotFoundException();
        }
    }

    @Override
    public synchronized List<MessageData> getMessages(long chatID) throws ChatNotFoundException {
        // check chat exsists
        if(!chatExists(chatID)) throw new ChatNotFoundException();
        
        String searchArgument = "{'_id': '"+chatID+"'}";
        String presentationArgument = "{ 'messages': '1'}";
        
        DBObject searchObj       = createDBObject(searchArgument);
        DBObject presentationObj = createDBObject(presentationArgument);
        
        DBObject replyObj = m_client.getChatCollection().findOne(searchObj,presentationObj);
        if(replyObj == null) throw new ChatNotFoundException();
        BasicDBList dbList = (BasicDBList) replyObj.get("message");
        
        List<MessageData> messageData = new ArrayList();
        DBObject[] messages = dbList.toArray(new DBObject[0]);
        for(DBObject aMessage : messages){
            
            
            String fromUser = aMessage.get("fromUser").toString();
            String toUser = aMessage.get("toUser").toString();
            long tStamp = Long.parseLong(aMessage.get("timeStamp").toString());
            String message = aMessage.get("message").toString();
            long mediaID = Long.parseLong(aMessage.get("mediaID").toString());
            String fromUserLoc = aMessage.get("fromUserLocation").toString();
            String fromUserWeather= aMessage.get("fromUserWeather").toString();
            String toUserLoc = aMessage.get("toUserLocation").toString();
            String toUserWeather = aMessage.get("toUserWeather").toString();
            
            MessageData m = new MessageDataImpl(chatID, fromUser, toUser, 
                                                tStamp, message, mediaID, 
                                                fromUserLoc, fromUserWeather, 
                                                toUserLoc, toUserWeather);
            messageData.add(m);
        }
        return messageData;
    }

    @Override
    public synchronized List<MessageData> getMessages(String username1, String username2) throws ChatNotFoundException {
        // check chat exsists
        if(!chatExists(username1,username2)) throw new ChatNotFoundException();
        
        String searchArgument = "{'$or':[{'$and':[{'username1': '"+username1+"'}, {'username2': '"+username2+"'}]}, {'$and':[{'username1': '"+username2+"'}, {'username2': '"+username1+"'}]}]}";
        String presentationArgument = "{ 'messages': '1','_id':'1'}";
        
        DBObject searchObj       = createDBObject(searchArgument);
        DBObject presentationObj = createDBObject(presentationArgument);
        
        DBObject replyObj = m_client.getChatCollection().findOne(searchObj,presentationObj);
        if(replyObj == null) throw new ChatNotFoundException();
        BasicDBList dbList = (BasicDBList) replyObj.get("message");
        
        List<MessageData> messageData = new ArrayList();
        DBObject[] messages = dbList.toArray(new DBObject[0]);
        long chatID = Long.parseLong(replyObj.get("_id").toString());
        for(DBObject aMessage : messages){
            
            String fromUser = aMessage.get("fromUser").toString();
            String toUser = aMessage.get("toUser").toString();
            long tStamp = Long.parseLong(aMessage.get("timeStamp").toString());
            String message = aMessage.get("message").toString();
            long mediaID = Long.parseLong(aMessage.get("mediaID").toString());
            String fromUserLoc = aMessage.get("fromUserLocation").toString();
            String fromUserWeather= aMessage.get("fromUserWeather").toString();
            String toUserLoc = aMessage.get("toUserLocation").toString();
            String toUserWeather = aMessage.get("toUserWeather").toString();
            
            MessageData m = new MessageDataImpl(chatID, fromUser, toUser, 
                                                tStamp, message, mediaID, 
                                                fromUserLoc, fromUserWeather, 
                                                toUserLoc, toUserWeather);
            messageData.add(m);
        }
        return messageData;
    }
    
    
//    public static void main(String[] args) throws UnknownHostException, DataBaseNotFoundException {
//        RedisConfiguration r_conf = ClientFactory.createRedisDBConfiguration("localhost");
//        MongoConfiguration m_conf = ClientFactory.createMongoDBConfigruation("localhost", "test", "userCollection", "contactCollection", "chatCollection");
//        
//        RedisClient r_client = ClientFactory.createRedisClient(r_conf);
//        MongoClient m_client = ClientFactory.createMongoClient(m_conf);
//        
//        ServerPersistence serverPersistence = new ServerPersistenceImpl(r_client, m_client);
//        
//        System.out.print("register account ... ");
//        boolean result = serverPersistence.registerUser("mongooo", "dada", "mongo@dada.de", "haustier");
//        System.out.println((result? "OK": "FAIL"));
//        System.out.print("login ... ");
//        result = serverPersistence.loginUser("mongooo", "dada");
//        System.out.println((result? "OK": "FAIL"));
//        System.out.print("addLoginSession");
//        result = serverPersistence.addLoginSession("mongooo", "dada",System.currentTimeMillis(), "localhost", "Linux");
//        System.out.println((result? "OK": "FAIL"));
////        m_client.getUserCollection().drop();
//        
//    }

}
