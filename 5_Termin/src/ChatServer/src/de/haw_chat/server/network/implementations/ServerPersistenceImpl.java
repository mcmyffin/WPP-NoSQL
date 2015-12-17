package de.haw_chat.server.network.implementations;

import com.mongodb.*;
import com.mongodb.util.JSON;
import de.haw_chat.server.network.Exceptions.MediaFileAlreadyExistsException;
import de.haw_chat.server.network.Exceptions.MediaFileNotFoundException;
import de.haw_chat.server.network.Exceptions.MediaFileTooBigException;
import de.haw_chat.server.network.interfaces.ServerPersistence;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by dima on 15.12.15.
 */
public class ServerPersistenceImpl implements ServerPersistence {

    final Jedis redisClient; // need Adress
    final Mongo mongoClient; // need Adress


    /*** REDIS ***/
    private final String r_OK        = "OK";
    private final String r_NOT_FOUND = "nil";

    private final long maxMediaSizeInByte = 1073741824;
    /*** MONGO ***/
    final String m_coll_user    = "User";
    final String m_coll_contact = "Contac";
    final String m_coll_chat    = "Chat";

    private DB           mongoDB;
    DBCollection coll_user;
    DBCollection coll_contact;
    DBCollection coll_chat;

    public ServerPersistenceImpl(String redisAdress, String mongoAdress, String mongoDB) throws UnknownHostException {
        checkNotNull(redisAdress);
        checkNotNull(mongoAdress);

        this.redisClient = new Jedis(redisAdress);
        this.mongoClient = new Mongo(mongoAdress);
        this.mongoDB     = mongoClient.getDB(mongoDB);
    }

    private void initSessing(){
        if(mongoDB.collectionExists(m_coll_user))   coll_user   = mongoDB.getCollection(m_coll_user);
        if(mongoDB.collectionExists(m_coll_contact))coll_contact= mongoDB.getCollection(m_coll_contact);
        if(mongoDB.collectionExists(m_coll_chat))   coll_chat   = mongoDB.getCollection(m_coll_chat);

        if(coll_user    == null) coll_user   = mongoDB.createCollection(m_coll_user,null);
        if(coll_contact == null) coll_contact= mongoDB.createCollection(m_coll_contact,null);
        if(coll_chat    == null) coll_chat   = mongoDB.createCollection(m_coll_chat,null);
    }

    private DBObject createDBObject(String json){
        return (DBObject) JSON.parse(json);
    }

    private boolean isUserExist(String username){
        String searchArgument       = "{'username' : '"+username+"'}";
        String searchRepresentation = "{'username' : '1'}";
        DBObject searchObj          = createDBObject(searchArgument);
        DBObject representationObj  = createDBObject(searchRepresentation);

        DBObject replyObj = coll_user.findOne(searchObj,representationObj);
        return replyObj.containsField("username");
    }

    @Override
    public boolean loginUser(String username, String password) {
        checkNotNull(username);
        checkNotNull(password);

        String searchArgument       = "{'username' : '"+username+"'}";
        String searchRepresentation = "{'password' : '1'}";
        DBObject searchObj          = createDBObject(searchArgument);
        DBObject representationObj  = createDBObject(searchRepresentation);

        DBObject replyObj = coll_user.findOne(searchObj,representationObj);
        return  password.equals(replyObj.get("password"));
    }

    @Override
    public boolean addLoginSession(String username, String password, long timeStamp, String ipAdress, String os) {
        checkNotNull(username);
        checkNotNull(password);
        checkNotNull(ipAdress);
        checkNotNull(os);

        String searchArgument       = "{'username' : '"+username+"'}";
        DBObject searchObj          = createDBObject(searchArgument);
        DBObject representationObj  = new BasicDBObject();
        DBObject log                = createDBObject("{'timeStamp' : '"+timeStamp+"', 'ipAdress' : '"+ipAdress+"', 'os' : '"+os+"'}");

        DBObject userObj = coll_user.findOne(searchObj,representationObj);
        if(!password.equals(userObj.get("password"))) return false;

        DBObject loginLog = (DBObject) userObj.get("loginLog");
//        loginLog.

        return false;
    }

    @Override
    public boolean registerUser(String username, String password, String email, String gender) {
        checkNotNull(username);
        checkNotNull(password);
        checkNotNull(email);
        checkNotNull(gender);
        return false;
    }

    @Override
    public long addChat(String user1, String user2) {
        checkNotNull(user1);
        checkNotNull(user2);
        return 0;
    }

    @Override
    public boolean messageSend(long timeStamp, long chatID, String fromUser, String toUser, long mediaID,
                               String fromUserLoc, String fromUserWeather, String toUserLoc, String toUserWeather) {
        checkNotNull(fromUser);
        checkNotNull(toUser);
        checkNotNull(toUser);
        checkNotNull(fromUserLoc);
        checkNotNull(fromUserWeather);
        checkNotNull(toUserLoc);
        checkNotNull(toUserWeather);

        return false;
    }

    @Override
    public synchronized long uploadMediaFile(String base64MediaFile) throws MediaFileAlreadyExistsException, MediaFileTooBigException {
        checkNotNull(base64MediaFile);

        // check MediaFile Size
        if(base64MediaFile.getBytes().length > maxMediaSizeInByte) throw new MediaFileTooBigException();
        // check Redis Connection
        if(!redisClient.isConnected()) redisClient.connect();

        // create Timestamp as MediaID
        long timeStamp = System.currentTimeMillis();

        // upload to Redis
        String reply = redisClient.set(Long.toString(timeStamp),base64MediaFile);

        if(!reply.equals(r_OK)) throw new MediaFileAlreadyExistsException();
        return timeStamp;
    }

    @Override
    public String downloadMediaFile(long mediaID) throws MediaFileNotFoundException{
        // check Redis Connection
        if(!redisClient.isConnected()) redisClient.connect();
        // download MediaFile Base64
        String reply = redisClient.get(Long.toString(mediaID));

        if(reply.equals(r_NOT_FOUND)) throw new MediaFileNotFoundException();
        return reply;
    }

    @Override
    public List<String> getMessages(long chatID) {
        return null;
    }

    @Override
    public List<String> getMessages(String username1, String username2) {
        checkNotNull(username1);
        checkNotNull(username2);
        return null;
    }

    public static void main(String[] args) {

    }
}
