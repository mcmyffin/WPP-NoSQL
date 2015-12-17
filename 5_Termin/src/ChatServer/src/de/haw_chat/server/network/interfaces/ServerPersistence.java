package de.haw_chat.server.network.interfaces;

import de.haw_chat.server.network.Exceptions.MediaFileAlreadyExistsException;
import de.haw_chat.server.network.Exceptions.MediaFileNotFoundException;
import de.haw_chat.server.network.Exceptions.MediaFileTooBigException;

import java.util.List;

/**
 * Created by dima on 14.12.15.
 */
public interface ServerPersistence {

    // User
    public boolean loginUser(String username, String password);
    public boolean addLoginSession(String username, String password, long timeStamp, String ipAdress, String os);
    public boolean registerUser(String username, String password, String email, String gender);

    // Chat create and messages send
    public long addChat(String user1, String user2);
    public boolean messageSend(long timeStamp, long chatID, String fromUser, String toUser,long mediaID,
                               String fromUserLoc, String fromUserWeather, String toUserLoc, String toUserWeather);

    public long uploadMediaFile(String base64MediaFile) throws MediaFileAlreadyExistsException, MediaFileTooBigException;
    public String downloadMediaFile(long mediaID) throws MediaFileNotFoundException;

    // Message resieve
    public List<String> getMessages(long chatID);
    public List<String> getMessages(String username1, String username2);

}
