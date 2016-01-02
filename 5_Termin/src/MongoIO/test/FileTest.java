import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dima
 */
public class FileTest {
    
    public FileTest() {
    }
    
    @Test
    public void testTest() throws FileNotFoundException, IOException{
        
        File f_1 = new File("/home/dima/Desktop/exportMongo.txt");
        FileReader fileReader_1 = new FileReader(f_1);
        BufferedReader br_1 = new BufferedReader(fileReader_1);
        
        
        File f_2 = new File("/home/dima/Desktop/exportMongo_1.txt");
        FileReader fileReader_2 = new FileReader(f_2);
        BufferedReader br_2 = new BufferedReader(fileReader_2);
        
        
        
        String line_1 = br_1.readLine();
        String line_2 = br_2.readLine();
        
        while(line_1 != null && line_2 != null){
            
//            if(!line_1.equals(line_2)){
//                System.out.println("Original : "+line_1);
//                System.out.println("Copy     : "+line_2);
//            }
            assertEquals(line_2, line_1);
            line_1 = br_1.readLine();
            line_2 = br_2.readLine();
        }
        
        assertTrue(line_1 == null && line_2 == null);
    }
    
}
