
package chatty.gui.components.menus;

import chatty.Chatty;
import chatty.Helper;
import chatty.util.StringUtil;
import chatty.util.api.Emoticon;
import chatty.util.api.Emoticon.EmoticonImage;
import chatty.util.api.Emoticons;
import java.awt.event.ActionEvent;

/**
 * Shows information about the emote that was right-clicked on.
 * 
 * @author tduva
 */
public class EmoteContextMenu extends ContextMenu {
    
    private static Emoticons emoteManager;
    private final ContextMenuListener listener;
    private final EmoticonImage emoteImage;
    
    public EmoteContextMenu(EmoticonImage emoteImage, ContextMenuListener listener) {
        Emoticon emote = emoteImage.getEmoticon();
        this.listener = listener;
        this.emoteImage = emoteImage;
        
        addItem("code", StringUtil.shortenTo(emote.code, 40, 28));
        addItem("emoteImage", emoteImage.getSizeString());
        if (emote.numericId != Emoticon.ID_UNDEFINED) {
            addItem("emoteId", "ID: "+emote.numericId);
        }
        
        // Non-Twitch Emote Information
        if (emote.type != Emoticon.Type.TWITCH) {
            addSeparator();
            if (emote.type == Emoticon.Type.FFZ) {
                addItem("ffzlink", "FrankerFaceZ Emote");
                if (emote.creator != null) {
                    addItem("emoteCreator", "Emote by: "+emote.creator);
                }
            } else if (emote.type == Emoticon.Type.BTTV) {
                addItem("bttvlink", "BetterTTV Emote");
                if (emote.hasStreamSet()
                        && emote.emoteSet == Emoticon.SET_UNDEFINED
                        && Helper.validateStream(emote.getStream())) {
                    addItem("", emote.getStream());
                }
            } else if (emote.type == Emoticon.Type.CUSTOM) {
                addItem("", "Custom Emote");
            }
            if (emote.info != null) {
                addItem("", emote.info);
            }
            addStreamSubmenu(emote);
        }
        
        // Emoteset information
        if (emote.emoteSet > Emoticon.SET_UNDEFINED) {
            addSeparator();
            if (Emoticons.isTurboEmoteset(emote.emoteSet)) {
                addItem("twitchturbolink", "Turbo Emoticon");
            } else {
                addItem("", "Subscriber Emoticon");
                addStreamSubmenu(emote);
            }
            addItem("", "Emoteset: "+emote.emoteSet);
        }
        if (emote.emoteSet == Emoticon.SET_UNKNOWN) {
            addSeparator();
            addItem("", "Emoteset: unknown");
        }
        
        addSeparator();
        addItem("emoteDetails", "Show Details");
        
        addSeparator();
        addItem("ignoreEmote", "Ignore");
        if (!emote.hasStreamRestrictions()) {
            if (emoteManager.isFavorite(emote)) {
                addItem("unfavoriteEmote", "UnFavorite");
            } else {
                addItem("favoriteEmote", "Favorite");
            }
        }
        
        if (Chatty.DEBUG) {
            addItem("", String.valueOf(System.identityHashCode(emote)));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (listener != null) {
            listener.emoteMenuItemClicked(e, emoteImage);
        }
    }
    
    public static void setEmoteManager(Emoticons emotes) {
        emoteManager = emotes;
    }
    
    private void addStreamSubmenu(Emoticon emote) {
        if (emote.hasStreamSet() && Helper.validateStream(emote.getStream())) {
            String subMenu = emote.getStream();
            addItem("profile", "Twitch Profile", subMenu);
            addItem("join", "Join " + Helper.toValidChannel(emote.getStream()), subMenu);
            addSeparator(subMenu);
            addItem("showChannelEmotes", "Show Emotes", subMenu);
        }
    }
    
}
