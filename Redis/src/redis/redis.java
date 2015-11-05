package redis;

import redis.clients.jedis.Jedis;

import java.util.Scanner;

/**
 * Created by dima on 02.11.15.
 */
public class redis {

    private static String arg_help = "--help";
    private static String arg_h    = "-h";
    private static String arg_plz  = "--plz";
    private static String arg_p    = "-p";
    private static String arg_city = "--city";
    private static String arg_c    = "-c";
    private static String exit     = "exit";
    private static String usage = "redis [ARGUMENT] <adress/hostname>\n"
                                +"   <adress/hostname>  : IP-Adress/Hostname from Redis-Server\n\n"
                                +"   "+arg_h+" | "+arg_help+" : show Help usage\n"
                                +"   "+arg_p+" | "+arg_plz+"  : start interactive search for plz number\n"
                                +"   "+arg_c+" | "+arg_city+" : start interactive search for City value\n"
                                +"   ------------------------------------------------\n"
                                +"   "+exit+"  : close interactive search\n";
                                
    private static Scanner scanner = new Scanner(System.in);
    private static String read(){

        return scanner.nextLine();
    }

    public static void main(String[] args) {

        /* wrong arguments count */
        if(args.length != 2) {
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
