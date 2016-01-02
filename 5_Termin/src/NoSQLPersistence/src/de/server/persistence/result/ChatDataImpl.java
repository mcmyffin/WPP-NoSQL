package de.server.persistence.result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author dima
 */
public class ChatDataImpl implements ChatData{
    
    private final long id;
    private final String u1;
    private final String u2;
    private final long t;

    public ChatDataImpl(long chatID, String user1, String user2, long timeStamp) {
        checkNotNull(user1);
        checkNotNull(user2);
        
        this.id = chatID;
        this.u1 = user1;
        this.u2 = user2;
        this.t = timeStamp;
    }
    
    

    @Override
    public long getChatID() {
        return id;
    }

    @Override
    public String getUser1() {
        return u1;
    }

    @Override
    public String getUser2() {
        return u2;
    }

    @Override
    public long getTimeStamp() {
        return t;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChatDataImpl other = (ChatDataImpl) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
