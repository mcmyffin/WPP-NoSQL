package nosql;

import java.io.File;
import java.io.IOException;
import java.rmi.ConnectIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.json.JSONException;
import org.json.JSONObject;



/**
 *
 * @author nosql
 */
public class HBaseClient {

    private static String tableName = "PLZ_CITY";
    private static String family    = "Family";
    private static String f_family  = "Fussball";
    private static String c_column  = "City";
    private static String plz_count = "PLZ_C";
    private static String plz_n     = "PLZ_";
    
    private static String escape    = "exit";
    
    // arguments
    public static String arg_city   = "--city";
    public static String arg_c      = "-c";
    public static String arg_plz    = "--plz";
    public static String arg_p      = "-p";
    public static String arg_imp    = "--import";
    public static String arg_i      = "-i";
    public static String arg_help   = "--help";
    public static String arg_h      = "-h";
    public static String arg_u      = "-u";
    public static String arg_update = "--update";

    
//    public static boolean addColumnFamily(Connection con, String tableName, String newFamilyName){
//        try {
//            
//            if(con.isClosed() || con.isAborted()){
//                System.out.println("\n>> CANNOT CONNECT TO SERVER !!!");
//                return false;
//            }
//            
//            Admin admin = con.getAdmin();
//            
//            // disable table
//            admin.disableTable(TableName.valueOf(tableName));
//            
//            HTableDescriptor tdiscriptor = admin.getTableDescriptor(TableName.valueOf(tableName));
//            HColumnDescriptor cdiscriptor = new HColumnDescriptor(newFamilyName);
//
//            
//            tdiscriptor.addFamily(cdiscriptor);
//            admin.enableTable(TableName.valueOf(tableName));
//            
//            return true;
//        }catch(IOException ex){
//            return false;
//        }
//    }
    
    public static boolean createSchemaTables (Connection connection, String tableName, String familyName) {
        try {
            
            Admin admin = connection.getAdmin();
            if(connection.isClosed() || connection.isAborted()){
                System.out.println("CANNOT CONNECT TO SERVER !!!");
                return false;
            }
            
            HTableDescriptor table = new HTableDescriptor(TableName.valueOf(tableName));
            table.addFamily(new HColumnDescriptor(familyName));
            table.addFamily(new HColumnDescriptor(f_family));
            

            try{
                System.out.print("Creating table: "+tableName);
                admin.createTable(table);
                System.out.println(" Done.");
                return true;
            }catch(IOException e){
                System.err.println(" FAIL");
                System.err.println("CANNOT CREATE TABLE: TABLE EXISTS");
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }
    
    public static boolean deleteTable(Connection con, String tableName) throws IOException{
        Admin admin = con.getAdmin();
        try{
            TableName tName = TableName.valueOf(tableName);
            if(!admin.isTableDisabled(tName));
                admin.disableTable(TableName.valueOf(tableName));
            admin.deleteTable(TableName.valueOf(tableName));
            return true;
        }catch(IOException ex){
            return false;
        }
    }
    
    /**
     * Write a Value to Database.
     * 
     * If Value exists, than override oder new Version will be create
     * 
     * @param tName         Unique Tablename
     * @param keyElement    Unique KeyName
     * @param family        Unique FamilyName
     * @param column        New ColumnName
     * @param value         New Value
     */
    public static void writeAValue(String tName, String keyElement, String family, String column, String value) throws IOException{
        Connection con = getServerConnection();
        writeAValue(con, tName, keyElement, family, column, value);
        con.close();
    }
    
    /**
     * Write a Value to Database.
     * 
     * If Value exists, than override oder new Version will be create
     * 
     * @param con           Server connection
     * @param tName         Unique TableName
     * @param keyElement    Unique KeyName
     * @param family        Unique FamilyName
     * @param column        New ColumnName
     * @param value         New Value
     * @throws IOException 
     */
    public static void writeAValue(Connection con, String tName, String keyElement, String family, String column, String value) throws IOException{
        
        if(con.isClosed() || con.isAborted()){
            System.out.println("CANNOT CONNECT TO SERVER !!!");
            return;
        }

        TableName tableName = TableName.valueOf(tName);
        Table htable = con.getTable(tableName);

        byte[] keyBytes         = Bytes.toBytes(keyElement);
        byte[] familyBytes      = Bytes.toBytes(family);
        byte[] columnBytes      = Bytes.toBytes(column);
        byte[] valueBytes       = Bytes.toBytes(value);

        Put p = new Put(keyBytes);
        p.addColumn(familyBytes, columnBytes, valueBytes);

        try{
//            System.out.print("Try to put some Data ... ");
            htable.put(p);
//            System.out.println("Done.");
        }catch(IOException ex){
            System.err.println("CONNOT PUT DATA TO SERVER !!!");
            ex.printStackTrace();
        }
    }
    
    
    public static String readAValue (String tName, String keyElement, String family, String column) throws ConnectIOException, IOException{
            Connection con = getServerConnection();
            String value = readAValue(con, tName, keyElement, family, column);
            con.close();
            return value;
    }
    
    
    public static String readAValue (Connection con, String tName, String keyElement, String family, String column) throws ConnectIOException, IOException{
            
            if(con.isClosed() || con.isAborted()){
                System.out.println("CANNOT CONNECT TO SERVER !!!");
                throw new ConnectIOException("Connection Closed");
            }
            
            TableName tableName = TableName.valueOf(tName);
            Table htable = con.getTable(tableName);
            
            byte[] keyBytes         = Bytes.toBytes(keyElement);
            byte[] familyBytes      = Bytes.toBytes(family);
            byte[] columnBytes      = Bytes.toBytes(column);
            
            Get g = new Get(keyBytes);
            Result result = htable.get(g);
            
            byte[] value = result.getValue(familyBytes, columnBytes);
            return Bytes.toString(value);
    }
    
    public static boolean existsValue(Connection con, String tName, String keyElement) throws ConnectIOException, IOException{
        if(con.isClosed() || con.isAborted()){
                System.out.println("CANNOT CONNECT TO SERVER !!!");
                throw new ConnectIOException("Connection Closed");
        }    
        
        TableName tableName = TableName.valueOf(tName);
        Table htable = con.getTable(tableName);

        Get g = new Get(Bytes.toBytes(keyElement));
        return htable.exists(g);
    }
    
    private static Connection getServerConnection() throws IOException{
        Configuration conf = HBaseConfiguration.create();
        return ConnectionFactory.createConnection(conf);
    }
    
    
    
    public static String getCity(Connection c, String plz) throws IOException{
        return readAValue(c, tableName, plz, family, c_column);
    }
    
    public static int getPLZ_Count(Connection c, String city) throws IOException{
        if(!existsValue(c, tableName, city)) return 0;
        String count = readAValue(c, tableName, city, family, plz_count);
        
        return Integer.parseInt(count);
    }
    
    public static List<String> getPLZ(Connection c, String city) throws IOException{
        int count = getPLZ_Count(c, city);
        
        List<String> plzList = new ArrayList(count);
        for(int i = 1; i <= count; i++){
            String plz = readAValue(c, tableName, city, family, plz_n+i);
            plzList.add(plz);
        }
        return plzList;
    }
    
    public static void startSearchPLZ() throws IOException{
        Scanner s = new Scanner(System.in);
        String ask = ">> City?";
       
        Connection con = getServerConnection();
        System.out.println(">> OPEN SERVER CONNECTION");
        System.out.println(">> escape with: "+escape);
        
        System.out.println(ask);
        for(String answer = s.nextLine(); !answer.equals(escape) ; answer = s.nextLine()){
            
            long startTime = System.currentTimeMillis();
            List<String> plzList = getPLZ(con, answer);
            double searchTime = (System.currentTimeMillis() - startTime)/1000.0;
            System.out.println(">> "+plzList.toString()+" found in "+searchTime+" sec.");
            System.out.println(ask);
        }
        
        con.close();
        System.out.println(">> CLOSE SERVER CONNECTION");
    }
    
    public static void startSearchCity() throws IOException{
        Scanner s = new Scanner(System.in);
        String ask = ">> PLZ ?";
        
        Connection con = getServerConnection();
        System.out.println(">> OPEN SERVER CONNECTION");
        System.out.println(">> escape with: "+escape);
        System.out.println(ask);
        for(String answer = s.nextLine(); !answer.equals(escape) ; answer = s.nextLine()){    
            
            long startTime = System.currentTimeMillis();
            String city = getCity(con, answer);
            double searchTime = (System.currentTimeMillis() - startTime)/1000.0;
            System.out.println(">> "+city+" found in "+searchTime+" sec.");
            System.out.println(ask);
        }
        
        con.close();
        System.out.println(">> CLOSE SERVER CONNECTION");
    }
    
    public static void showUsage(){
        System.out.println("=== USAGE ===");
        System.out.println("\t-[OPTION] <ARGUMENT>");
        System.out.println("\tShow Usage:");
        System.out.println("\t\t"+arg_h+" OR "+arg_help);
        System.out.println("\tInteractive search for PLZ");
        System.out.println("\t\t"+arg_p+" OR "+arg_plz);
        System.out.println("\tInteractive search for CITY");
        System.out.println("\t\t"+arg_c+" OR "+arg_city);
        System.out.println("\tImport Data");
        System.out.println("\t\t"+arg_i+" OR "+arg_imp+" AND \"FULL_PATH/FILE\"");
        System.out.println("\tUpdate Data");
        System.out.println("\t\t"+arg_u+" OR "+arg_update);
        System.out.println("=============");
        
        System.exit(-1);
    }
    
    
    public static void importFile(String path){
        
        try {
            List<String> fileContent  = DataManager.readFile(path);
            List<JSONObject> jsonList = JSonParser.parseToJSON(fileContent);
            Connection con = getServerConnection();
            System.out.println(">> OPEN SERVER CONNECTION");
            
            // delete existing Table
            if(deleteTable(con,tableName)){
                System.out.println(">> EXISTING TABLE FOUND");
                System.out.println(">> TABLE DELETE");
            }else{
                System.out.println(">> EXISTING TABLE NOT FOUND");
            }
            
            // create new Table
            if(!createSchemaTables(con,tableName, family)){
                System.out.println(">> Import process FAIL");
                return;
            }
            
            int    i           = 1;
            long   startTime   = System.currentTimeMillis();
            double maxI        = jsonList.size();
            int    lastPercent = -1;
            double curPercent  = 0;
            
            for(JSONObject jso : jsonList){
                
                curPercent  = ((i/maxI)*100.0);
                
                String city = jso.getString("city");
                String plz = jso.getString("_id");
                
                // save plz  -> city
                writeAValue(con, tableName, plz, family, c_column, city);
                
                // save city -> plz
                if(existsValue(con, tableName, city)){
                    int count = getPLZ_Count(con, city);
                    writeAValue(con, tableName, city, family, plz_count, Integer.toString(count+1));
                    writeAValue(con, tableName, city, family, plz_n+(count+1), plz);
                }else{
                    writeAValue(con, tableName, city, family, plz_count, "1");
                    writeAValue(con, tableName, city, family, plz_n+"1", plz);
                }
                
                if(Math.floor(curPercent) != lastPercent){
                    lastPercent = (int) Math.floor(curPercent);
                    System.out.println(">> UPLOAD "+lastPercent+"%");
                }
//                System.out.print(".");
                i++;
            }
            double uploadTime = (System.currentTimeMillis() - startTime)/1000.0;
            System.out.println(">> import successfull in "+uploadTime+" sec.");
            
            // CLOSE CONNECTION
            con.close();
            System.out.println(">> CLOSE SERVER CONNECTION");
            
        } catch (IOException | JSONException ex) {
            System.out.println(">> CANNOT CONNECT TO SERVER");
        }
    }

    public static void update(){
        Scanner s = new Scanner(System.in);
        String[] citys;
        String c_name;
        String value;
        
        System.out.println(">> Citys? | split with ','");
        citys = (s.nextLine()).split(",");
        System.out.println(">> new ColumnName?");
        c_name = s.nextLine();
        System.out.println(">> Value?");
        value  = s.nextLine();
        System.out.println(">> OK, please wait ... ");
        
        try{
            Connection con = getServerConnection();
            System.out.println(">> OPEN CONNECTION TO SERVER");
            
            for(String city : citys){
                int count = getPLZ_Count(con, city);
                System.out.println(">> Search for '"+city+"'");
                System.out.println(">> Found "+count+" entry(s)");
                System.out.print(">> UPDATE [");
                for(int i = 1; i <= count; i++){
                    String plz = readAValue(con, tableName, city, family, plz_n+i);
                    writeAValue(con, tableName, plz, f_family, c_name, value);
                    System.out.print("=");
                }
                System.out.println("]");
                System.out.println(count+" entry(s) updated\n");
            }
            
            // CLOSE CONNECTION
            con.close();
            System.out.println(">> CLOSE CONNECTION TO SERVER");
        }catch(IOException ex){
            System.out.println(">> CANNOT CONNECT TO SERVER");
        }
    }
    
    public static void main(String[] args) throws IOException {
        switch (args.length) {
            case 2:
                String arg1 = args[0];
                String arg2 = args[1];
                
                if((arg1.equals(arg_i) || arg1.equals(arg_imp))){
                    importFile(arg2); // TODO
                }
                break;
            case 1:
                arg1 = args[0];
                if(arg1.equals(arg_c) || arg1.equals(arg_city)) startSearchCity();
                else if(arg1.equals(arg_p) || arg1.equals(arg_plz)) startSearchPLZ();
                else if(arg1.equals(arg_u) || arg1.equals(arg_update)) update();
                else showUsage();
                break;
            default:
                showUsage();
        }
    }
}
