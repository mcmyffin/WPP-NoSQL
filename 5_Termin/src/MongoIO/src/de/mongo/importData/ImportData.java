package de.mongo.importData;

import static com.google.common.base.Preconditions.checkNotNull;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import de.server.persistence.result.MessageData;
import de.server.persistence.result.MessageDataImpl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;



/**
 *
 * @author dima
 */
public class ImportData implements IImportData{

    
    
    @Override
    public List<MessageData> loadFile(File f) throws IOException{
        checkNotNull(f);
        
        String spitBy = " , \"message\" : ";
        
        FileReader fileReader = new FileReader(f);
        BufferedReader br = new BufferedReader(fileReader);
                
        List<MessageData> list = new ArrayList(1000);
        
        for(String line = br.readLine(); line != null; line = br.readLine()){
            
            try{
                
                String[] splitedLine = line.split(spitBy);
                DBObject jsObj = (DBObject) JSON.parse(splitedLine[0]+"}");
                
                String user = jsObj.get("username").toString();
                long tstamp = Long.parseLong(jsObj.get("_id").toString());
                String message = splitedLine[1].substring(1, splitedLine[1].length()-2);
                message = message.replace("\\\"", "\"");
                message = message.replace("\\\\", "\\");
                
                
//                message = message.replace("\"", "");
                
                MessageData md = new MessageDataImpl(user,tstamp,message);
                list.add(md);
                
            }catch(RuntimeException ex){
                ex.printStackTrace();
            }
        }
        return list;
    }
    
}
