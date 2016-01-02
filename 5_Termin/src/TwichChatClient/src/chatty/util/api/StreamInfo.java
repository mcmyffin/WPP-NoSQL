
package chatty.util.api;

import chatty.Helper;
import chatty.util.DateTime;
import chatty.util.StringUtil;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Holds the current info (name, viewers, title, game) of a stream, as well
 * as a history of the same information and stuff like when the info was
 * last requested, whether it's currently waiting for an API response etc.
 * 
 * Holds two kinds of information: The primary info (title, game, viewers) and
 * the secondary information (display name, notFound) which is sort of a
 * by-product of requesting the primary info.
 *
 * @author tduva
 */
public class StreamInfo {
    
    private static final Logger LOGGER = Logger.getLogger(StreamInfo.class.getName());
    
    private enum UpdateResult { UPDATED, CHANGED, SET_OFFLINE };
    
    /**
     * All lowercase name of the stream
     */
    public final String stream;
    
    /**
     * Correctly capitalized name of the stream. May be null if no set.
     */
    private String display_name;
    
    private long lastUpdated = 0;
    private long lastStatusChange = 0;
    private String status = null;
    private String game = "";
    private int viewers = 0;
    private long startedAt = -1;
    private long lastOnline = -1;
    private long startedAtWithPicnic = -1;
    private boolean online = false;
    private boolean updateSucceeded = false;
    private int updateFailedCounter = 0;
    private boolean requested = false;
    private boolean followed = false;
    private volatile boolean notFound = false;
    
    /**
     * The time the stream was changed from online -> offline, so recheck if
     * that actually is correct after some time. If this is -1, then do nothing.
     * Should be set to -1 with EVERY update (received data), except when it's
     * not already -1 on the change from online -> offline (to avoid request
     * spam if recheckOffline() is always true).
     */
    private long recheckOffline = -1;
    
    // When the viewer stats where last calculated
    private long lastViewerStats;
    // How long at least between viewer stats calculations
    private static final int VIEWERSTATS_DELAY = 30*60*1000;
    // How long a stats range can be at most
    private static final int VIEWERSTATS_MAX_LENGTH = 35*60*1000;
    
    private static final int RECHECK_OFFLINE_DELAY = 10*1000;
    
    /**
     * Maximum length in seconds of what should count as a PICNIC (short stream
     * offline period), to set the online time with PICNICs correctly.
     */
    private static final int MAX_PICNIC_LENGTH = 600;
    
    /**
     * The current full status (title + game), updated when new data is set.
     */
    private String currentFullStatus;
    private String prevFullStatus;
    
    private final LinkedHashMap<Long,StreamInfoHistoryItem> history = new LinkedHashMap<>();
    
    private int expiresAfter = 300;
    
    private final StreamInfoListener listener;

    public StreamInfo(String stream, StreamInfoListener listener) {
        this.listener = listener;
        this.stream = stream.toLowerCase(Locale.ENGLISH);
    }
    
    private void streamInfoUpdated(UpdateResult r) {
        if (r == UpdateResult.SET_OFFLINE) {
            setOffline();
        }
        if (r == UpdateResult.CHANGED) {
            streamInfoStatusChanged();
        }
        streamInfoUpdated();
    }
    
    private void streamInfoUpdated() {
        if (listener != null) {
            listener.streamInfoUpdated(this);
        }
    }
    
    private void streamInfoStatusChanged() {
        lastStatusChange = System.currentTimeMillis();
        if (listener != null) {
            listener.streamInfoStatusChanged(this, getFullStatus());
        }
    }
    
    public synchronized void setRequested() {
        this.requested = true;
    }
    
    public synchronized boolean isRequested() {
        return requested;
    }

    /**
     * Set stream info from followed streams request.
     * 
     * This calls methods intended to be overwritten (listeners), which should
     * be taken into account when synchronizing externally. This should be
     * thread-safe though (hopefully).
     *
     * @param status The current stream title
     * @param game The current game being played
     * @param viewers The current viewercount
     * @param startedAt The timestamp when the stream was started, -1 if not set
     */
    public void setFollowed(String status, String game, int viewers, long startedAt) {
        //System.out.println(status);
        followed = true;
        boolean saveToHistory = false;
        if (hasExpired()) {
            saveToHistory = true;
        }
        set(status, game, viewers, startedAt, saveToHistory);
    }
    
    /**
     * Set stream info from a regular stream info request.
     * 
     * This calls methods intended to be overwritten (listeners), which should
     * be taken into account when synchronizing externally. This should be
     * thread-safe though (hopefully).
     *
     * @param status The current stream title
     * @param game The current game being played
     * @param viewers The current viewercount
     * @param startedAt The timestamp when the stream was started, -1 if not set
     */
    public void set(String status, String game, int viewers, long startedAt) {
        set(status, game, viewers, startedAt, true);
    }
    
    /**
     * This should only be used when the update was successful.
     * 
     * @param status The current title of the stream
     * @param game The current game
     * @param viewers The current viewercount
     * @param startedAt The timestamp when the stream was started, -1 if not set
     * @param saveToHistory Whether to save the data to history
     */
    private void set(String status, String game, int viewers, long startedAt, boolean saveToHistory) {
        UpdateResult result;
        synchronized(this) {
            this.status = StringUtil.trim(StringUtil.removeLinebreakCharacters(status));
            this.game = StringUtil.trim(StringUtil.nullToString(game));
            this.viewers = viewers;

            /**
             * Always set to -1 (do nothing) when stream is set as online, but
             * also output a message if necessary.
             */
            if (recheckOffline != -1) {
                if (this.startedAt < startedAt) {
                    LOGGER.info("StreamInfo " + stream + ": Stream not offline anymore");
                } else {
                    LOGGER.info("StreamInfo " + stream + ": Stream not offline");
                }
            }
            recheckOffline = -1;

            if (lastOnlineAgo() > MAX_PICNIC_LENGTH) {
                /**
                 * Only update online time with PICNICs when offline time was
                 * long enough (of course also depends on what stream data
                 * Chatty has).
                 */
                this.startedAtWithPicnic = startedAt;
            }
            this.startedAt = startedAt;
            this.lastOnline = System.currentTimeMillis();
            this.online = true;

            if (saveToHistory) {
                addHistoryItem(System.currentTimeMillis(), new StreamInfoHistoryItem(viewers, status, game));
            }
            result = setUpdateSucceeded(true);
        }
        streamInfoUpdated(result);
    }
    
    /**
     *
     * This calls methods intended to be overwritten (listeners), which should
     * be taken into account when synchronizing externally. This should be
     * thread-safe though (hopefully).
     */
    public void setOffline() {
        UpdateResult result;
        synchronized (this) {
            // If switching from online to offline
            if (online && recheckOffline == -1) {
                LOGGER.info("Waiting to recheck offline status for " + stream);
                recheckOffline = System.currentTimeMillis();
            } else {
                if (recheckOffline != -1) {
                    //addHistoryItem(recheckOffline, new StreamInfoHistoryItem());
                    LOGGER.info("Offline after check: " + stream);
                }
                recheckOffline = -1;
                this.online = false;
                addHistoryItem(System.currentTimeMillis(), new StreamInfoHistoryItem());
            }
            result = setUpdateSucceeded(true);
        }
        streamInfoUpdated(result);
    }
    
    /**
     *
     * This calls methods intended to be overwritten (listeners), which should
     * be taken into account when synchronizing externally. This should be
     * thread-safe though (hopefully).
     */
    public void setUpdateFailed() {
        UpdateResult result;
        synchronized(this) {
            result = setUpdateSucceeded(false);
        }
        streamInfoUpdated(result);
    }
    
    /**
     * Handle some stuff that is always valid when an update is received (either
     * failed or succeeded). Returns the result of the update (whether the
     * status changed, regular or if it should be set to offline).
     *
     * This should be within the synchronization, but the return value can be
     * used outside the synchronization.
     * 
     * @param succeeded Whether the update succeeded or not
     * @return The UpdateResult indicating whether the status changed and so on
     */
    private UpdateResult setUpdateSucceeded(boolean succeeded) {
        boolean statusChanged = false;
        updateSucceeded = succeeded;
        setUpdated();
        if (succeeded) {
            updateFailedCounter = 0;
        } else {
            updateFailedCounter++;
            if (recheckOffline != -1) {
                /**
                 * If an offline check is pending and the update failed, then
                 * just set as offline now (may of course not be accurate at all
                 * anymore).
                 */
                LOGGER.warning("StreamInfo " + stream + ": Update failed, "
                        + "delayed setting offline");
                return UpdateResult.SET_OFFLINE;
            }
        }
        currentFullStatus = makeFullStatus();
        if (succeeded && !currentFullStatus.equals(prevFullStatus)
                || lastUpdateLongAgo()) {
            prevFullStatus = currentFullStatus;
            statusChanged = true;
        }

        if (statusChanged) {
            return UpdateResult.CHANGED;
        }
        return UpdateResult.UPDATED;
    }
    
    private void setUpdated() {
        lastUpdated = System.currentTimeMillis() / 1000;
        requested = false;
    }
    
    public synchronized void setExpiresAfter(int expiresAfter) {
        this.expiresAfter = expiresAfter;
    }
    
    public String getStream() {
        return stream;
    }
    
    /**
     * The full status consists of the title and game of the stream, which is
     * replaced by appropriate messages if the stream is offline or not title is
     * set. This is meant to be displayed to the user.
     * 
     * If the stream info update failed this is an empty String.
     * 
     * @return The current full status as a String
     */
    public synchronized String getFullStatus() {
        return currentFullStatus;
    }
    
    /**
     * Makes the full status based on the current data.
     * 
     * @return 
     */
    private String makeFullStatus() {
        if (online) {
            String fullStatus = status;
            if (status == null) {
                fullStatus = "No stream title set";
            }
            if (game != null && !game.isEmpty()) {
                fullStatus += " ("+game+")";
            }
            return fullStatus;
        }
        else if (!updateSucceeded) {
            return "";
        }
        else {
            return "Stream offline";
        }
    }
    
    /**
     * The correctly capitalized name of the stream, or the all lowercase name
     * if correctly capitalized name is not set.
     * 
     * @return The correctly capitalized name or all lowercase name
     */
    public String getDisplayName() {
        return display_name != null ? display_name : stream;
    }
    
    /**
     * Whether a correctly capitalized name is set, which if true is returned
     * by {@see getDisplayName()}.
     * 
     * @return true if a correctly capitalized name is set, false otherwise
     */
    public boolean hasDisplayName() {
        return display_name != null;
    }
    
    /**
     * Sets the correctly capitalized name for this stream.
     * 
     * @param name The correctly capitalized name
     */
    public void setDisplayName(String name) {
        this.display_name = name;
    }
    
    public synchronized void setNotFound() {
        notFound = true;
    }
    
    public synchronized boolean isNotFound() {
        return notFound;
    }


    
    /**
     * Whether to recheck the offline status by requesting the stream status
     * again earlier than usual.
     * 
     * @return true if it should be checked, false otherwise
     */
    public synchronized boolean recheckOffline() {
        return recheckOffline != -1
                && System.currentTimeMillis() - recheckOffline > RECHECK_OFFLINE_DELAY;
    }
    
    public synchronized boolean getFollowed() {
        return followed;
    }
    
    public synchronized boolean getOnline() {
        return this.online;
    }
    
    /**
     * The time the stream was started. As always, this may contain stale data
     * if the stream info is not valid or the stream offline.
     * 
     * @return The timestamp or -1 if no time was received
     */
    public synchronized long getTimeStarted() {
        return startedAt;
    }
    
    /**
     * The time the stream was started, including short disconnects (max 10
     * minutes). If there was no disconnect, then the time is equal to
     * getTimeStarted(). As always, this may contain stale data if the stream
     * info is not valid or the stream offline.
     *
     * @return The timestamp or -1 if not time was received or the time is
     * invalid
     */
    public synchronized long getTimeStartedWithPicnic() {
        return startedAtWithPicnic;
    }
    
    public synchronized long getTimeStartedWithPicnicAgo() {
        return System.currentTimeMillis() - startedAtWithPicnic;
    }
    
    /**
     * How long ago the stream was last online. If the stream was never seen as
     * online this session, then a huge number will be returned.
     * 
     * @return The number of seconds that have passed since the stream was last
     * seen as online
     */
    public synchronized long lastOnlineAgo() {
        return (System.currentTimeMillis() - lastOnline) / 1000;
    }
    
    public synchronized long getLastOnlineTime() {
        return lastOnline;
    }
    
    
    
    // Getters
    
    /**
     * Gets the status stored for this stream. May not be correct, check
     * isValid() before using any data.
     * 
     * @return 
     */
    public synchronized String getStatus() {
        return status;
    }
    
    /**
     * Gets the title stored for this stream, which is the same as the status,
     * unless the status is null. As opposed to getStatus() this never returns
     * null.
     * 
     * @return 
     */
    public synchronized String getTitle() {
        if (status == null) {
            return "No stream title set";
        }
        return status;
    }
    
    /**
     * Gets the game stored for this stream. May not be correct, check
     * isValid() before using any data.
     * 
     * @return The name of the game or an empty String if no game is set
     */
    public synchronized String getGame() {
        return game;
    }
    
    /**
     * Gets the viewers stored for this stream. May not be correct, check
     * isValid() before using any data.
     * 
     * @return 
     */
    public synchronized int getViewers() {
        return viewers;
    }
    
    /**
     * Calculates the number of seconds that passed after the last update
     * 
     * @return Number of seconds that have passed after the last update
     */
    public synchronized long getUpdatedDelay() {
        return (System.currentTimeMillis() / 1000) - lastUpdated;
    }
    
    /**
     * Checks if the info should be updated. The stream info takes longer
     * to expire when there were failed attempts at downloading the info from
     * the API. This only affects hasExpired(), not isValid().
     * 
     * @return true if the info should be updated, false otherwise
     */
    public synchronized boolean hasExpired() {
        return getUpdatedDelay() > expiresAfter * (1+ updateFailedCounter / 2);
    }
    
    /**
     * Checks if the info is valid, taking into account if the last request
     * succeeded and how old the data is.
     * 
     * @return true if the info can be used, false otherwise
     */
    public synchronized boolean isValid() {
        if (!updateSucceeded || getUpdatedDelay() > expiresAfter*2) {
            return false;
        }
        return true;
    }
    
    public synchronized boolean lastUpdateLongAgo() {
        if (updateSucceeded && getUpdatedDelay() > expiresAfter*4) {
            return true;
        }
        return false;
    }
    
    /**
     * Returns the number of seconds the last status change is ago.
     * 
     * @return 
     */
    public synchronized long getStatusChangeTimeAgo() {
        return (System.currentTimeMillis() - lastStatusChange) / 1000;
    }
    
    public synchronized long getStatusChangeTime() {
        return lastStatusChange;
    }
    
    @Override
    public synchronized String toString() {
        return "Online: "+online+
                " Status: "+status+
                " Game: "+game+
                " Viewers: "+viewers;
    }
    
    private void addHistoryItem(Long time, StreamInfoHistoryItem item) {
        synchronized(history) {
            history.put(time, item);
        }
    }
    
    public LinkedHashMap<Long,StreamInfoHistoryItem> getHistory() {
        synchronized(history) {
            return new LinkedHashMap<>(history);
        }
    }
    
    /**
     * Create a summary of the viewercount in the interval that hasn't been
     * calculated yet (delay set as a constant).
     * 
     * @param force Get stats even if the delay hasn't passed yet.
     * @return 
     */
    public ViewerStats getViewerStats(boolean force) {
        synchronized(history) {
            if (lastViewerStats == 0 && !force) {
                // No stats output yet, so assume current time as start, so
                // it's output after the set delay
                lastViewerStats = System.currentTimeMillis() - 5000;
            }
            long timePassed = System.currentTimeMillis() - lastViewerStats;
            if (!force && timePassed < VIEWERSTATS_DELAY) {
                return null;
            }
            long startAt = lastViewerStats+1;
            // Only calculate the max length
            if (timePassed > VIEWERSTATS_MAX_LENGTH) {
                startAt = System.currentTimeMillis() - VIEWERSTATS_MAX_LENGTH;
            }
            int min = -1;
            int max = -1;
            int total = 0;
            int count = 0;
            long firstTime = -1;
            long lastTime = -1;
            StringBuilder b = new StringBuilder();
            // Initiate with -2, because -1 already means offline
            int prevViewers = -2;
            for (long time : history.keySet()) {
                if (time < startAt) {
                    continue;
                }
                // Start doing anything for values >= startAt
                
                // Update so that it contains the last value that was looked at
                // at the end of this method
                lastViewerStats = time;
                
                int viewers = history.get(time).getViewers();

                // Append to viewercount development String
                if (prevViewers > -1 && viewers != -1) {
                    // If there is a prevViewers set and if online
                    int diff = viewers - prevViewers;
                    if (diff >= 0) {
                        b.append("+");
                    }
                    b.append(Helper.formatViewerCount(diff));
                } else if (viewers != -1) {
                    if (prevViewers == -1) {
                        // Previous was offline, so show that
                        b.append("_");
                    }
                    b.append(Helper.formatViewerCount(viewers));
                }
                prevViewers = viewers;
                
                
                if (viewers == -1) {
                    continue;
                }
                // Calculate min/max/sum/count only when online
                if (firstTime == -1) {
                    firstTime = time;
                }
                lastTime = time;
                
                if (viewers > max) {
                    max = viewers;
                }
                if (min == -1 || viewers < min) {
                    min = viewers;
                }
                total += viewers;
                count++;
            }
            
            // After going through all values, do some finishing work
            if (prevViewers == -1) {
                // Last value was offline, so show that
                b.append("_");
            }
            if (count == 0) {
                return null;
            }
            int avg = total / count;
            return new ViewerStats(min, max, avg, firstTime, lastTime, count, b.toString());
        }
    }
    
    /**
     * Holds a set of immutable values that make up viewerstats.
     */
    public static class ViewerStats {
        public final int max;
        public final int min;
        public final int avg;
        public final long startTime;
        public final long endTime;
        public final int count;
        public final String history;
        
        public ViewerStats(int min, int max, int avg, long startTime,
                long endTime, int count, String history) {
            this.max = max;
            this.min = min;
            this.avg = avg;
            this.startTime = startTime;
            this.endTime = endTime;
            this.count = count;
            this.history = history;
        }
        
        /**
         * Which duration the data in this stats covers. This is not necessarily
         * the whole duration that was worked with (e.g. if the stream went
         * offline at the end, that data may not be included). This is the range
         * between the first and last valid data point.
         * 
         * @return The number of seconds this data covers.
         */
        public long duration() {
            return (endTime - startTime) / 1000;
        }
        
        /**
         * Checks if these viewerstats contain any viewer data.
         * 
         * @return 
         */
        public boolean isValid() {
            // If min was set to another value than the initial one, then this
            // means at least one data point with a viewercount was there.
            return min != -1;
        }
        
        @Override
        public String toString() {
            return "Viewerstats ("+DateTime.format2(startTime)
                    +"-"+DateTime.format2(endTime)+"):"
                    + " avg:"+Helper.formatViewerCount(avg)
                    + " min:"+Helper.formatViewerCount(min)
                    + " max:"+Helper.formatViewerCount(max)
                    + " ["+count+"/"+history+"]";
        }
    }
}
