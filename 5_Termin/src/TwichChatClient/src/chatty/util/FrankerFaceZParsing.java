
package chatty.util;

import chatty.Usericon;
import chatty.util.api.Emoticon;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Emotes/Mod Icon parsing functions.
 * 
 * @author tduva
 */
public class FrankerFaceZParsing {
    
    private static final Logger LOGGER = Logger.getLogger(FrankerFaceZParsing.class.getName());
    
    /**
     * Parses the mod icon.
     * 
     * Request: /room/:room
     * 
     * @param json
     * @return 
     */
    public static Usericon parseModIcon(String json) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject o = (JSONObject)parser.parse(json);
            JSONObject room = (JSONObject)o.get("room");
            String roomId = (String)room.get("id");
            String modBadgeUrl = (String)room.get("moderator_badge");
            if (modBadgeUrl == null) {
                return null;
            }
            return Usericon.createTwitchLikeIcon(Usericon.Type.MOD,
                            roomId, modBadgeUrl, Usericon.SOURCE_FFZ);
        } catch (ParseException | ClassCastException | NullPointerException ex) {
            
        }
        return null;
    }
    
    /**
     * Parses the global emotes request.
     * 
     * Request: /set/global
     * 
     * @param json
     * @return 
     */
    public static Set<Emoticon> parseGlobalEmotes(String json) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject o = (JSONObject)parser.parse(json);
            JSONArray defaultSets = (JSONArray)o.get("default_sets");
            JSONObject sets = (JSONObject)o.get("sets");
            for (Object setObject : defaultSets) {
                int set = ((Number)setObject).intValue();
                JSONObject setData = (JSONObject)sets.get(String.valueOf(set));
                return parseEmoteSet(setData, null, null);
            }
        } catch (ParseException | ClassCastException | NullPointerException ex) {
            LOGGER.warning("Error parsing global FFZ emotes: "+ex);
        }
        return new HashSet<>();
    }
    
    /**
     * Parse the result of a request for a single room.
     * 
     * Request: /room/:room
     * 
     * @param json The JSON to parse
     * @return Set of emotes, can be empty if there are no emotes or an error
     * occured
     */
    public static Set<Emoticon> parseRoomEmotes(String json) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject o = (JSONObject)parser.parse(json);
            JSONObject room = (JSONObject)o.get("room");
            String roomId = (String)room.get("id");
            int set = ((Number)room.get("set")).intValue();
            JSONObject sets = (JSONObject)o.get("sets");
            JSONObject setData = (JSONObject)sets.get(String.valueOf(set));
            return parseEmoteSet(setData, roomId, null);
        } catch (ParseException | ClassCastException | NullPointerException ex) {
            LOGGER.warning("Error parsing FFZ emotes: "+ex);
        }
        return new HashSet<>();
    }
    
    /**
     * Parses the emotes of the set request.
     * 
     * Request: /set/:id
     * 
     * @param json The JSON to parse
     * @param subType
     * @return 
     */
    public static Set<Emoticon> parseSetEmotes(String json, Emoticon.SubType subType) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(json);
            JSONObject setData = (JSONObject)root.get("set");
            return parseEmoteSet(setData, null, subType);
        } catch (ParseException | ClassCastException | NullPointerException ex) {
            LOGGER.warning("Error parsing FFZ emotes: "+ex);
        }
        return new HashSet<>();
    }
    
    /**
     * Parses a single emote set. Emote set in this context is a set of emotes
     * that users have access to either globally or in a single room.
     * 
     * @param setData The set JSONObject, containing the list of emotes and meta
     * information
     * @param streamRestriction The stream this emote set should be restricted
     * to or null for no restriction
     * @param subType The subType to be set for the Emoticons
     * @return The set of parsed emotes, can be empty if no emotes were found or
     * an error occured
     */
    public static Set<Emoticon> parseEmoteSet(JSONObject setData,
            String streamRestriction, Emoticon.SubType subType) {
        try {
            JSONArray emoticons = (JSONArray)setData.get("emoticons");
            String title = JSONUtil.getString(setData, "title");
            return parseEmoticons(emoticons, streamRestriction, title, subType);
        } catch (ClassCastException | NullPointerException ex) {
            LOGGER.warning("Error parsing FFZ emote set: "+ex);
        }
        return new HashSet<>();
    }

    /**
     * Parses the list of emotes.
     * 
     * @param emotes The JSONArray containing the emote objects
     * @param streamRestriction The stream these emotes should be restricted to
     * or null for no restriction
     * @param info The info to be set for the Emoticons
     * @param subType The subType to be set for the Emoticons
     * @return 
     */
    public static Set<Emoticon> parseEmoticons(JSONArray emotes,
            String streamRestriction, String info, Emoticon.SubType subType) {
        Set<Emoticon> result = new HashSet<>();
        if (emotes != null) {
            for (Object emote : emotes) {
                if (emote != null && emote instanceof JSONObject) {
                    Emoticon createdEmote = parseEmote((JSONObject)emote, streamRestriction, info, subType);
                    if (createdEmote != null) {
                        result.add(createdEmote);
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Parses a single emote. Required for creating an emote are the emote code,
     * the id and the x1 URL.
     * 
     * @param emote The emote data as a JSONObject
     * @param streamRestriction The stream restriction to use for this emote,
     * can be null if the emote is global
     * @param info The info to set for this emote, can be null if no info should
     * be set
     * @param subType The subType to be set for the Emoticon
     * @return The Emoticon object or null if an error occured
     */
    public static Emoticon parseEmote(JSONObject emote, String streamRestriction,
            String info, Emoticon.SubType subType) {
        try {
            // Base information
            int width = JSONUtil.getInteger(emote, "width", -1);
            int height = JSONUtil.getInteger(emote, "height", -1);
            String code = (String)emote.get("name");
            JSONObject urls = (JSONObject)emote.get("urls");
            String url1 = (String)urls.get("1");
            String url2 = (String)urls.get("2");
            int id = ((Number)emote.get("id")).intValue();
            
            // Creator
            Object owner = emote.get("owner");
            String creator = null;
            if (owner != null && owner instanceof JSONObject) {
                creator = (String)((JSONObject)owner).get("display_name");
            }
            
            // Check if required data is there
            if (code == null || code.isEmpty()) {
                return null;
            }
            if (url1 == null || url1.isEmpty()) {
                return null;
            }
            
            Emoticon.Builder b = new Emoticon.Builder(Emoticon.Type.FFZ, code, url1);
            b.setX2Url(url2);
            b.setSize(width, height);
            b.setCreator(creator);
            b.setNumericId(id);
            b.addStreamRestriction(streamRestriction);
            b.setInfo(info);
            b.setSubType(subType);
            return b.build();
        } catch (ClassCastException | NullPointerException ex) {
            LOGGER.warning("Error parsing FFZ emote: "+ex);
            return null;
        }
    }
    
}
