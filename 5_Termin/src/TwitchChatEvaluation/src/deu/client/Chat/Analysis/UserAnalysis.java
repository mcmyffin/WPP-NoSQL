package deu.client.Chat.Analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dima
 */
public class UserAnalysis {

    public static Typ getUserTyp(Map<Integer,Long> map){
        List<Integer> list = new ArrayList(map.keySet());
        Collections.sort(list);
        list.addAll(list.subList(1, 7));
        
        if(isSchueler(map,list)) return Typ.SCHUELER;
        else if(isArbeiterTyp(map,list)) return Typ.ARBEITER;
        else if(isArbeitslos(map,list)) return Typ.ARBEITSLOS;
        else if(isBotTyp(map)) return Typ.BOT;
        else return Typ.UNBEKANNT;
    }
    
    private static boolean isBotTyp(Map<Integer,Long> map){
        int tolerance = 3;
        List<Long> col = new ArrayList(map.values());
        Collections.sort(col);
        
        for(long val : col) {
            if(tolerance <= 0) return false;
            if(val <= 0) tolerance--;
        }
        return true;
    }

    private static boolean isArbeiterTyp(Map<Integer, Long> map, List<Integer> list) {
        int minWorkTime = 8;
        int minSleepTime= 8;
        
        int countWorkTime = countCommonOfflineTime(list,map, minWorkTime);
        if(countWorkTime > 1) return true;
        if(countWorkTime <= 0) return false;
        
        int countSleepTime = countCommonOfflineTime(list,map, minSleepTime);
        return countSleepTime > 1;
    }

    private static int countCommonOfflineTime(List<Integer> list, Map<Integer,Long> map, int minOfflineTime){
        int results = 0;
        
        int fromIndex = 0;
        int toIndex   = fromIndex+(minOfflineTime);
        while(toIndex <= list.size()){
            
            boolean wrong = false;
            List<Integer> subList = list.subList(fromIndex, toIndex);
            
            for(int i = 1; i <= subList.size(); i++){
                long val = map.get(subList.get(i-1));
                if(val > 0){
                    fromIndex += (i);
                    toIndex    = fromIndex+minOfflineTime;
                    wrong      = true;
                }
            }
            
            if(!wrong){
                results++;
                fromIndex = toIndex;
                toIndex   = fromIndex + minOfflineTime;
            }
        }
        return results;
    }
    
    private static boolean isSchueler(Map<Integer, Long> map, List<Integer> list) {
        int offlineTime = 10+8;
//        int minSleepTime  = 0;
        
        int countSleepTime = countCommonOfflineTime(list,map, offlineTime);
        return countSleepTime >= 1;
//        if(countSleepTime <= 0) return false;
        
//        int countSchoolTime = countCommonOfflineTime(list,map, minSchoolTime);
//        return countSleepTime >= 1;
    }

    private static boolean isArbeitslos(Map<Integer, Long> map, List<Integer> list) {
        int minSleepTime = 8;
        int countSleepTime = countCommonOfflineTime(list,map, minSleepTime);
        return countSleepTime >= 1;
    }
}
