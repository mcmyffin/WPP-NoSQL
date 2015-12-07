package nosql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dima
 */
public class DataManager{

    
    private static boolean isFileValid(File aFile){
        if(aFile != null) return aFile.exists() && aFile.isFile();
        else return false;
    }
    
    
    public static List<String> readFile(String filePath) throws FileNotFoundException, IOException{
        
        File aFile = new File(filePath);
        if(!isFileValid(aFile)) throw new FileNotFoundException();
        
        FileReader     fReader = new FileReader(aFile);
        BufferedReader bReder = new BufferedReader(fReader);
        
        List<String> fileContent = new ArrayList(200);
        for(String line = bReder.readLine(); line != null ; line = bReder.readLine()){
            
            fileContent.add(line);
        }
        
        return fileContent;
    }
    
}
