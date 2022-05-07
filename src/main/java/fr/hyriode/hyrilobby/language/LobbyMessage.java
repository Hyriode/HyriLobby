package fr.hyriode.hyrilobby.language;

import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrilobby.HyriLobby;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 25/04/2022 at 16:25
 */
public enum LobbyMessage {

    JOIN_MESSAGE("join"),
    JOIN_QUEUE_MESSAGE("queue"),

    FLY_COMMAND_RESULT("fly"),

    BASIC_SUBTITLE("basic"),
    FIRST_SUBTITLE("first"),
    BUY_TITLE("buy"),

    SELECTOR_GUI_LOBBY_NAME("basic"),

    SETTINGS_MSG_SOUND("msg-sound"),
    SETTINGS_GLOBAL_CHAT("global-chat"),
    SETTINGS_PARTY_REQUEST("party-request"),
    SETTINGS_FRIEND_REQUEST("friend-request"),
    SETTINGS_LANG("lang"),
    SETTINGS_MSG_LEVELS("msg-levels"),
    SETTINGS_VISIBILITY_LEVELS("visibility-levels"),



    HOST_ITEM("host"),
    VIP_ITEM("vip"),
    JUMP_ITEM("jump"),

    SELECTOR_GUI_LOBBY_FRIENDS_LINE("basic.friends"),
    CONNECTED_LINE("basic.connected"),
    CONNECT_LINE("basic.connect"),
    LOBBY_CONNECT("lobby.connect"),
    LOBBY_PLAYERS_LINE("basic.players"),
    SOON_LINE("basic.soon"),
    TYPE_LINE("basic.type"),
    BACK_ITEM("basic.item.back"),
    CURRENT_ITEM("basic.item.current"),
    ;

    private final String key;

    LobbyMessage(String key) {
        this.key = key;
    }

    public HyriLanguageMessage get() {
        return HyriLobby.getLanguageManager().getMessage(this.key);
    }

    public HyriLanguageMessage getCommand() {
        return HyriLobby.getLanguageManager().getMessage("command." + this.key + ".result");
    }

    public HyriLanguageMessage getMessage() {
        return HyriLobby.getLanguageManager().getMessage("message." + this.key + ".display");
    }

    public HyriLanguageMessage getTitle() {
        return HyriLobby.getLanguageManager().getMessage("title." + this.key + ".display");
    }

    public HyriLanguageMessage getSubTitle() {
        return HyriLobby.getLanguageManager().getMessage("subtitle." + this.key + ".display");
    }

    public HyriLanguageMessage getActionBar() {
        return HyriLobby.getLanguageManager().getMessage("actionbar." + this.key + ".display");
    }

    public HyriLanguageMessage getGuiItem(String guiName) {
        return HyriLobby.getLanguageManager().getMessage("gui." + guiName + ".item." + this.key + ".name");
    }

    public HyriLanguageMessage getGuiItem() {
        return HyriLobby.getLanguageManager().getMessage("gui.item." + this.key + ".name");
    }

    public List<String> getItemLore(Player player) {
        final String str = HyriLobby.getLanguageManager().getValue(player,"item." +this.key+ ".lore");
        final String[] splitLore = str.split("\n");

        return new ArrayList<>(Arrays.asList(splitLore));
    }
}
