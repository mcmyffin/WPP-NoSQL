package redis.parser;


import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dima
 */
public class JSonParser {

    
    
    public static List<JSONObject> parseToJSON(List<String> list) throws JSONException{
        
        List<JSONObject> jsonList = new ArrayList(list.size());
        for(String elem : list){    
            jsonList.add(new JSONObject(elem));
        }
        return jsonList;
    }
}
