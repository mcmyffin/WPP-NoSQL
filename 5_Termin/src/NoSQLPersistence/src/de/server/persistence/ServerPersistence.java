package de.server.persistence;

import de.server.persistence.result.MediaFile;
import de.server.persistence.Exceptions.*;
import de.server.persistence.result.ContactData;
import de.server.persistence.result.OwnContactData;
import de.server.persistence.result.MessageData;
import java.util.List;

/**
 *
 * @author nosql
 */
public interface ServerPersistence {
    
    // User
    public boolean loginUser(String username, String password);
    public boolean addLoginSession(String username, String password, long timeStamp, String ipAdress, String os);
    public boolean registerUser(String username, String password, String email, String gender);

    public boolean addContact(String user1, String user2, long timeStamp);
    public List<OwnContactData> getOwnContacts(String user) throws UserNotFoundException;
    public List<ContactData> getAllContacts(String user) throws UserNotFoundException;
    
    // Chat create and messages send
    public long addChat(String user1, String user2)  throws ChatAlreadyExistsException, UserNotFoundException;
    
    public void messageSend(MessageData message) throws ChatNotFoundException, UserNotFoundException;
    
    public long uploadMediaFile(MediaFile mediaFile) throws MediaFileTooBigException, ChatNotFoundException, 
                                                            UserNotFoundException, MediaUploadFileFailedException;
    
    public MediaFile downloadMediaFile(long mediaID, long chatID, String user) throws MediaFileNotFoundException, ChatNotFoundException,
                                                                                      UserNotFoundException, MediaFilePermissionDenied;

    // Message resieve
    public List<MessageData> getMessages(long chatID) throws ChatNotFoundException;
    public List<MessageData> getMessages(String username1, String username2) throws ChatNotFoundException;
}
