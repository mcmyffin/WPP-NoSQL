
package chatty.gui;

import chatty.User;
import chatty.sniff.Sniff;
import chatty.util.api.Emoticons;
import java.awt.Color;

/**
 * A single chat message, containing all the metadata.
 * 
 * @author tduva
 */
public class Message {

    public final long timeStamp;
    public final String text;
    public final User user;
    public final Emoticons.TagEmotes emotes;
    public Color color;
    public boolean whisper;
    public boolean highlighted;
    public boolean ignored_compact;
    public boolean action;
    
    public Message(long timeStamp, User user, String text, Emoticons.TagEmotes emotes) {
        this.timeStamp = timeStamp;
        this.text = text;
        this.user = user;
        this.emotes = emotes;
        Sniff.addData(timeStamp,user.nick,text);
    }
    
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
    
    public boolean isHighlighted() {
        return highlighted;
    }
    
    public void setAction(boolean action) {
        this.action = action;
    }
    
    public boolean isAction() {
        return action;
    }
    
    public void setIgnoredCompact(boolean ignored) {
        this.ignored_compact = ignored;
    }
    
    public boolean isIgnoredCompact() {
        return ignored_compact;
    }
    
    public void setWhisper(boolean whisper) {
        this.whisper = whisper;
    }
    
    public boolean isWhisper() {
        return whisper;
    }

    public long getTimeStamp(){
        return this.timeStamp;
    }
}
