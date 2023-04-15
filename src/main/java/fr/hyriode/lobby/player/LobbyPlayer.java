package fr.hyriode.lobby.player;


import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.HyriConstants;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.player.model.SettingsLevel;
import fr.hyriode.api.queue.IHyriQueueManager;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.hyrame.item.IItemManager;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.title.Title;
import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.hyrame.utils.PlayerUtil;
import fr.hyriode.hyrame.utils.TimeUtil;
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

    private boolean queue = false;

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

        if (teleport) {
            this.teleportToSpawn();
        }

        if (IHyriPlayerSession.get(this.uuid).isModerating()) {
            if (login) {
                this.setupScoreboard();
            }
            return;
        }

        HyriAPI.get().getServer().addPlayerPlaying(player.getUniqueId());

        PlayerUtil.resetPlayer(player, true);

        player.setLevel(account.getNetworkLeveling().getLevel());
        player.setExp(this.getExp(account.getNetworkLeveling()));
        player.getInventory().setArmorContents(null);
        player.setGameMode(GameMode.ADVENTURE);

        if (account.getRank().isSuperior(PlayerRank.VIP_PLUS) || account.getRank().isStaff()) {
            player.setAllowFlight(true);
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
        if (IHyriPlayerSession.get(this.uuid).isModerating()) {
            return;
        }

        final Player player = this.asPlayer();
        final LobbyJump jump = new LobbyJump(this.plugin);
        final IItemManager itemManager = this.plugin.getHyrame().getItemManager();

        this.setJump(jump);

        jump.setActualCheckPoint(jump.getStart());

        player.getInventory().clear();
        player.setAllowFlight(false);
        player.setFlying(false);

        itemManager.giveItem(player, 3, LobbyJumpCheckPointItem.class);
        itemManager.giveItem(player, 5, LobbyJumpLeaveItem.class);

        player.sendMessage(LobbyMessage.JUMP_JOIN_MESSAGE.asString(player));

        jump.getTimer().onChanged(time -> new ActionBar(LobbyMessage.JUMP_TIME_BAR.asString(player).replace("%time%", TimeUtil.formatTime(time))).send(player));

        player.getInventory().setHeldItemSlot(2);
    }

    public void endJump() {
        if (IHyriPlayerSession.get(this.uuid).isModerating()) {
            return;
        }

        final Player player = this.asPlayer();
        final long time = System.currentTimeMillis() - this.jump.getStartTime();
        final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        final DecimalFormat decimal = new DecimalFormat("000");

        player.sendMessage(LobbyMessage.JUMP_SUCCESS_ALL_MESSAGE.asString(player).replace("%time%", format.format(time) + "." + decimal.format(time % 1000)));

        this.jump.getTimer().cancel();
        this.jump.setActualCheckPoint(null);

        this.jump = null;

        this.plugin.getLeaderboardManager().getJumpLeaderboard().updatePlayerScore(this.uuid, time);

        this.handleLogin(false, false);
    }

    public void resetTimer() {
        final Player player = this.asPlayer();

        this.jump.getTimer().setCurrentTime(0);

        player.sendMessage(LobbyMessage.JUMP_RESET_MESSAGE.asString(player));
    }

    public void leaveJump(boolean teleport) {
        if (!this.hasJump()) {
            return;
        }

        final Player player = this.asPlayer();

        player.sendMessage(LobbyMessage.JUMP_LEAVE_MESSAGE.asString(player));

        this.leaveJump0();

        this.handleLogin(false, false);

        player.getInventory().setHeldItemSlot(0);

        if (teleport) {
            player.teleport(this.plugin.config().getJumpLocation().asBukkit());
        }
    }

    public void leaveJump0() {
        this.jump.getTimer().cancel();
        this.jump.setActualCheckPoint(null);
        this.jump = null;
    }

    public void setInPvP(boolean inPvp) {
        final IHyriPlayerSession session = IHyriPlayerSession.get(this.uuid);

        if (session.isModerating() || session.isVanished()) {
            return;
        }

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
        final IHyriPlayerSession session = IHyriPlayerSession.get(this.uuid);
        final IItemManager item = this.plugin.getHyrame().getItemManager();
        final IHyriQueueManager queueManager = HyriAPI.get().getQueueManager();

        if (session.isModerating()) {
            return;
        }

        player.getInventory().clear();

        if (session.getQueue() != null && queueManager.getQueue(session.getQueue()) != null) {
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

    public void initPlayersVisibility(SettingsLevel level, boolean showAll) {
        final Player player = this.asPlayer();
        final IHyriPlayerSession session = IHyriPlayerSession.get(this.uuid);
        final IHyriParty party = session.hasParty() ? IHyriParty.get(session.getParty()) : null;

        for (Player target : Bukkit.getOnlinePlayers()) {
            final UUID targetId = target.getUniqueId();
            final IHyriPlayer targetAccount = IHyriPlayer.get(targetId);

            if (level == SettingsLevel.ALL || targetAccount.getRank().isStaff() || showAll) {
                player.showPlayer(target);
                continue;
            }

            if (level == SettingsLevel.NONE) {
                player.hidePlayer(target);
                continue;
            }

            if (level == SettingsLevel.FRIENDS) {
                if (targetAccount.getFriends().has(this.uuid)) {
                    player.showPlayer(target);
                } else {
                    player.hidePlayer(target);
                }
                continue;
            }

            if (level == SettingsLevel.PARTY) {
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
        final IHyriPlayerSession session = IHyriPlayerSession.get(this.uuid);

        LobbyMessage.JOIN_MESSAGE.sendTo(this.asPlayer());

        if (session.isVanished() || session.isModerating() || session.getNickname().has()) {
            return;
        }

        if (account.getRank().isSuperior(PlayerRank.VIP_PLUS) || account.getRank().isStaff() || (session.getNickname().has() && session.getNickname().getRank().getId() >= PlayerRank.VIP_PLUS.getId())) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                target.sendMessage(LobbyMessage.JOIN_VIP_MESSAGE.asString(target).replace("%player%", account.getNameWithRank()));
            }
        }
    }

    private void sendJoinTitle() {
        final IHyriPlayerSession session = IHyriPlayerSession.get(this.uuid);
        final IHyriPlayer account = this.asHyriPlayer();
        final String basicTitle = ChatColor.AQUA + "»" + ChatColor.DARK_AQUA + " " + HyriConstants.SERVER_NAME + " " + ChatColor.AQUA + "«";

        if (session.getLastServer() == null) {
            final Title title = new Title(basicTitle, LobbyMessage.BASIC_JOIN_SUBTITLE.asString(account), 20, 3 * 20, 20);

            if (account.getStatistics().getTotalPlayTime() == 0) {
                title.setSubTitle(LobbyMessage.FIRST_JOIN_SUBTITLE.asString(account));
            }

            Title.sendTitle(this.asPlayer(), title);
        }
    }

    private float getExp(IHyriLeveling leveling) {
        final double currentLevelExperience = leveling.getAlgorithm().levelToExperience(leveling.getLevel());
        final double currentExperience = leveling.getExperience() - currentLevelExperience;
        final double totalExperience = leveling.getAlgorithm().levelToExperience(leveling.getLevel() + 1) - currentLevelExperience;

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
