package de.haw_chat.server;

import de.haw_chat.server.network.implementations.DeviceFactory;
import de.haw_chat.server.network.interfaces.Server;
import de.haw_chat.server.network.interfaces.ServerConfiguration;

import java.util.Scanner;

/**
 * Created by Andreas on 31.10.2015.
 */
public class Main {

    private static int defaultPort       = 12345;
    private static int defaultMaxClients = 30;
    private static boolean defaultSsl    = false;

    private static final String opt_help =  "-h";
    private static final String com_exit =  "exit";

    private static String usage  =  "-- USAGE --\n"+
                                    "   *.jar [OPTION] <PORT> <MAX_CLIENTS> <SSL>\n\n"+
                                    "   [OPTION]        : "+ opt_help +"\n" +
                                    "   <PORT>          : port number. Default 12345\n"+
                                    "   <MAX_CLIENTS>   : maximum Client connections\n"+
                                    "   <SSL>           : 1 for ssl enable or 0 for disable. Default 0\n";

    private static void showUsage(String message){
        System.out.println(message);
        System.out.println(usage);
        System.exit(1);
    }

    private static void showUsage(){
        System.out.println(usage);
        System.exit(1);
    }

//    private

    public static void main(String[] args) {

        if(args.length != 0 && args.length < 3) showUsage();
        else if(args.length == 1 && args[0] == opt_help) showUsage();
        else if(args.length == 3){
            try{
                defaultPort       = Integer.parseInt(args[0]);
                defaultMaxClients = Integer.parseInt(args[1]);
                defaultSsl        = Integer.parseInt(args[2]) == 1;
            }catch (NumberFormatException ex){
                showUsage(">> INVALID ARGUMENT FORMAT");
            }
        }else if (args.length != 0) showUsage();

        Scanner sc = new Scanner(System.in);
        ServerConfiguration configuration =
                DeviceFactory.createChatServerConfiguration(defaultPort, defaultMaxClients, defaultSsl);
        Server server = DeviceFactory.createChatServer(configuration);

        Thread thread = new Thread(server);
        thread.start();

        System.out.println("SERVER STARTED!");

        String readedLine = null;
        while(true){
            readedLine = sc.nextLine();
            if(readedLine.equals(com_exit)) System.exit(0);
        }
    }
}
