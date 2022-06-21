package fr.hyriode.lobby.player;


import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.HyriConstants;
import fr.hyriode.api.leveling.IHyriLeveling;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.hyrame.item.IHyriItemManager;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.title.Title;
import fr.hyriode.hyrame.utils.PlayerUtil;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.item.hotbar.*;
import fr.hyriode.lobby.jump.LobbyJump;
import fr.hyriode.lobby.jump.item.LobbyJumpCheckPointItem;
import fr.hyriode.lobby.jump.item.LobbyJumpLeaveItem;
import fr.hyriode.lobby.jump.item.LobbyJumpResetItem;
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
            this.setupScoreboard();

            this.sendJoinTitle();
            this.sendJoinMessage();
        }

        this.giveItems();

        this.inPvp = false;
        this.jump = null;
    }

    private float getExp() {
        final IHyriLeveling leveling = this.asHyriPlayer().getNetworkLeveling();
        final IHyriLeveling.Algorithm algorithm = leveling.getAlgorithm();

        final double totalExperience = algorithm.levelToExperience(leveling.getLevel() + 1);
        final double neededExperience = algorithm.levelToExperience(leveling.getLevel() + 1) - algorithm.levelToExperience(leveling.getLevel());

        return (float) (neededExperience / totalExperience);
    }

    public void startJump() {
        final Player player = this.asPlayer();
        final LobbyJump jump = new LobbyJump(this.plugin);
        final IHyriItemManager itemManager = this.plugin.getHyrame().getItemManager();

        this.setJump(jump);

        jump.setActualCheckPoint(this.getJump().getStart());

        player.getInventory().clear();
        player.setAllowFlight(false);
        player.setFlying(false);

        itemManager.giveItem(player, 2, LobbyJumpCheckPointItem.class);
        itemManager.giveItem(player, 4, LobbyJumpResetItem.class);
        itemManager.giveItem(player, 6, LobbyJumpLeaveItem.class);

        player.sendMessage(jump.getPrefix(player) + LobbyMessage.JUMP_JOIN_MESSAGE.asLang().getForPlayer(player));

        jump.getTimer().setOnTimeChanged(aLong -> {
            final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            final String line = format.format(aLong * 1000);

            new ActionBar(jump.getPrefix(this.asPlayer()) + ChatColor.AQUA + line).send(asPlayer());
        });

        player.getInventory().setHeldItemSlot(2);
    }

    public void resetJump() {
        final LobbyJump jump = this.jump;
        final Player player = this.asPlayer();

        jump.setActualCheckPoint(jump.getStart());
        player.teleport(jump.getStart().getLocation());
        this.resetTimer();
    }

    public void endJump() {
        final Player player = this.asPlayer();
        final double time = System.currentTimeMillis() - jump.getStartTime();
        final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        final DecimalFormat decimal = new DecimalFormat("000");

        this.asPlayer().sendMessage(this.jump.getPrefix(this.asPlayer()) + LobbyMessage.JUMP_SUCCESS_ALL.asLang().getForPlayer(player)
                .replace("%time%", format.format(time) + "." + decimal.format(time % 1000))
        );

        this.jump.getTimer().cancel();
        this.jump.setActualCheckPoint(null);

        this.jump = null;

        this.handleLogin(false, false);

        player.getInventory().setHeldItemSlot(2);
    }

    public void resetTimer() {
        final Player player = this.asPlayer();

        this.jump.getTimer().setCurrentTime(0);

        player.sendMessage(LobbyMessage.JUMP_RESET.asLang().getForPlayer(player));
    }

    public void leaveJump(boolean teleport) {
        final Player player = this.asPlayer();

        player.sendMessage(this.jump.getPrefix(this.asPlayer()) + LobbyMessage.JUMP_LEAVE_MESSAGE.asLang().getForPlayer(this.asHyriPlayer()));

        this.jump.getTimer().cancel();
        this.jump.setActualCheckPoint(null);
        this.jump = null;

        this.handleLogin(false, false);

        player.getInventory().setHeldItemSlot(2);

        if (teleport) {
            player.teleport(this.plugin.getConfiguration().getJumpLocation().asBukkit());
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

        this.inPvp = inPvp;
    }

    public void giveItems() {
        final Player player = this.asPlayer();
        final IHyriItemManager item = this.plugin.getHyrame().getItemManager();

        player.getInventory().clear();

        item.giveItem(player, 0, GameChooserItem.class);
        item.giveItem(player, 1, PlayerProfileItem.class);
        item.giveItem(player, 4, ShopItem.class);
        item.giveItem(player, 7, SettingsItem.class);
        item.giveItem(player, 8, LobbySelectorItem.class);

        player.getInventory().setHeldItemSlot(0);
    }

    public void teleportToSpawn() {
        this.asPlayer().teleport(this.plugin.getConfiguration().getSpawnLocation().asBukkit());
    }

    private void setupScoreboard() {
        this.lobbyScoreboard = new LobbyScoreboard(this.plugin, this.asPlayer());
        this.lobbyScoreboard.show();
    }

    private void sendJoinMessage() {
        final IHyriPlayer account = this.asHyriPlayer();

        if(!account.hasNickname()) {
            if (account.getRank().isSuperior(HyriPlayerRankType.VIP_PLUS) || account.getRank().isStaff()) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage(LobbyMessage.JOIN_MESSAGE.asLang().getForPlayer(onlinePlayer)
                            .replace("%player%", account.getNameWithRank()));
                }
            }
        } else {
            if(account.getNickname().getRank().getId() >= HyriPlayerRankType.VIP_PLUS.getId()) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage(LobbyMessage.JOIN_MESSAGE.asLang().getForPlayer(onlinePlayer)
                            .replace("%player%", account.getNameWithRank(true)));
                }
            }
        }
    }

    private void sendJoinTitle() {
        final String basicTitle = ChatColor.AQUA + "»" + ChatColor.DARK_AQUA + " " + HyriConstants.SERVER_NAME + " " + ChatColor.AQUA + "«";

        if (this.asHyriPlayer().getLastServer() == null) {
            Title title = new Title(
                    basicTitle,
                    LobbyMessage.BASIC_JOIN_SUBTITLE.asLang().getForPlayer(this.asHyriPlayer()),
                    20,
                    3 * 20,
                    20);

            if (this.asHyriPlayer().getPlayTime() == 0) {
                title.setSubTitle(LobbyMessage.FIRST_JOIN_SUBTITLE.asLang().getForPlayer(this.asHyriPlayer()));
            }


            Title.sendTitle(this.asPlayer(), title);
        }
    }

    public void handleLogout() {
        this.leaveJump(false);
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
        return HyriAPI.get().getPlayerManager().getPlayer(this.uuid);
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
