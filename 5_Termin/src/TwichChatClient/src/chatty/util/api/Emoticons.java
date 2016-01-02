
package chatty.util.api;

import chatty.Chatty;
import chatty.Helper;
import chatty.util.settings.Settings;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Add emoticons and get a list of them matching a certain emoteset.
 * 
 * <p>
 * Emotes are sorted into the {@code emoticons} map if they don't have a stream
 * restriction and into the {@code streamEmoticons} map if they have a stream
 * restriction. Emoticons in {@code streamEmoticons} may still have an emoteset
 * or other restrictions that should be checked. This is only done this way to
 * retrieve and iterate over a relatively small subset of all emotes.
 * </p>
 * 
 * <p>
 * This is generally not thread-safe and all methods should be only used from
 * the same thread (like in this case probably the EDT).
 * </p>
 * 
 * @author tduva
 */
public class Emoticons {
    
    private static final Logger LOGGER = Logger.getLogger(Emoticons.class.getName());
    
    private static final Map<String, String> EMOTICONS_MAP = new HashMap<>();
    
    static {
        EMOTICONS_MAP.put("B-?\\)", "B)");
        EMOTICONS_MAP.put("R-?\\)", "R)");
        EMOTICONS_MAP.put("\\:-?D", ":D");
        EMOTICONS_MAP.put("\\;-?\\)", ";)");
        EMOTICONS_MAP.put("\\:-?(o|O)", ":O");
        EMOTICONS_MAP.put("\\:-?\\)", ":)");
        EMOTICONS_MAP.put("\\;-?(p|P)", ";P");
        EMOTICONS_MAP.put("[o|O](_|\\.)[o|O]", "o_O");
        EMOTICONS_MAP.put(">\\(", ">(");
        EMOTICONS_MAP.put("\\:-?(?:\\/|\\\\)(?!\\/)", ":/");
        EMOTICONS_MAP.put("\\:-?\\(", ":(");
        EMOTICONS_MAP.put("\\:-?(p|P)", ":p");
        EMOTICONS_MAP.put("\\:-?[z|Z|\\|]", ":|");
        EMOTICONS_MAP.put(":-?(?:7|L)", ":7");
        EMOTICONS_MAP.put("\\:>", ":>");
        EMOTICONS_MAP.put("\\:-?(S|s)", ":S");
        EMOTICONS_MAP.put("#-?[\\\\/]", "#/");
        EMOTICONS_MAP.put("<\\]", "<]");
        EMOTICONS_MAP.put("\\:-?[\\\\/]", ":/");
    }
    
    /**
     * Emoticons associated with an emoteset (Twitch Emotes and others).
     */
    private final HashMap<Integer,HashSet<Emoticon>> emoticonsByEmoteset = new HashMap<>();
    
    private final Set<Emoticon> customEmotes = new HashSet<>();
    
    private final Map<Integer, Emoticon> customEmotesById = new HashMap<>();
    
    /**
     * Only global Twitch emotes.
     */
    private final Set<Emoticon> globalTwitchEmotes = new HashSet<>();
    
    /**
     * Only other (FFZ/BTTV) global emotes.
     */
    private final Set<Emoticon> otherGlobalEmotes = new HashSet<>();
    
    /**
     * Twitch emotes associated to their Twitch id.
     */
    private final HashMap<Integer,Emoticon> twitchEmotesById = new HashMap<>();
    
    /**
     * Emoticons associated with a channel (FrankerFaceZ/BTTV).
     */
    private final HashMap<String,HashSet<Emoticon>> streamEmoticons = new HashMap<>();
    
    /**
     * Emoteset -> Stream association (from Twitchemotes.com).
     */
    private final Map<Integer, String> emotesetStreams = Collections.synchronizedMap(new HashMap<Integer, String>());
    
    private static final HashSet<Emoticon> EMPTY_SET = new HashSet<>();
    
    private static final Set<String> EMPTY_STRING_SET = new HashSet<>();
    
    private final Set<String> ignoredEmotes = new HashSet<>();
    
    private final Map<String, Favorite> favoritesNotFound = new HashMap<>();
    
    private final HashMap<Favorite, Emoticon> favorites = new HashMap<>();
    
    private boolean loadedFavoritesFromSettings;
    
    private final Set<String> emoteNames = new HashSet<>();
    
    private final Map<String, Set<String>> emotesNamesPerStream = new HashMap<>();
    
    private Set<Integer> localEmotesets = new HashSet<>();
    
    public void updateEmoticons(EmoticonUpdate update) {
        removeEmoticons(update);
        if (!update.emotes.isEmpty()) {
            addEmoticons(update.emotes);
        }
    }
    
    private void removeEmoticons(EmoticonUpdate update) {
        // If used for other types as well, may have to handle favorites
        if (update.typeToRemove == null) {
            return;
        }
        int removed = 0;
        if (update.typeToRemove == Emoticon.Type.FFZ) {
            Iterator<Emoticon> it = otherGlobalEmotes.iterator();
            while (it.hasNext()) {
                Emoticon emote = it.next();
                if (emote.type == update.typeToRemove) {
                    if (update.subTypeToRemove == null
                            || emote.subType == update.subTypeToRemove) {
                        it.remove();
                        removed++;
                    }
                }
            }
        }
        if (removed >= 0) {
            LOGGER.info(String.format("Removed %d emotes (%s/%s)", removed,
                    update.typeToRemove, update.subTypeToRemove));
        }
    }
    
    /**
     * Adds the given emoticons and sorts them into different maps, depending
     * on their restrictions.
     * 
     * <ul>
     * <li>If they have a stream restriction, they will be put in the
     * {@code streamEmoticons} map, with the stream name as the key. If there
     * is more than one stream in the restriction, it will be added under
     * several keys.</li>
     * <li>If there is no stream restriction, then they will be put in the
     * {@code emoticons} map, with the emoteset as the key. If no emoteset is
     * defined, then {@code null} will be used as key.</li>
     * </ul>
     * 
     * <p>
     * This is not thread-safe, so it should only be called from the EDT.
     * </p>
     * 
     * @param newEmoticons 
     */
    public void addEmoticons(Set<Emoticon> newEmoticons) {
        for (Emoticon emote : newEmoticons) {
            Set<String> channelRestrictions = emote.getStreamRestrictions();
            if (channelRestrictions != null) {
                // With channel restriction
                for (String channel : channelRestrictions) {
                    // Create channel set if necessary
                    if (!streamEmoticons.containsKey(channel)) {
                        streamEmoticons.put(channel, new HashSet<Emoticon>());
                    }
                    addEmote(streamEmoticons.get(channel), emote);
                }
            } else {
                if (emote.hasGlobalEmoteset()) {
                    // Global emotes
                    if (emote.type == Emoticon.Type.TWITCH) {
                        addEmote(globalTwitchEmotes, emote);
                    } else {
                        addEmote(otherGlobalEmotes, emote);
                    }
                } else {
                    // Emoteset based
                    Integer emoteset = emote.emoteSet > -1 ? emote.emoteSet : null;
                    if (!emoticonsByEmoteset.containsKey(emoteset)) {
                        emoticonsByEmoteset.put(emoteset, new HashSet<Emoticon>());
                    }
                    addEmote(emoticonsByEmoteset.get(emoteset), emote);
                }
            }
            // By Twitch Emote ID
            if (emote.type == Emoticon.Type.TWITCH && emote.numericId != Emoticon.ID_UNDEFINED) {
                twitchEmotesById.put(emote.numericId, emote);
            }
        }
        LOGGER.info("Added "+newEmoticons.size()+" emotes."
                + " Now "+emoticonsByEmoteset.size()+" emotesets and "
                +streamEmoticons.size()+" channels with exclusive emotes ("
                +getGlobalTwitchEmotes().size()+" global emotes).");
        findFavorites();
    }
    
    /**
     * Helper method to add an emote to a Collection.
     * 
     * @param collection
     * @param emote 
     */
    private void addEmote(Collection<Emoticon> collection, Emoticon emote) {
        /**
         * Add emote codes for TAB Completion. Only add emotes the local user
         * has access to.
         */
        if ((emote.hasGlobalEmoteset() || localEmotesets.contains(emote.emoteSet))) {
            if (!emote.hasStreamRestrictions()) {
                emoteNames.add(emote.code);
            } else {
                // Channel specific emotes
                for (String stream : emote.getStreamRestrictions()) {
                    if (!emotesNamesPerStream.containsKey(stream)) {
                        emotesNamesPerStream.put(stream, new HashSet<String>());
                    }
                    emotesNamesPerStream.get(stream).add(emote.code);
                }
            }
        }
        collection.remove(emote);
        collection.add(emote);
    }
    
    /**
     * Add emote received by message tags but not actually added from JSON.
     * 
     * @param emote 
     */
    public void addTempEmoticon(Emoticon emote) {
        twitchEmotesById.put(emote.numericId, emote);
    }

    public Set<Emoticon> getCustomEmotes() {
        return customEmotes;
    }
    
    public Emoticon getCustomEmoteById(int id) {
        return customEmotesById.get(id);
    }
    
    /**
     * Gets a list of all emoticons that don't have an emoteset associated
     * with them. This returns the original Set, so it should not be modified.
     * 
     * @return 
     */
    public Set<Emoticon> getGlobalTwitchEmotes() {
        return globalTwitchEmotes;
    }
    
    public Set<Emoticon> getOtherGlobalEmotes() {
        return otherGlobalEmotes;
    }
    
    public HashMap<Integer, Emoticon> getEmoticonsById() {
        return twitchEmotesById;
    }
    
    /**
     * Gets a list of emoticons that are associated with the given emoteset.
     * This returns the original Set, so it should not be modified.
     *
     * @param emoteSet
     * @return
     */
    public HashSet<Emoticon> getEmoticons(int emoteSet) {
        HashSet<Emoticon> result = emoticonsByEmoteset.get(emoteSet);
        if (result == null) {
            result = EMPTY_SET;
        }
        return result;
    }
    
    /**
     * Gets a list of emoticons that are associated with the given channel. This
     * returns the original Set, so it should not be modified.
     *
     * @param stream The name of the channel
     * @return
     */
    public HashSet<Emoticon> getEmoticons(String stream) {
        HashSet<Emoticon> result = streamEmoticons.get(stream);
        if (result == null) {
            result = EMPTY_SET;
        }
        return result;
    }
    
    public Collection<String> getEmoteNames() {
        return emoteNames;
    }
    
    public Collection<String> getEmotesNamesByStream(String stream) {
        Collection<String> names = emotesNamesPerStream.get(stream);
        return names == null ? EMPTY_STRING_SET : names;
    }
    
    public void updateEmoteNames(Set<Integer> emotesets) {
        if (!this.localEmotesets.equals(emotesets)) {
            this.localEmotesets = emotesets;
            for (int emoteset : emotesets) {
                for (Emoticon emote : getEmoticons(emoteset)) {
                    emoteNames.add(emote.code);
                }
            }
        }
    }
    
    /**
     * Checks whether the given emoteset is a turbo emoteset. This may be
     * incomplete.
     * 
     * @param emoteSet The emoteset to check
     * @return true when it is a turbo emoteset, false otherwise
     */
    public static boolean isTurboEmoteset(int emoteSet) {
        if (emoteSet == 33 || emoteSet == 42
                    || emoteSet == 457 || emoteSet == 793) {
            return true;
        }
        return false;
    }
    
    /**
     * Adds the emoteset data that associates them with a stream name.
     * 
     * @param data 
     */
    public void addEmotesetStreams(Map<Integer, String> data) {
        emotesetStreams.putAll(data);
    }
    
    /**
     * Gets the name of the stream the given emoteset is associated with. This
     * of course only works if the emoteset data was actually successfully
     * requested before calling this.
     * 
     * @param emoteset The emoteset
     * @return The name of the stream, or null if none could be found for this
     * emoteset
     */
    public String getStreamFromEmoteset(int emoteset) {
        String stream = emotesetStreams.get(emoteset);
        if ("--twitch-turbo--".equals(stream) || "turbo".equals(stream)) {
            return "Turbo Emotes";
        }
        return emotesetStreams.get(emoteset);
    }
    
    /**
     * Gets the emoteset from the given stream name. This of course only works
     * if the emoteset data was actually successfully requested before calling
     * this.
     * 
     * @param stream The name of the stream to get the emoteset for
     * @return The emoteset, or -1 if none could be found
     */
    public int getEmotesetFromStream(String stream) {
        for (int emoteset : emotesetStreams.keySet()) {
            if (emotesetStreams.get(emoteset).equals(stream)) {
                return emoteset;
            }
        }
        return -1;
    }
    
    /**
     * Replaces the ignored emotes list with the given data.
     * 
     * @param ignoredEmotes A Collection of emote codes to ignore
     */
    public void setIgnoredEmotes(Collection<String> ignoredEmotes) {
        this.ignoredEmotes.clear();
        this.ignoredEmotes.addAll(ignoredEmotes);
    }
    
    /**
     * Adds the given emote to the list of ignored emotes, by adding it's code.
     * 
     * @param emote The emote to ignore
     */
    public void addIgnoredEmote(Emoticon emote) {
        addIgnoredEmote(emote.code);
    }
    
    /**
     * Adds the given emote code to the list of ignored emotes.
     * 
     * @param emoteCode The emote code to add
     */
    public void addIgnoredEmote(String emoteCode) {
        ignoredEmotes.add(emoteCode);
    }
    
    /**
     * Check if the given emote is on the list of ignored emotes. Compares the
     * emote code to the codes on the list.
     * 
     * @param emote The Emoticon to check
     * @return true if the emote is ignored, false otherwise
     */
    public boolean isEmoteIgnored(Emoticon emote) {
        return ignoredEmotes.contains(emote.code);
    }
    
    /**
     * Displays some information about the emotes that match the given emote
     * code (usually just one, but might be several if emotes with the same code
     * exist for different emotesets or channels).
     * 
     * @param emoteCode A single emote code to find the emote for
     * @return A String with a description of the found emote(s)
     */
    public String getEmoteInfo(String emoteCode) {
        if (emoteCode == null) {
            return "No emote specified.";
        }
        Set<Emoticon> found = findMatchingEmoticons(emoteCode);
        if (found.isEmpty()) {
            return "No matching emote found.";
        }
        StringBuilder b = new StringBuilder();
        b.append("Found ").append(found.size());
        if (found.size() == 1) {
            b.append(" emote");
        } else {
            b.append(" emotes");
        }
        b.append(" for '").append(emoteCode).append("': ");
        String sep = "";
        for (Emoticon emote : found) {
            Set<String> streams = emote.getStreamRestrictions();
            b.append(sep+"'"+emote.code+"'"
                +" / Type: "+emote.type+" / "
                +(emote.hasGlobalEmoteset()
                    ? "Usable by everyone"
                    : ("Emoteset: "+emote.emoteSet
                      +" ("+getStreamFromEmoteset(emote.emoteSet)+")"))
                
                +(streams == null
                    ? " / Usable in all channels"
                    : " / Only usable in: "+streams));
            sep = " ### ";
        }
        
        return b.toString();
    }
    
    /**
     * Finds all emotes matching the given emote code.
     * 
     * @param emoteCode
     * @return 
     */
    public Set<Emoticon> findMatchingEmoticons(String emoteCode) {
        Set<Emoticon> found = new HashSet<>();
        found.addAll(findMatchingEmoticons(emoteCode, emoticonsByEmoteset.values()));
        found.addAll(findMatchingEmoticons(emoteCode, streamEmoticons.values()));
        return found;
    }
    
    /**
     * Finds all emotes matching the given emote code within the given data.
     * 
     * @param emoteCode
     * @param values
     * @return 
     */
    public Set<Emoticon> findMatchingEmoticons(String emoteCode,
            Collection<HashSet<Emoticon>> values) {
        Set<Emoticon> found = new HashSet<>();
        for (Collection<Emoticon> emotes : values) {
            for (Emoticon emote : emotes) {
                if (emote.getMatcher(emoteCode).matches()) {
                    found.add(emote);
                }
            }
        }
        return found;
    }
    
    /**
     * Creates a new Set that only contains the subset of the given emotes that
     * are of the given type (e.g. Twitch, FFZ or BTTV).
     * 
     * @param emotes The emotes to filter
     * @param type The emote type to allow through the filter
     * @return A new Set containing the emotes of the given type
     */
    public static final Set<Emoticon> filterByType(Set<Emoticon> emotes,
            Emoticon.Type type) {
        Set<Emoticon> filtered = new HashSet<>();
        for (Emoticon emote : emotes) {
            if (emote.type == type) {
                filtered.add(emote);
            }
        }
        return filtered;
    }
    
    public static final String toWriteable(String emoteCode) {
        String writeable = EMOTICONS_MAP.get(emoteCode);
        if (writeable == null) {
            return emoteCode;
        }
        return writeable;
    }
    
    /**
     * Adds the given Emoticon to the favorites.
     * 
     * @param emote The Emoticon to add
     */
    public void addFavorite(Emoticon emote) {
        favorites.put(createFavorite(emote), emote);
    }
    
    /**
     * Creates a Favorite object for the given Emoticon.
     * 
     * @param emote The Emoticon to create the Favorite object for
     * @return The created Favorite object
     */
    private Favorite createFavorite(Emoticon emote) {
        return new Favorite(emote.code, emote.emoteSet, 0);
    }
    
    /**
     * Loads the favorites from the settings.
     * 
     * @param settings The Settings object
     */
    public void loadFavoritesFromSettings(Settings settings) {
        List<List> entriesToLoad = settings.getList("favoriteEmotes");
        favoritesNotFound.clear();
        favorites.clear();
        for (List item : entriesToLoad) {
            Favorite f = listToFavorite(item);
            if (f != null) {
                favoritesNotFound.put(f.code, f);
            }
        }
        findFavorites();
        loadedFavoritesFromSettings = true;
    }

    /**
     * Saves the favorites to the settings, discarding any favorites that
     * haven't been found several times already.
     * 
     * @param settings The Settings object
     */
    public void saveFavoritesToSettings(Settings settings) {
        if (!loadedFavoritesFromSettings) {
            LOGGER.warning("Not saving favorite emotes, because they don't seem to have been loaded in the first place.");
            return;
        }
        List<List> entriesToSave = new ArrayList<>();
        for (Favorite f : favorites.keySet()) {
            entriesToSave.add(favoriteToList(f, true));
        }
        for (Favorite f : favoritesNotFound.values()) {
            if (f.notFoundCount > 30) {
                LOGGER.warning("Not saving favorite emote "+f+" (not found)");
            } else {
                entriesToSave.add(favoriteToList(f, false));
            }
        }
        settings.putList("favoriteEmotes", entriesToSave);
    }
    
    /**
     * Turns the given list into a single Favorite object. This is used to load
     * the favorites from the settings. The expected format is as detailed in
     * {@see favoriteToList(Favorite, boolean)}.
     * 
     * @param item The List to turn into a Favorite object
     * @return The created Favorite, or null if an error occured
     * @see favoriteToList(Favorite, boolean)
     */
    private Favorite listToFavorite(List item) {
        try {
            String code = (String) item.get(0);
            int emoteset = ((Number) item.get(1)).intValue();
            int notFoundCount = ((Number) item.get(2)).intValue();
            return new Favorite(code, emoteset, notFoundCount);
        } catch (ClassCastException | ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }
    
    /**
     * Turns the given favorite into a list, so it can be saved to the settings.
     * The format is (arrayindex: value (Type)):
     * 
     * <ul>
     * <li>0: code (String),</li>
     * <li>1: emoteset (Number),</li>
     * <li>2: notFoundCount (Number)</li>
     * </ul>
     * 
     * The notFoundCount is increased if the emote was not found during this
     * session, otherwise it is set to 0.
     * 
     * @param f The favorite to turn into a list
     * @param found Whether this favorite was found during this session
     * @return The created list
     * @see listToFavorite(List)
     */
    private List favoriteToList(Favorite f, boolean found) {
        List list = new ArrayList();
        list.add(f.code);
        list.add(f.emoteset);
        if (found) {
            list.add(0);
        } else {
            list.add(f.notFoundCount+1);
        }
        return list;
    }
    
    /**
     * If there are still Favorites not yet associated with an actual Emoticon
     * object, then search through the current emoticons. This should be done
     * everytime new emotes are added (e.g. from request or loaded from cache).
     */
    private void findFavorites() {
        if (favoritesNotFound.isEmpty()) {
            return;
        }
        int count = favoritesNotFound.size();
        findFavorites(twitchEmotesById.values());
        findFavorites(otherGlobalEmotes);
        if (favoritesNotFound.isEmpty()) {
            LOGGER.info("Emoticons: Found all remaining " + count + " favorites");
        } else {
            LOGGER.info("Emoticons: "+favoritesNotFound.size()+" favorites still not found");
        }
    }
    
    private boolean findFavorites(Collection<Emoticon> emotes) {
        for (Emoticon emote : emotes) {
            checkFavorite(emote);
            if (favoritesNotFound.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    private void checkFavorite(Emoticon emote) {
        Favorite f = favoritesNotFound.get(emote.code);
        if (f != null && f.emoteset == emote.emoteSet) {
            favorites.put(f, emote);
            favoritesNotFound.remove(emote.code);
        }
    }
    
    /**
     * Removes the given Emoticon from the favorites.
     * 
     * @param emote 
     */
    public void removeFavorite(Emoticon emote) {
        favorites.remove(createFavorite(emote));
        favoritesNotFound.remove(emote.code);
    }
    
    /**
     * Returns a copy of the favorites.
     * 
     * @return 
     */
    public Set<Emoticon> getFavorites() {
        return new HashSet<>(favorites.values());
    }
    
    /**
     * Gets the number of favorites that couldn't be found.
     * 
     * @return 
     */
    public int getNumNotFoundFavorites() {
        return favoritesNotFound.size();
    }
    
    /**
     * Checks whether the given Emoticon is a favorite.
     * 
     * @param emote
     * @return 
     */
    public boolean isFavorite(Emoticon emote) {
        return favoritesNotFound.containsKey(emote.code) || favorites.containsValue(emote);
    }
    
    /**
     * A favorite specifying the emote code, the emoteset and how often it
     * hasn't been found. The emote code and emoteset are required to find the
     * actual Emoticon object that corresponds to it.
     */
    private static class Favorite {
        
        public final String code;
        public final int emoteset;
        public final int notFoundCount;
        
        Favorite(String code, int emoteset, int notFoundCount) {
            this.code = code;
            this.emoteset = emoteset;
            this.notFoundCount = notFoundCount;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 19 * hash + Objects.hashCode(this.code);
            hash = 19 * hash + this.emoteset;
            return hash;
        }

        /**
         * A Favorite is considered equal when both the emote code and emoteset
         * are equal.
         * 
         * @param obj
         * @return 
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Favorite other = (Favorite) obj;
            if (!Objects.equals(this.code, other.code)) {
                return false;
            }
            if (this.emoteset != other.emoteset) {
                return false;
            }
            return true;
        }
        
        @Override
        public String toString() {
            return code+"["+emoteset+"]";
        }
        
    }
    
    /**
     * Loads custom emotes from the emotes.txt file in the settings directory.
     * 
     * Each line can have one emote. See {@link loadCustomEmote(String)} for the
     * parsing of each line.
     */
    public void loadCustomEmotes() {
        customEmotes.clear();
        customEmotesById.clear();
        
        Path file = Paths.get(Chatty.getUserDataDirectory()+"emotes.txt");
        try (BufferedReader r = Files.newBufferedReader(file, Charset.forName("UTF-8"))) {
            int countLoaded = 0;
            String line;
            while ((line = r.readLine()) != null) {
                if (loadCustomEmote(line)) {
                    countLoaded++;
                }
            }
            LOGGER.info("Loaded "+countLoaded+" custom emotes");
        } catch (IOException ex) {
            LOGGER.info("Didn't load custom emotes: "+ex);
        }
    }
    
    /**
     * Parse a single line of custom emotes. Allows a number of options that are
     * in the form "option-key:option-value" which basicially can be in any
     * place in the line as long as they are seperated by whitespace. The first
     * and second part without recognized option is used as code and url
     * respectively. Unknown parts (non-options) after the code/url have been
     * found are ignored.
     * 
     * Lines starting with "#" are ignored.
     *
     * @param line The line, cannot be null
     * @return true if an emote was found and added, false otherwise
     */
    private boolean loadCustomEmote(String line) {

        // Comment
        if (line.startsWith("#")) {
            return false;
        }
        
        String code = null;
        boolean literal = true;
        String url = null;
        int emoteset = Emoticon.SET_UNDEFINED;
        int id = Emoticon.ID_UNDEFINED;
        // Use Dimension because it's easier to check if one value is set
        Dimension size = null;
        String streamRestriction = null;
        
        String[] split = line.trim().split("\\s+");
        for (int i=0;i<split.length;i++) {
            String item = split[i];
            
            if (item.startsWith("re:") && item.length() > "re:".length()) {
                literal = false;
                code = item.substring("re:".length());
            } else if (item.startsWith("id:")) {
                try {
                    id = Integer.parseInt(item.substring("id:".length()));
                } catch (NumberFormatException ex) {
                    // Just don't set the id
                }
            } else if (item.startsWith("set:")) {
                try {
                    emoteset = Integer.parseInt(item.substring("set:".length()));
                } catch (NumberFormatException ex) {
                    // Just don't set the emoteset
                }
            } else if (item.startsWith("chan:") && item.length() > "chan:".length()) {
                streamRestriction = Helper.toStream(item.substring("chan:".length()));
            } else if (item.startsWith("size:")) {
                // All other data is found, so this must be the size
                try {
                    String[] sizes = item.substring("size:".length()).split("x");
                    int width = Integer.parseInt(sizes[0]);
                    int height = Integer.parseInt(sizes[1]);
                    // Set here so it's only set if both values are correct
                    size = new Dimension(width, height);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    // Do nothing if the format isn't right
                }
            } else {
                // No prefixes anymore, so check which one was set already
                // The order of this is important
                if (code == null) {
                    code = item;
                } else if (url == null) {
                    url = item;
                    if (!item.startsWith("http")) {
                        try {
                            Path path = Paths.get(Chatty.getUserDataDirectory()).resolve(Paths.get(url));
                            url = path.toUri().toURL().toString();
                        } catch (MalformedURLException ex) {
                            url = null;
                        }
                    }
                } else {
                    // Just ignore other stuff
                }
            }
        }
        
        // With code and URL found we can add the emote, other stuff is optional
        if (code != null && url != null) {
            Emoticon.Builder b = new Emoticon.Builder(Emoticon.Type.CUSTOM, code, url);
            b.setLiteral(literal).setEmoteset(emoteset);
            b.setNumericId(id);
            if (size != null) {
                b.setSize(size.width, size.height);
            }
            b.addStreamRestriction(streamRestriction);
            Emoticon emote = b.build();
            customEmotes.add(emote);
            if (id != Emoticon.ID_UNDEFINED) {
                customEmotesById.put(id, emote);
            }
            return true;
        }
        return false;
    }
    
    /**
     * Makes a String containing info about the currently loaded custom emotes
     * over several lines.
     * 
     * @return The info text
     */
    public String getCustomEmotesInfo() {
        if (customEmotes.size() == 0) {
            return "No custom emotes loaded";
        }
        StringBuilder b = new StringBuilder(customEmotes.size()+" custom emotes loaded:\n");
        for (Emoticon emote : customEmotes) {
            List<String> info = new ArrayList<>();
            if (emote.hasStreamRestrictions()) {
                info.add("#"+emote.getStreamRestrictions().iterator().next());
            }
            if (emote.emoteSet != Emoticon.SET_UNDEFINED) {
                info.add("set:"+emote.emoteSet);
            }
            if (emote.numericId != Emoticon.ID_UNDEFINED) {
                info.add("id:"+emote.numericId);
            }
            b.append("\""+emote.code+"\" ");
            if (info.size() > 0) {
                b.append(info);
            }
            b.append("\n   ");
            b.append(emote.url);
            b.append("\n");
        }
        b.delete(b.length()-1, b.length());
        return b.toString();
    }
    
    /**
     * Parses the Twitch emotes tag into an easier usable format.
     * 
     * @param tag The value of the emotes tag, can be null if no emotes are
     * supplied
     * @return The TagEmotes object containing the Map of emotes and ranges, or
     * null if the tag value was null (used for local messages for example,
     * indicating twitch emotes have to be parsed using regex)
     */
    public static TagEmotes parseEmotesTag(String tag) {
        Map<Integer, TagEmote> result = new HashMap<>();
        if (tag == null) {
            return null;
        }
        String[] emotes = tag.split("/");
        for (String emote : emotes) {
            // Go through all emotes
            int idEnd = emote.indexOf(":");
            if (idEnd > 0) {
                try {
                    /**
                     * Parse the emote id and ranges. If the format isn't as
                     * expected, this emote or at least any further ranges for
                     * this emote are ignored.
                     */
                    int id = Integer.parseInt(emote.substring(0, idEnd));
                    String[] emoteRanges = emote.substring(idEnd+1).split(",");
                    
                    // Save all ranges for this emote
                    for (String range : emoteRanges) {
                        String[] rangeSplit = range.split("-");
                        int start = Integer.parseInt(rangeSplit[0]);
                        int end = Integer.parseInt(rangeSplit[1]);
                        if (end > start && start >= 0) {
                            // Every start index should only appear once anyway,
                            // so it can be used as key
                            result.put(start, new TagEmote(id, end));
                        }
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    // Simply ignore error and continue with the next emote
                }
            }
        }
        return new TagEmotes(result);
    }
    
    public static void main(String[] args) {
        System.out.println(parseEmotesTag("131:1-2,4-5/43:1-7"));
    }
    
    /**
     * Wrapping class for the Map of emote ids and their ranges.
     */
    public static class TagEmotes {
        public final Map<Integer, TagEmote> emotes;
        
        public TagEmotes(Map<Integer, TagEmote> emotes) {
            this.emotes = emotes;
        }
        
        @Override
        public String toString() {
            return emotes.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TagEmotes other = (TagEmotes) obj;
            if (!Objects.equals(this.emotes, other.emotes)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + Objects.hashCode(this.emotes);
            return hash;
        }
    }
    
    /**
     * Tag Emote with an id and a range end (the start should be the key
     * referring to this object.
     */
    public static class TagEmote {
        
        public final int id;
        public final int end;
        
        public TagEmote(int start, int end) {
            this.id = start;
            this.end = end;
        }
        
        @Override
        public String toString() {
            return ">"+id+":"+end;
        }
    }
            
}
