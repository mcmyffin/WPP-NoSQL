package redis;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import redis.clients.jedis.Jedis;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import redis.data_manager.DataManager;
import redis.data_manager.IDataManager;
import redis.parser.JSonParser;

/**
 * Created by dima on 02.11.15.
 */
public class redis {

    private static String arg_i    = "-i";
    private static String arg_imp  = "--import";
    private static String arg_help = "--help";
    private static String arg_h    = "-h";
    private static String arg_plz  = "--plz";
    private static String arg_p    = "-p";
    private static String arg_city = "--city";
    private static String arg_c    = "-c";
    private static String exit     = "exit";
    private static String usage = "redis [ARGUMENT] <IP-Adress/Hostname>\n\n"
                                +"   ------------------ [ARGUMENT] ------------------\n"                    
                                +"   <adress/hostname>  : IP-Adress/Hostname from Redis-Server\n\n"
                                +"   "+arg_h+" | "+arg_help+" : show Help usage\n"
                                +"   "+arg_p+" | "+arg_plz+"  : start interactive search for plz number\n"
                                +"   "+arg_c+" | "+arg_city+" : start interactive search for City value\n"
                                +"   "+arg_i+" | "+arg_imp+"  : import from file\n"
                                +"   ------------------------------------------------\n"
                                +"   "+exit+"  : close interactive search\n";
                                
    private static Scanner scanner = new Scanner(System.in);
    private static String read(){

        return scanner.nextLine();
    }

    public static void main(String[] args) {

        /* wrong arguments count */
        if(args.length < 2 || args.length > 3) {
            showUsage(true);
        }
        
        String argument = args[0];
        String adress   = args[1];

        /* Create Redis-Client */
        Jedis client = new Jedis(adress);

        
        /* interactive search with PLZ */
        if(argument.equals(arg_plz) || argument.equals(arg_p)){
            print("interactive PLZ search activated");
            searchForPLZ(client,scanner);
            print("interactive PLZ search deactivated");
        }
        
        /* interactive search with City */
        else if(argument.equals(arg_city) || argument.equals(arg_c)){
            print("interactive City search activated");
            searchForCity(client, scanner);
            print("interactive City search deactivated");
        }
        
        /* import file */
        else if(argument.equals(arg_i) || argument.equals(arg_imp)){
            print("import file, please type in correctly file-path");
            String filepath = read();
            print("wait a second ...");
            
            IDataManager dataManager = new DataManager();
            
            try {
                List<String> dataContent  = dataManager.readFile(new File(filepath));
                List<JSONObject> jsonList = JSonParser.parseToJSON(dataContent);
                
                print(jsonList.size()+" obj. found");
                print("try to import ...");
                
                String id   = "_id";
                String city = "city";
                String loc  = "loc";
                String pop  = "pop";
                String state= "state";
                
                Map<String,String> map = new HashMap();
                for(JSONObject o : jsonList){
                    
                    map.clear();
                    print("add id: "+o.get(id));
                    
                    map.put(city, o.get(city).toString());
                    map.put(loc, o.get(loc).toString());
                    map.put(pop, o.get(pop).toString());
                    map.put(state, o.get(state).toString());
                    
                    String returncode = client.hmset(o.get(id).toString(), map);
                    print("[SERVER] : "+returncode);
                }
                
            } catch (JSONException | IOException ex) {
                print(ex.getMessage());
                System.exit(2);
            }
        }
        
        else{
            showUsage(true);
        }
    }
    
    private static void showUsage(boolean withExit){
        System.out.println(usage);
        if(withExit) System.exit(1);
    }
    
    private static void print(String message){
        System.out.println(">> "+message);
    }
    
    
    private static void searchForPLZ(Jedis client, Scanner scanner){
        
        print("NOT IMPLEMENTED !!!");
        System.exit(2);
        
        for(String line = read(); !line.equals(exit); line = read()){
            
        }
    }
    
    private static void searchForCity(Jedis client, Scanner scanner){
        
        print("NOT IMPLEMENTED !!!");
        System.exit(2);
        for(String line = read(); !line.equals(exit); line = read()){
            
        }
    }
}
