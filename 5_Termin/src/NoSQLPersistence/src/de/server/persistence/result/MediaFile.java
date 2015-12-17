package de.server.persistence.result;

/**
 *
 * @author nosql
 */
public interface MediaFile {

    public String getFileName();
    public String getFileType();
    public long getTimeStamp();
    
    public String getOwner();
    public long getChatID();
    
    public long getFileSize();
    public String getFileContent();
}
