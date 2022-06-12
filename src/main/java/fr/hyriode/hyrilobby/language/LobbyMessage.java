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

    JOIN_MESSAGE("message.join.display"),
    JOIN_QUEUE_MESSAGE("message.queue.display"),

    JUMP_JOIN_MESSAGE("message.jump-join.display"),
    JUMP_LEAVE_MESSAGE("message.jump-leave.display"),
    JUMP_RESPAWN_BAR("bar.jump-respawn.display"),
    JUMP_SUCCESS_CHECKPOINT("message.checkpoint-success.display"),
    JUMP_RESET("message.reset.display"),
    JUMP_GO_BACK_CHECKPOINT("message.back-checkpoint.display"),
    JUMP_SUCCESS_ALL("message.jump-success.display"),

    STAFF_ERROR("message.error-staff.display"),
    IN_PARTY_ERROR("message.queue-party-error.display"),

    FLY_COMMAND_RESULT("command.fly.result"),
    FLY_COMMAND_ERROR("command.fly.error"),
    FLY_COMMAND_ON("command.fly.value.on"),
    FLY_COMMAND_OFF("command.fly.value.off"),

    BASIC_JOIN_SUBTITLE("subtitle.basic.display"),
    FIRST_JOIN_SUBTITLE("subtitle.first.display"),

    SELECTOR_LOBBY_ID("gui.lobby-selector.item.name"),

    SETTINGS_MSG_SOUND("gui.settings.item.msg-sound.name"),
    SETTINGS_GLOBAL_CHAT("gui.settings.item.global-chat.name"),
    SETTINGS_PARTY_REQUEST("gui.settings.item.party-request.name"),
    SETTINGS_FRIEND_REQUEST("gui.settings.item.friend-request.name"),
    SETTINGS_LANG("gui.settings.item.lang.name"),
    SETTINGS_MSG_LEVELS("gui.settings.item.msg-levels.name"),
    SETTINGS_VISIBILITY_LEVELS("gui.settings.item.visibility-levels.name"),

    PROFILE_FRIENDS_NAME("gui.profile.item.friends.name"),
    PROFILE_FRIENDS_LORE("gui.profile.item.friends.lore"),
    PROFILE_LANGUAGE_NAME("gui.profile.item.language.name"),
    PROFILE_LANGUAGE_LORE("gui.profile.item.language.lore"),
    PROFILE_ACCOUNT("gui.profile.item.account.name"),
    PROFILE_RANK("gui.profile.item.rank.name"),
    PROFILE_LEVEL("gui.profile.item.level.name"),
    PROFILE_FIRST_JOIN("gui.profile.item.first-join.name"),
    PROFILE_PLAYTIME("gui.profile.item.playtime.name"),
    PROFILE_HYRIPLUS_BUY_DATE("gui.profile.item.hyriplus-buy-date.name"),
    PROFILE_HYRIPLUS_EXPIRE_DATE("gui.profile.item.hyriplus-expire-date.name"),
    PROFILE_HYRIPLUS_REMAINING("gui.profile.item.hyriplus-remaining.name"),
    PROFILE_HYRIPLUS_DONT_HAVE("gui.profile.item.hyriplus-dont-have.name"),

    FRIENDS_LEVEL("gui.friends.item.level.name"),
    FRIENDS_LAST_SEEN("gui.friends.item.last-seen.name"),
    FRIENDS_REMOVE("gui.friends.item.remove.name"),
    FRIENDS_ADD("gui.friends.item.add.name"),
    FRIENDS_ADD_ENTER_NAME("gui.friends.item.add.enter-name.name"),
    FRIENDS_ADD_NOT_NULL("gui.friends.item.add.not-null.name"),
    FRIENDS_ADD_CANT_FOUND("gui.friends.item.add.cant-found.name"),
    FRIENDS_ADD_ALREADY_FRIEND("gui.friends.item.add.already-friend.name"),
    FRIENDS_ADD_SUCCESS("gui.friends.item.add.success.name"),

    HOST_ITEM("gui.game-selector.item.host.name"),
    VIP_ITEM("gui.game-selector.item.vip.name"),
    JUMP_ITEM("gui.game-selector.item.jump.name"),

    CONNECTED_LINE("basic.connected"),
    CONNECT_LINE("basic.connect"),
    LOBBY_CONNECT("lobby.connect"),
    LOBBY_PLAYERS_LINE("basic.players"),
    SOON_LINE("basic.soon"),
    BACK_ITEM("basic.item.back"),
    CURRENT_ITEM("basic.item.current"),
    TYPE_LINE("basic.game.type"),
    ;

    private final String key;

    LobbyMessage(String key) {
        this.key = key;
    }

    public HyriLanguageMessage get() {
        return HyriLobby.getLanguageManager().getMessage(this.key);
    }

    public List<String> getAsList(Player player) {
        final String str = HyriLobby.getLanguageManager().getValue(player,this.key);
        final String[] splitLore = str.split("\n");

        return new ArrayList<>(Arrays.asList(splitLore));
    }
}
