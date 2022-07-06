package fr.hyriode.lobby.player;


import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.HyriConstants;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.queue.IHyriQueueManager;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.settings.HyriSettingsLevel;
import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.hyrame.item.IHyriItemManager;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.title.Title;
import fr.hyriode.hyrame.utils.PlayerUtil;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.item.hotbar.*;
import fr.hyriode.lobby.item.queue.LeaveQueueItem;
import fr.hyriode.lobby.jump.LobbyJump;
import fr.hyriode.lobby.jump.item.LobbyJumpCheckPointItem;
import fr.hyriode.lobby.jump.item.LobbyJumpLeaveItem;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.scoreboard.LobbyScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 24/04/2022 at 21:48
 */
public class LobbyPlayer {

    private final UUID uuid;
    private final HyriLobby plugin;

    private boolean inPvp;

    private LobbyJump jump;

    private LobbyScoreboard lobbyScoreboard;

    public LobbyPlayer(UUID uuid, HyriLobby plugin) {
        this.uuid = uuid;
        this.plugin = plugin;
    }

    public void handleLogin(boolean login, boolean teleport) {
        final Player player = this.asPlayer();
        final IHyriPlayer account = this.asHyriPlayer();

        if (!account.isInModerationMode()) {
            HyriAPI.get().getServer().addPlayerPlaying(player.getUniqueId());
        }

        PlayerUtil.resetPlayer(player, true);

        player.setLevel(account.getNetworkLeveling().getLevel());
        player.setExp(this.getExp());
        player.getInventory().setArmorContents(null);
        player.setGameMode(GameMode.ADVENTURE);

        if (account.getRank().isSuperior(HyriPlayerRankType.VIP_PLUS) || account.getRank().isStaff()) {
            player.setAllowFlight(true);
        }

        if (teleport) {
            this.teleportToSpawn();
        }

        if (login) {
            this.initPlayersVisibility(account.getSettings().getPlayersVisibilityLevel(), false);
            this.setupScoreboard();

            this.sendJoinTitle();
            this.sendJoinMessage();
        }

        this.giveDefaultItems();

        this.inPvp = false;
        this.jump = null;
    }

    public void handleLogout() {
        this.leaveJump(false);

        HyriAPI.get().getServer().removePlayerPlaying(this.uuid);
    }

    public void startJump() {
        final Player player = this.asPlayer();
        final LobbyJump jump = new LobbyJump(this.plugin);
        final IHyriItemManager itemManager = this.plugin.getHyrame().getItemManager();

        this.setJump(jump);

        jump.setActualCheckPoint(jump.getStart());

        player.getInventory().clear();
        player.setAllowFlight(false);
        player.setFlying(false);

        itemManager.giveItem(player, 3, LobbyJumpCheckPointItem.class);
        itemManager.giveItem(player, 5, LobbyJumpLeaveItem.class);

        player.sendMessage(jump.getPrefix(player) + LobbyMessage.JUMP_JOIN_MESSAGE.asString(player));

        jump.getTimer().setOnTimeChanged(aLong -> {
            final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            final String line = format.format(aLong * 1000);

            new ActionBar(jump.getPrefix(player) + ChatColor.AQUA + line).send(player);
        });

        player.getInventory().setHeldItemSlot(2);
    }

    public void endJump() {
        final Player player = this.asPlayer();
        final long time = System.currentTimeMillis() - jump.getStartTime();
        final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        final DecimalFormat decimal = new DecimalFormat("000");

        player.sendMessage(this.jump.getPrefix(player) + LobbyMessage.JUMP_SUCCESS_ALL.asString(player).replace("%time%", format.format(time) + "." + decimal.format(time % 1000)));

        this.jump.getTimer().cancel();
        this.jump.setActualCheckPoint(null);

        this.jump = null;

        this.plugin.getLeaderboardManager().getJumpLeaderboard().updatePlayerScore(this.uuid, time);

        this.handleLogin(false, false);

        player.getInventory().setHeldItemSlot(2);
    }

    public void resetTimer() {
        final Player player = this.asPlayer();

        this.jump.getTimer().setCurrentTime(0);

        player.sendMessage(this.jump.getPrefix(player) + LobbyMessage.JUMP_RESET.asString(player));
    }

    public void leaveJump(boolean teleport) {
        if (!this.hasJump()) {
            return;
        }

        final Player player = this.asPlayer();

        player.sendMessage(this.jump.getPrefix(player) + LobbyMessage.JUMP_LEAVE_MESSAGE.asString(player));

        this.jump.getTimer().cancel();
        this.jump.setActualCheckPoint(null);
        this.jump = null;

        this.handleLogin(false, false);

        player.getInventory().setHeldItemSlot(2);

        if (teleport) {
            player.teleport(this.plugin.config().getJumpLocation().asBukkit());
        }
    }

    public void setInPvP(boolean inPvp) {
        if (inPvp) {
            final Player player = this.asPlayer();

            final ItemStack sword = new ItemBuilder(Material.STONE_SWORD).unbreakable().build();
            final ItemStack apple = new ItemBuilder(Material.GOLDEN_APPLE, 3).build();
            final ItemStack chestplate = new ItemBuilder(Material.IRON_CHESTPLATE).unbreakable().build();
            final ItemStack leggings = new ItemBuilder(Material.IRON_LEGGINGS).unbreakable().build();
            final ItemStack boots = new ItemBuilder(Material.IRON_BOOTS).unbreakable().build();

            player.getInventory().clear();
            player.setAllowFlight(false);

            player.getInventory().setChestplate(chestplate);
            player.getInventory().setLeggings(leggings);
            player.getInventory().setBoots(boots);

            player.getInventory().addItem(sword, apple);
            player.getInventory().setHeldItemSlot(0);

            player.closeInventory();
        }

        this.initPlayersVisibility(this.asHyriPlayer().getSettings().getPlayersVisibilityLevel(), inPvp);

        this.inPvp = inPvp;
    }

    public void giveDefaultItems() {
        final Player player = this.asPlayer();
        final IHyriPlayer account = IHyriPlayer.get(this.uuid);
        final IHyriParty party = account.hasParty() ? HyriAPI.get().getPartyManager().getParty(account.getParty()) : null;
        final IHyriItemManager item = this.plugin.getHyrame().getItemManager();
        final IHyriQueueManager queueManager = HyriAPI.get().getQueueManager();

        player.getInventory().clear();

        if (queueManager.getPlayerQueue(player.getUniqueId()) != null || (party != null && party.isLeader(this.uuid) && queueManager.getPartyQueue(party.getId()) != null)) {
            item.giveItem(player, 0, LeaveQueueItem.class);
        } else {
            item.giveItem(player, 0, GameSelectorItem.class);
        }

        item.giveItem(player, 1, PlayerProfileItem.class);
        item.giveItem(player, 4, StoreItem.class);
        item.giveItem(player, 7, SettingsItem.class);
        item.giveItem(player, 8, LobbySelectorItem.class);

        player.getInventory().setHeldItemSlot(0);
    }

    public void teleportToSpawn() {
        this.asPlayer().teleport(this.plugin.config().getSpawnLocation().asBukkit());
    }

    public void initPlayersVisibility(HyriSettingsLevel level, boolean showAll) {
        final Player player = this.asPlayer();
        final IHyriPlayer account = this.asHyriPlayer();
        final IHyriFriendHandler handler = HyriAPI.get().getFriendManager().createHandler(player.getUniqueId());
        final IHyriParty party = account.hasParty() ? HyriAPI.get().getPartyManager().getParty(account.getParty()) : null;

        for (Player target : Bukkit.getOnlinePlayers()) {
            final UUID targetId = target.getUniqueId();
            final IHyriPlayer targetAccount = IHyriPlayer.get(targetId);

            if (level == HyriSettingsLevel.ALL || targetAccount.getRank().isStaff() || showAll) {
                player.showPlayer(target);
                continue;
            }

            if (level == HyriSettingsLevel.NONE) {
                player.hidePlayer(target);
                continue;
            }

            if (level == HyriSettingsLevel.FRIENDS) {
                if (handler.areFriends(targetId)) {
                    player.showPlayer(target);
                } else {
                    player.hidePlayer(target);
                }
                continue;
            }

            if (level == HyriSettingsLevel.PARTY) {
                if (party != null && party.hasMember(targetId)) {
                    player.showPlayer(target);
                } else {
                    player.hidePlayer(target);
                }
            }
        }
    }

    private void setupScoreboard() {
        this.lobbyScoreboard = new LobbyScoreboard(this.plugin, this.asPlayer());
        this.lobbyScoreboard.show();
    }

    private void sendJoinMessage() {
        final IHyriPlayer account = this.asHyriPlayer();

        LobbyMessage.JOIN_MESSAGE.sendTo(this.asPlayer());

        if (account.isInVanishMode() || account.isInModerationMode()) {
            return;
        }

        if (account.getRank().isSuperior(HyriPlayerRankType.VIP_PLUS) || account.getRank().isStaff() || (account.hasNickname() && account.getNickname().getRank().getId() >= HyriPlayerRankType.VIP_PLUS.getId())) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                target.sendMessage(LobbyMessage.JOIN_VIP_MESSAGE.asString(target).replace("%player%", account.getNameWithRank(true)));
            }
        }
    }

    private void sendJoinTitle() {
        final IHyriPlayer account = this.asHyriPlayer();
        final String basicTitle = ChatColor.AQUA + "»" + ChatColor.DARK_AQUA + " " + HyriConstants.SERVER_NAME + " " + ChatColor.AQUA + "«";

        if (account.getLastServer() == null) {
            final Title title = new Title(basicTitle, LobbyMessage.BASIC_JOIN_SUBTITLE.asString(account), 20, 3 * 20, 20);

            if (account.getPlayTime() == 0) {
                title.setSubTitle(LobbyMessage.FIRST_JOIN_SUBTITLE.asString(account));
            }

            Title.sendTitle(this.asPlayer(), title);
        }
    }

    private float getExp() {
        final IHyriLeveling leveling = this.asHyriPlayer().getNetworkLeveling();
        final double currentExperience = leveling.getExperience();
        final double totalExperience = leveling.getAlgorithm().levelToExperience(leveling.getLevel() + 1);

        return (float) (currentExperience / totalExperience);
    }

    public LobbyScoreboard getLobbyScoreboard() {
        return this.lobbyScoreboard;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public Player asPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public IHyriPlayer asHyriPlayer() {
        return IHyriPlayer.get(this.uuid);
    }

    public boolean isInPvp() {
        return this.inPvp;
    }

    public boolean hasJump() {
        return this.jump != null;
    }

    public LobbyJump getJump() {
        return jump;
    }

    public void setJump(LobbyJump jump) {
        this.jump = jump;
    }

}
