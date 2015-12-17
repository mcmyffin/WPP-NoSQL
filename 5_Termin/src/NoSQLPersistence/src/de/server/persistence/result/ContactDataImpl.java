package de.server.persistence.result;


import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author nosql
 */
public class ContactDataImpl implements OwnContactData, ContactData{

    private final String contactName;
    private final long timeStamp;
    
    public ContactDataImpl(String contactName, long timeStamp){
        checkNotNull(contactName);
        
        this.contactName = contactName;
        this.timeStamp   = timeStamp;
    }
    
    @Override
    public String getContactName() {
        return this.contactName;
    }

    @Override
    public long getTimeStamp() {
        return this.timeStamp;
    }
    
}
