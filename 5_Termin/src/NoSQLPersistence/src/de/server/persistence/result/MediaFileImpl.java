package de.server.persistence.result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author nosql
 */
public class MediaFileImpl implements MediaFile{
    
    private final String fileName;
    private final String fileType;
    private final long timeStamp;
    private final String owner;
    private final long chatID;
    private final long fileSize;
    private final String fileContent;
    
    public MediaFileImpl(String fName, String fType, long tStamp, String owner, long chatID, long fileSize, String fContent){
        checkNotNull(fName);
        checkNotNull(fType);
        checkNotNull(owner);
        checkNotNull(fContent);
        
        this.fileName = fName;
        this.fileType = fType;
        this.timeStamp = tStamp;
        this.owner = owner;
        this.chatID = chatID;
        this.fileSize = fileSize;
        this.fileContent = fContent;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getFileType() {
        return fileType;
    }

    @Override
    public long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public long getChatID() {
        return chatID;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

    @Override
    public String getFileContent() {
        return fileContent;
    }
    
}
