package redis.data_manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author dima
 */
public interface IDataManager {
    
    public List<String> readFile(File aFile) throws FileNotFoundException,IOException;
    
}
