package redis;

import redis.clients.jedis.Jedis;

import java.util.Scanner;

/**
 * Created by dima on 02.11.15.
 */
public class redis {

    private static Scanner sc = new Scanner(System.in);

    private static String read(){

        return sc.nextLine();
    }

    public static void main(String[] args) {


//        if(args.length != 2) System.exit(1);


        Jedis client = new Jedis("141.22.64.0");



        for(String line = read(); !line.equals("exit"); line = read()){

            if(line.equals("set")){

                System.out.println("ADD KEY-Value to DB");
                System.out.println("<KEY>");;

                String key = read();

                System.out.println("<VALUE>");

                String value = read();

                String returnVal = client.set(key,value);;
                System.out.println(returnVal);


//            }else if(line.equals("get")){
//
//                System.out.println("GET KEY-VALUE from DB");
//                System.out.println("<KEY>");
//
//                String key = read();
//
//                try {
//                    String value = new String(client.get(key));
//                    System.out.println(value);
//                } catch (RedisException e) {
//                    System.out.println("ERROR: "+e.getMessage());
//                }
            }else{

                System.out.println("UNSOPORTED COMMAND");
                System.out.println("Commands:");
                System.out.println("  get");
                System.out.println("  set");
                System.out.println("  exit");
            }
        }

    }
}
