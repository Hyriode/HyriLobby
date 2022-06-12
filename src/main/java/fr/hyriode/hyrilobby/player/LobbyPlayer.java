package fr.hyriode.hyrilobby.player;


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
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.item.hotbar.*;
import fr.hyriode.hyrilobby.jump.LobbyJump;
import fr.hyriode.hyrilobby.jump.LobbyJumpCheckPoint;
import fr.hyriode.hyrilobby.jump.item.LobbyJumpCheckPointItem;
import fr.hyriode.hyrilobby.jump.item.LobbyJumpLeaveItem;
import fr.hyriode.hyrilobby.jump.item.LobbyJumpResetItem;
import fr.hyriode.hyrilobby.language.LobbyMessage;
import fr.hyriode.hyrilobby.scoreboard.LobbyScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Set;
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

        PlayerUtil.resetPlayer(this.asPlayer(), true);

        this.asPlayer().setLevel(this.asHyriPlayer().getNetworkLeveling().getLevel());
        this.asPlayer().setExp(this.getExp());

        this.asPlayer().getInventory().setArmorContents(null);
        this.asPlayer().setGameMode(GameMode.ADVENTURE);

        if (this.asHyriPlayer().getRank().isSuperior(HyriPlayerRankType.VIP_PLUS) || this.asHyriPlayer().getRank().isStaff()) {
            this.asPlayer().setAllowFlight(true);
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
        final LobbyJump jump = new LobbyJump(this.plugin);
        final IHyriItemManager itemManager = this.plugin.getHyrame().getItemManager();

        this.setJump(jump);
        this.getJump().setActualCheckPoint(this.getJump().getStart());

        this.asPlayer().getInventory().clear();
        this.asPlayer().setAllowFlight(false);
        this.asPlayer().setFlying(false);

        itemManager.giveItem(this.asPlayer(), 2, LobbyJumpCheckPointItem.class);
        itemManager.giveItem(this.asPlayer(), 4, LobbyJumpResetItem.class);
        itemManager.giveItem(this.asPlayer(), 6, LobbyJumpLeaveItem.class);

        this.asPlayer().sendMessage(jump.getPrefix(this.asPlayer()) + LobbyMessage.JUMP_JOIN_MESSAGE.get().getForPlayer(this.asPlayer()));
        this.getJump().getTimer().setOnTimeChanged(aLong -> {
            final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            final String line = format.format(aLong * 1000);

            new ActionBar(jump.getPrefix(this.asPlayer()) + ChatColor.AQUA + line).send(asPlayer());
        });
        this.asPlayer().getInventory().setHeldItemSlot(2);
    }

    public void resetJump() {
        final LobbyJump jump = this.jump;
        final Player player = this.asPlayer();

        jump.setActualCheckPoint(jump.getStart());
        player.teleport(jump.getStart().getLocation());
        this.resetTimer();
    }

    public void endJump() {
        final double time = System.currentTimeMillis() - jump.getStartTime();
        final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        final DecimalFormat decimal = new DecimalFormat("000");

        this.asPlayer().sendMessage(jump.getPrefix(this.asPlayer()) + LobbyMessage.JUMP_SUCCESS_ALL.get().getForPlayer(this.asPlayer())
                .replace("%time%", format.format(time) + "." + decimal.format(time % 1000))
        );

        this.getJump().getTimer().cancel();
        this.getJump().setActualCheckPoint(null);
        this.setJump(null);

        this.handleLogin(false, false);
        this.asPlayer().getInventory().setHeldItemSlot(2);
    }

    public void resetTimer() {
        this.getJump().getTimer().setCurrentTime(0);
        this.asPlayer().sendMessage(LobbyMessage.JUMP_RESET.get().getForPlayer(this.asPlayer()));
    }

    public void leaveJump(boolean teleport) {
        this.asPlayer().sendMessage(jump.getPrefix(this.asPlayer()) + LobbyMessage.JUMP_LEAVE_MESSAGE.get().getForPlayer(this.asPlayer()));

        this.getJump().getTimer().cancel();
        this.getJump().setActualCheckPoint(null);
        this.setJump(null);

        this.handleLogin(false, false);
        this.asPlayer().getInventory().setHeldItemSlot(2);

        if(teleport) {
            this.asPlayer().teleport(this.plugin.getConfiguration().getJumpLocation().asBukkit());
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
        if(!this.asHyriPlayer().hasNickname()) {
            if (this.asHyriPlayer().getRank().isSuperior(HyriPlayerRankType.VIP_PLUS) || this.asHyriPlayer().getRank().isStaff()) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage(LobbyMessage.JOIN_MESSAGE.get().getForPlayer(onlinePlayer)
                            .replace("%player%", this.asHyriPlayer().getNameWithRank()));
                }
            }
        } else {
            if(this.asHyriPlayer().getNickname().getRank().getId() >= HyriPlayerRankType.VIP_PLUS.getId()) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage(LobbyMessage.JOIN_MESSAGE.get().getForPlayer(onlinePlayer)
                            .replace("%player%", this.asHyriPlayer().getNameWithRank(true)));
                }
            }
        }
    }

    private void sendJoinTitle() {
        final String basicTitle = ChatColor.AQUA + "»" + ChatColor.DARK_AQUA + " " + HyriConstants.SERVER_NAME + " " + ChatColor.AQUA + "«";

        if (this.asHyriPlayer().getLastServer() == null) {
            Title title = new Title(
                    basicTitle,
                    LobbyMessage.BASIC_JOIN_SUBTITLE.get().getForPlayer(this.asHyriPlayer()),
                    20,
                    3 * 20,
                    20);

            if (this.asHyriPlayer().getPlayTime() == 0) {
                title.setSubTitle(LobbyMessage.FIRST_JOIN_SUBTITLE.get().getForPlayer(this.asHyriPlayer()));
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
