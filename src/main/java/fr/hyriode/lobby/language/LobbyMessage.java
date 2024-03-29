package fr.hyriode.lobby.language;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.HyriConstants;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.game.rotating.IHyriRotatingGame;
import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.player.IHyriPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 25/04/2022 at 16:25
 */
public enum LobbyMessage {

    JOIN_MESSAGE("message.join.display", (target, input) -> input.replace("%website%", HyriConstants.WEBSITE_URL).replace("%store%", HyriConstants.STORE_WEBSITE_URL).replace("%discord%", HyriConstants.DISCORD_URL)),
    JOIN_VIP_MESSAGE("message.join-vip.display"),

    STAFF_ERROR("message.error-staff.display"),

    LOBBY_RESTARTING_MESSAGE("message.lobby-restarting.display"),

    QUEUE_PREFIX("prefix.queue"),
    QUEUE_NORMAL_PLAYER_JOINED_MESSAGE("message.normal-queue.player-joined", QUEUE_PREFIX),
    QUEUE_NORMAL_PLAYER_LEFT_MESSAGE("message.normal-queue.player-left", QUEUE_PREFIX),
    QUEUE_NORMAL_DISPLAY_BAR("bar.normal-queue.display", QUEUE_PREFIX),
    QUEUE_HOST_PLAYER_JOINED_MESSAGE("message.host-queue.player-joined", QUEUE_PREFIX),
    QUEUE_HOST_PLAYER_LEFT_MESSAGE("message.host-queue.player-left", QUEUE_PREFIX),
    QUEUE_HOST_DISPLAY_BAR("bar.host-queue.display", QUEUE_PREFIX),

    JUMP_PREFIX("prefix.jump"),
    JUMP_JOIN_MESSAGE("message.jump-join", JUMP_PREFIX),
    JUMP_LEAVE_MESSAGE("message.jump-leave", JUMP_PREFIX),
    JUMP_SUCCESS_CHECKPOINT_MESSAGE("message.checkpoint-success", JUMP_PREFIX),
    JUMP_RESET_MESSAGE("message.reset", JUMP_PREFIX),
    JUMP_GO_BACK_CHECKPOINT_MESSAGE("message.back-checkpoint", JUMP_PREFIX),
    JUMP_SUCCESS_ALL_MESSAGE("message.jump-success", JUMP_PREFIX),
    JUMP_RESPAWN_BAR("bar.jump-respawn"),
    JUMP_TIME_BAR("bar.jump-time", JUMP_PREFIX),

    FLY_COMMAND_RESULT("command.fly.result"),
    FLY_COMMAND_ERROR("command.fly.error"),
    FLY_COMMAND_ON("command.fly.value.on"),
    FLY_COMMAND_OFF("command.fly.value.off"),

    BASIC_JOIN_SUBTITLE("subtitle.basic.display"),
    FIRST_JOIN_SUBTITLE("subtitle.first.display"),

    SELECTOR_LOBBY_ID("gui.lobby-selector.item.name"),

    SETTINGS_PREFIX("prefix.settings"),
    SETTINGS_UPDATED_MESSAGE("message.settings.updated", SETTINGS_PREFIX),
    SETTINGS_PRIVATE_MSG_NAME("gui.settings.item.private-msg.name"),
    SETTINGS_PRIVATE_MSG_DESCRIPTION("gui.settings.item.private-msg.description"),
    SETTINGS_PRIVATE_MSG_SOUND_NAME("gui.settings.item.private-msg-sound.name"),
    SETTINGS_PRIVATE_MSG_SOUND_DESCRIPTION("gui.settings.item.private-msg-sound.description"),
    SETTINGS_GLOBAL_CHAT_NAME("gui.settings.item.global-chat.name"),
    SETTINGS_GLOBAL_CHAT_DESCRIPTION("gui.settings.item.global-chat.description"),
    SETTINGS_PARTY_REQUEST_NAME("gui.settings.item.party-request.name"),
    SETTINGS_PARTY_REQUEST_DESCRIPTION("gui.settings.item.party-request.description"),
    SETTINGS_FRIEND_REQUEST_NAME("gui.settings.item.friend-request.name"),
    SETTINGS_FRIEND_REQUEST_DESCRIPTION("gui.settings.item.friend-request.description"),
    SETTINGS_FRIEND_CONNECTION_NAME("gui.settings.item.friend-connection.name"),
    SETTINGS_FRIEND_CONNECTION_DESCRIPTION("gui.settings.item.friend-connection.description"),
    SETTINGS_AUTO_QUEUE_NAME("gui.settings.item.auto-queue.name"),
    SETTINGS_AUTO_QUEUE_DESCRIPTION("gui.settings.item.auto-queue.description"),
    SETTINGS_FOLLOW_PARTY_NAME("gui.settings.item.follow-party.name"),
    SETTINGS_FOLLOW_PARTY_DESCRIPTION("gui.settings.item.follow-party.description"),
    SETTINGS_VISIBILITY_NAME("gui.settings.item.visibility.name"),
    SETTINGS_VISIBILITY_DESCRIPTION("gui.settings.item.visibility.description"),
    SETTINGS_LANG("gui.settings.item.lang.name"),

    SETTINGS_STATUS("gui.settings.status"),
    SETTINGS_ON("gui.settings.on"),
    SETTINGS_OFF("gui.settings.off"),
    SETTINGS_LEVEL_ALL("gui.settings.level.all"),
    SETTINGS_LEVEL_PARTY("gui.settings.level.party"),
    SETTINGS_LEVEL_FRIENDS("gui.settings.level.friends"),
    SETTINGS_LEVEL_NONE("gui.settings.level.none"),

    LANG_SETTINGS_ITEM_NAME("lang.settings-item.name"),
    LANG_SETTINGS_ITEM_LORE("lang.settings-item.lore"),
    LANG_ITEM_USING_LORE("lang.item.using.lore"),
    LANG_ITEM_LORE("lang.item.lore"),
    LANG_UPDATED_MESSAGE("message.settings.lang-changed", SETTINGS_PREFIX),

    PROFILE_FRIENDS_NAME("gui.profile.item.friends.name"),
    PROFILE_FRIENDS_LORE("gui.profile.item.friends.lore"),

    PROFILE_SETTINGS_NAME("gui.profile.item.settings.name"),
    PROFILE_SETTINGS_LORE("gui.profile.item.settings.lore"),

    PROFILE_ACCOUNT_NAME("gui.profile.item.account.name"),
    PROFILE_ACCOUNT_LORE("gui.profile.item.account.lore"),
    PROFILE_LEVELING_NAME("gui.profile.item.leveling.name"),
    PROFILE_LEVELING_LORE("gui.profile.item.leveling.lore"),

    PROFILE_HYRIPLUS_BUY_DATE("gui.profile.item.hyriplus-buy-date.name"),
    PROFILE_HYRIPLUS_EXPIRE_DATE("gui.profile.item.hyriplus-expire-date.name"),
    PROFILE_HYRIPLUS_REMAINING("gui.profile.item.hyriplus-remaining.name"),
    PROFILE_HYRIPLUS_PLUS_COLOR("gui.profile.item.hyriplus-plus-color.name"),
    PROFILE_HYRIPLUS_DONT_HAVE("gui.profile.item.hyriplus-dont-have.name"),
    PROFILE_HYRIPLUS_EXPLANATION("gui.profile.item.hyriplus-explanation.name"),
    PROFILE_HYRIPLUS_STORE_LINK("gui.profile.item.hyriplus-store-link.name"),
    PROFILE_HYRIPLUS_CHANGE_COLOR("gui.profile.item.hyriplus-change-color.name"),

    PROFILE_BOOSTERS_NAME("gui.profile.item.boosters.name"),
    PROFILE_BOOSTERS_LORE("gui.profile.item.boosters.lore"),
    PROFILE_LOOTBOXES_NAME("gui.profile.item.lootboxes.name"),
    PROFILE_LOOTBOXES_DESCRIPTION("gui.profile.item.lootboxes.description"),

    PROFILE_STATS_NAME("gui.profile.item.statistics.name"),
    PROFILE_STATS_LORE("gui.profile.item.statistics.lore"),

    FRIENDS_FRIEND_ITEM_LORE("gui.friends.friend.item.lore"),
    FRIENDS_ADD_FRIEND_ITEM_NAME("gui.friends.add-friend.item.name"),
    FRIENDS_ADD_FRIEND_ITEM_LORE("gui.friends.add-friend.item.lore"),
    FRIENDS_ADD_FRIEND_SIGN_LINE("gui.friends.add-friend.sign.line"),

    HYRIPLUS_PREFIX("prefix.hyriplus"),
    HYRIPLUS_COLOR_LORE("hyriplus.color.item.lore"),
    HYRIPLUS_NOT_UNLOCKED_LINE("hyriplus.not-unlocked.line"),
    HYRIPLUS_SELECTED_LINE("hyriplus.selected.line"),
    HYRIPLUS_NOT_UNLOCKED_MESSAGE("hyriplus.not-unlocked.message", HYRIPLUS_PREFIX),
    HYRIPLUS_CHANGED_MESSAGE("hyriplus.changed.message", HYRIPLUS_PREFIX),

    BOOSTERS_BOOSTER_ITEM_NAME("gui.boosters.booster.item.name"),
    BOOSTERS_BOOSTER_ITEM_LORE("gui.boosters.booster.item.lore"),

    BOOSTER_GAME_SELECTOR_ITEM_LORE("booster-game-selector.item.lore"),
    BOOSTER_GAME_SELECTOR_NO_BOOSTER("booster-game-selector.no-booster"),
    BOOSTER_GAME_SELECTOR_ICON_TIME_SLOT("booster-game-selector.icon.time-slot"),
    BOOSTER_GAME_SELECTOR_TIME_SLOT("booster-game-selector.time-slot"),
    BOOSTER_GAME_SELECTOR_CURRENT("booster-game-selector.current"),

    LEVELING_PREFIX("prefix.leveling"),
    LEVELING_REWARD_NOT_LEVEL("leveling.reward-not-level.message", LEVELING_PREFIX),
    LEVELING_REWARD_CLAIMED("leveling.reward-claimed.message", LEVELING_PREFIX),
    LEVELING_REWARD_ALREADY_CLAIMED("leveling.reward-already-claimed.message", LEVELING_PREFIX),
    LEVELING_REWARD_ALREADY_CLAIMED_LINE("leveling.reward-already-claimed.line"),
    LEVELING_REWARD_NOT_LEVEL_LINE("leveling.reward-not-level.line"),
    LEVELING_REWARD_ITEM("leveling.reward.item.name"),

    LOOTBOX_ITEM_NAME("gui.lootboxes.item.name"),
    LOOTBOX_REWARD_MESSAGE("lootbox.reward.message"),
    LOOTBOX_COSMETIC_REWARD_MESSAGE("lootbox.cosmetic-reward.message"),
    LOOTBOX_COSMETIC_ALREADY_OWNED_MESSAGE("lootbox.cosmetic-already-owned.message"),

    STORE_PREFIX("prefix.store"),
    STORE_LOOTBOXES_NAME("gui.store.item.lootboxes.name"),
    STORE_LOOTBOXES_LORE("gui.store.item.lootboxes.lore"),
    STORE_BOOSTERS_NAME("gui.store.item.boosters.name"),
    STORE_BOOSTERS_LORE("gui.store.item.boosters.lore"),
    STORE_RANKS_NAME("gui.store.item.ranks.name"),
    STORE_RANKS_LORE("gui.store.item.ranks.lore"),
    STORE_GAMES_NAME("gui.store.item.games.name"),
    STORE_GAMES_LORE("gui.store.item.games.lore"),
    STORE_COSMETICS_NAME("gui.store.item.cosmetics.name"),
    STORE_COSMETICS_LORE("gui.store.item.cosmetics.lore"),

    STORE_BOOSTERS_TYPE_NAME("gui.store-boosters.item.type.name"),
    STORE_BOOSTERS_TYPE_DESCRIPTION("gui.store-boosters.item.type.description"),

    STORE_MONEY_EDIT_NAME("gui.store-confirm.money-edit.name"),
    STORE_MONEY_EDIT_DESCRIPTION("gui.store-confirm.money-edit.description"),

    STORE_PRICE_LINE("gui.store.item.price"),
    STORE_OWN_LINE("gui.store.item.own"),

    STORE_NOT_ENOUGH_MONEY_MESSAGE("message.store.not-enough-money", STORE_PREFIX),
    STORE_PURCHASE_CONFIRMED_MESSAGE("message.store.purchase-confirmed", STORE_PREFIX),
    STORE_PURCHASE_CANCELLED_MESSAGE("message.store.purchase-cancelled", STORE_PREFIX),
    STORE_ONLY_ON_WEBSITE_MESSAGE("message.store.only-on-website", STORE_PREFIX),
    STORE_ALREADY_OWN_MESSAGE("message.store.already-own", STORE_PREFIX),
    STORE_DEPENDS_NOT_OWNED_MESSAGE("message.store.depends-not-owned", STORE_PREFIX),

    CONFIRM_ITEM_CONFIRM_NAME("gui.confirm.item.confirm.name"),
    CONFIRM_ITEM_CANCEL_NAME("gui.confirm.item.cancel.name"),

    SELECTOR_HOME_ITEM_NAME("gui.game-selector.item.home.name"),
    SELECTOR_HOME_ITEM_DESCRIPTION("gui.game-selector.item.home.description"),
    SELECTOR_HOST_ITEM_NAME("gui.game-selector.item.host.name"),
    SELECTOR_HOST_ITEM_DESCRIPTION("gui.game-selector.item.host.description"),
    SELECTOR_VIP_ITEM_NAME("gui.game-selector.item.vip.name"),
    SELECTOR_VIP_ITEM_DESCRIPTION("gui.game-selector.item.vip.description"),
    SELECTOR_JUMP_ITEM_NAME("gui.game-selector.item.jump.name"),
    SELECTOR_JUMP_ITEM_DESCRIPTION("gui.game-selector.item.jump.description"),
    SELECTOR_ROTATING_GAME_ITEM_NAME("gui.game-selector.item.rotating-game.name"),
    SELECTOR_ROTATING_GAME_ITEM_DESCRIPTION("gui.game-selector.item.rotating-game.description", (target, input) -> {
        final IHyriRotatingGame game = HyriAPI.get().getGameManager().getRotatingGameManager().getRotatingGame();

        if (game == null) {
            return input;
        }

        final IHyriGameInfo gameInfo = game.getInfo();

        if (gameInfo == null) {
            return input;
        }

        final int players = HyriAPI.get().getNetworkManager()
                .getPlayerCounter()
                .getCategory(gameInfo.getName())
                .getPlayers();

        return input.replace("%game%", gameInfo.getDisplayName())
                .replace("%players%", String.valueOf(players));
    }),
    SELECTOR_SERVER_ITEM_DESCRIPTION("gui.server-selector.item.server.description"),

    GAME_NPC_HEADER_PLAYERS("game.npc.header.players"),
    GAME_NPC_HEADER_PLAY("game.npc.header.play"),

    NPC_LANGUAGE_HEADER("npc.language.header"),
    NPC_ROTATING_GAME_HEADER("npc.rotating-game.header", (target, input) -> {
        final IHyriRotatingGame game = HyriAPI.get().getGameManager().getRotatingGameManager().getRotatingGame();

        if (game == null) {
            return input;
        }

        final IHyriGameInfo gameInfo = game.getInfo();

        if (gameInfo == null) {
            return input;
        }
        return input.replace("%rotating_game%", gameInfo.getDisplayName()).replace("%players%", String.valueOf(HyriAPI.get().getNetworkManager().getPlayerCounter().getCategory(gameInfo.getName()).getPlayers()));
    }),
    NPC_HOST_HEADER("npc.host.header"),

    HOST_ITEM_LORE("host.item.lore"),
    HOST_CREATE_ITEM_NAME("host.create-item.name"),
    HOST_CREATE_ITEM_LORE("host.create-item.lore"),
    HOST_UNLIMITED_WORD("host.unlimited.word"),
    HOST_CREATE_ERROR("host.create.error.message"),
    HOST_SPECTATING_ITEM_NAME("host.spectating-item.name"),
    HOST_SPECTATING_ITEM_LORE("host.spectating-item.lore"),
    HOST_TYPE_ITEM_NAME("host.type-item.name"),
    HOST_TYPE_ITEM_LORE("host.type-item.lore"),
    HOST_CLICK_TO_CREATE("host.click.to-create"),
    HOST_CREATING_HOST_BAR("host.creating-host.bar"),
    HOST_ADVERT_MESSAGE("host.advert.message"),
    HOST_LIMIT_EXCEEDED_MESSAGE("host.limit-exceeded.message"),

    GAME_BOOSTER_LINE("game.booster.line"),
    BOOSTER_ADDED_IN_QUEUE_MESSAGE("booster.added-in-queue.message"),

    CLICK_TO_CHANGE("click.to-change"),
    CLICK_TO_BUY("click.to-buy"),
    CLICK_TO_JOIN("click.to-join"),
    CLICK_TO_SPECTATE("click.to-spectate"),
    CLICK_TO_SELECT("click.to-select"),
    CLICK_TO_SHOW("click.to-show"),
    CLICK_TO_SEE("click.to-see"),
    CLICK_TO_CLAIM("click.to-claim"),
    CLICK_TO_OPEN("click.to-open"),

    LEADERBOARD_LEVELING_HEADER("leaderboard.leveling.header"),
    LEADERBOARD_JUMP_HEADER("leaderboard.jump.header"),

    PLAYER_STATUS_ACTION_BAR("action-bar.session-status.display"),
    PLAYER_STATUS_NICK("action-bar.session-status.nick"),
    PLAYER_STATUS_VANISH("action-bar.session-status.vanish"),

    FRIENDS("basic.friends"),
    CONNECTED_LINE("basic.connected"),
    PLAY("basic.play"),
    LOBBY_CONNECT("lobby.connect"),
    LOBBY_PLAYERS_LINE("basic.players"),
    SOON_LINE("basic.soon"),
    BACK_ITEM("basic.item.back"),
    CURRENT_ITEM("basic.item.current,"),
    TYPE_LINE("basic.game.type"),

    PREVIOUS_ITEM_NAME("basic.item.page.previous.name"),
    PREVIOUS_ITEM_LORE("basic.item.page.previous.lore"),
    NEXT_ITEM_NAME("basic.item.page.next.name"),
    NEXT_ITEM_LORE("basic.item.page.next.lore"),

    CASINO_PREFIX("prefix.casino"),
    ROLLER_MACHINES_NAME("game.casino.roller-machines.name"),
    WWTBAM_NAME("game.casino.wwtbam.name"),
    DO_NOT_HAVE_ENOUGH_HYRIS("message.casino.dont-have-hyris", CASINO_PREFIX),

    CASINO_WON("message.won", CASINO_PREFIX),
    CASINO_LOST("message.lost", CASINO_PREFIX),
    CASINO_DRAW("message.draw", CASINO_PREFIX),

    CASINO_RELOAD("game.casino.007.reload"),
    CASINO_SHOOT("game.casino.007.shoot"),
    CASINO_PROTECT("game.casino.007.protect"),
    CASINO_RELOAD_BEFORE_SHOOTING("game.casino.007.reload-before", CASINO_PREFIX),

    CASINO_PLAYER_RELOAD("message.casino.007.reload", CASINO_PREFIX),
    CASINO_PLAYER_SHOOT("message.casino.007.shoot", CASINO_PREFIX),
    CASINO_PLAYER_PROTECT("message.casino.007.protect", CASINO_PREFIX),

    CASINO_BOT_RELOAD("message.casino.007.bot-reload", CASINO_PREFIX),
    CASINO_BOT_SHOOT("message.casino.007.bot-shoot", CASINO_PREFIX),
    CASINO_BOT_PROTECT("message.casino.007.bot-protect", CASINO_PREFIX),
    CASINO_RECOVER_BET("game.casino.wwtbam.recover-bet"),
    CASINO_PUT_THEM_BACK("game.casino.wwtbam.put-them-back"),
    CASINO_CONTINUE("game.casino.wwtbam.continue"),
    CASINO_CONFIRM_BET("click.to-confirm-bet"),
    CASINO_CANCEL_BET("message.casino.cancel-bet", CASINO_PREFIX),

    ROLLER_MACHINES_DESCRIPTION("hologram.casino.roller-machines.description"),

    CASINO_SHIFUMI_PLAYER_ROCK("message.casino.shifumi.rock", CASINO_PREFIX),
    CASINO_SHIFUMI_PLAYER_PAPER("message.casino.shifumi.paper", CASINO_PREFIX),
    CASINO_SHIFUMI_PLAYER_SCISSORS("message.casino.shifumi.scissors", CASINO_PREFIX),
    CASINO_SHIFUMI_BOT_ROCK("message.casino.shifumi.bot-rock", CASINO_PREFIX),
    CASINO_SHIFUMI_BOT_PAPER("message.casino.shifumi.bot-paper", CASINO_PREFIX),
    CASINO_SHIFUMI_BOT_SCISSORS("message.casino.shifumi.bot-scissors", CASINO_PREFIX),

    CASINO_SHIFUMI_ROCK("game.casino.shifumi.rock"),
    CASINO_SHIFUMI_PAPER("game.casino.shifumi.paper"),
    CASINO_SHIFUMI_SCISSORS("game.casino.shifumi.scissors"),
    CASINO_SHIFUMI_EQUALITY(  "game.casino.shifumi.equality", CASINO_PREFIX),
    HOLOGRAM_CASINO_ROLLER_MACHINES_DESCRIPTION("hologram.casino.roller-machines.description"),
    HOLOGRAM_CASINO_WWTBAM_DESCRIPTION("hologram.casino.wwtbam.description"),
    HOLOGRAM_CASINO_SHIFUMI_DESCRIPTION("hologram.casino.shifumi.description")
    ;

    private HyriLanguageMessage languageMessage;

    private final String key;
    private final BiFunction<Player, String, String> formatter;

    LobbyMessage(String key, BiFunction<Player, String, String> formatter) {
        this.key = key;
        this.formatter = formatter;
    }

    LobbyMessage(String key, LobbyMessage prefix) {
        this.key = key;
        this.formatter = (target, input) -> prefix.asString(target) + input;
    }

    LobbyMessage(String key) {
        this(key, (target, input) -> input);
    }

    public HyriLanguageMessage asLang() {
        return this.languageMessage == null ? this.languageMessage = HyriLanguageMessage.get(this.key) : this.languageMessage;
    }

    public String asString(IHyriPlayer account) {
        return this.formatter.apply(Bukkit.getPlayer(account.getUniqueId()), this.asLang().getValue(account));
    }

    public String asString(Player player) {
        return this.formatter.apply(player, this.asLang().getValue(player));
    }

    public void sendTo(Player player) {
        player.sendMessage(this.asString(player));
    }

    public List<String> asList(IHyriPlayer account) {
        return new ArrayList<>(Arrays.asList(this.asString(account).split("\n")));
    }

    public List<String> asList(Player player) {
        return new ArrayList<>(Arrays.asList(this.asString(player).split("\n")));
    }

    public List<String> asList(HyriLanguage language) {
        return new ArrayList<>(Arrays.asList(this.asLang().getValue(language).split("\n")));
    }

}
