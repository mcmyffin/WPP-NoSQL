package de.mongo.exportData;

import static com.google.common.base.Preconditions.checkNotNull;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import de.server.persistence.result.MessageData;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutput;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dima
 */
public class ExportData implements IExportData{

    
    
    @Override
    public boolean saveToFile(File f, List<MessageData> list) throws IOException{
        checkNotNull(f);
        checkNotNull(list);
        
        if(!f.exists()) f.createNewFile();
        else return false;
        OutputStreamWriter writer = new FileWriter(f);
        BufferedWriter bf = new BufferedWriter(writer);
        
        for(MessageData md : list){
            DBObject dbObj = new BasicDBObject();
            
            dbObj.put("_id", md.getTimeStamp());
            dbObj.put("username", md.getUser());
            dbObj.put("message", md.getMessage());
            
            bf.write(dbObj.toString());
            bf.newLine();
        }
        bf.close();
        writer.close();
        return true;
    }
    
}
