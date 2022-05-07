package fr.hyriode.hyrilobby.player;


import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.HyriConstants;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.item.IHyriItemManager;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.title.Title;
import fr.hyriode.hyrame.utils.PlayerUtil;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.item.hotbar.*;
import fr.hyriode.hyrilobby.jump.Jump;
import fr.hyriode.hyrilobby.language.LobbyMessage;
import fr.hyriode.hyrilobby.scoreboard.LobbyScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 24/04/2022 at 21:48
 */
public class LobbyPlayer {

    private final UUID uuid;
    private final HyriLobby plugin;

    private boolean inJump;
    private boolean inPvp;

    private Jump jump;
    private Jump.CheckPoint lastCheckpoint;

    private LobbyScoreboard lobbyScoreboard;

    public LobbyPlayer(UUID uuid, HyriLobby plugin) {
        this.uuid = uuid;
        this.plugin = plugin;
        this.inJump = false;
        this.inPvp = false;
    }

    public void handleLogin(boolean login) {

        PlayerUtil.resetPlayer(this.asPlayer(), true);
        this.asPlayer().getInventory().setArmorContents(null);
        this.asPlayer().setGameMode(GameMode.ADVENTURE);

        if (this.asHyriPlayer().getRank().isSuperior(HyriPlayerRankType.VIP_PLUS) || this.asHyriPlayer().getRank().isStaff()) {
            this.asPlayer().setAllowFlight(true);
        }

        this.teleportToSpawn();

        if (login) {
            this.setupScoreboard();

            this.sendJoinTitle();
            this.sendJoinMessage();
        }

        this.giveItems();

        this.inPvp = false;
        this.inJump = false;
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
        }

        this.inPvp = inPvp;
    }


    public void setInJump(boolean inJump) {
        if(inJump) {
            this.asPlayer().setAllowFlight(false);


        } else {
            this.giveItems();
            this.asPlayer().teleport(this.plugin.getConfiguration().getJumpLocation().asBukkit());
            if (this.asHyriPlayer().getRank().isSuperior(HyriPlayerRankType.VIP_PLUS) || this.asHyriPlayer().getRank().isStaff()) {
                this.asPlayer().setAllowFlight(true);
            }
            this.jump = null;
            this.lastCheckpoint = null;
        }

        this.inJump = inJump;
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
                    onlinePlayer.sendMessage(LobbyMessage.JOIN_MESSAGE.getMessage().getForPlayer(onlinePlayer)
                            .replace("%player%", this.asHyriPlayer().getNameWithRank()));
                }
            }
        } else {
            if(this.asHyriPlayer().getNickname().getRank().getId() >= HyriPlayerRankType.VIP_PLUS.getId()) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendMessage(LobbyMessage.JOIN_MESSAGE.getMessage().getForPlayer(onlinePlayer)
                            .replace("%player%", this.asHyriPlayer().getNameWithRank(true)));
                }
            }
        }
    }

    private void sendJoinTitle() {
        //  final Date today = Date.from(Instant.now());
        final String basicTitle = ChatColor.AQUA + "»" + ChatColor.DARK_AQUA + " " + HyriConstants.SERVER_NAME + " " + ChatColor.AQUA + "«";

        if (this.asHyriPlayer().getLastServer() == null) {
            Title title = new Title(
                    basicTitle,
                    LobbyMessage.BASIC_SUBTITLE.getSubTitle().getForPlayer(this.asHyriPlayer()),
                    20,
                    3 * 20,
                    20);

            if (this.asHyriPlayer().getPlayTime() == 0) {
                title.setSubTitle(LobbyMessage.FIRST_SUBTITLE.getSubTitle().getForPlayer(this.asHyriPlayer()));
            }

        /*
        if (this.asHyriPlayer().getHyriPlus().getPurchaseDate().equals(today)) {
            title.setSubTitle(LobbyMessage.BUY_TITLE.getSubTitle().getForPlayer(this.asHyriPlayer()));
        }
         */

            Title.sendTitle(this.asPlayer(), title);
        }
    }

    public void handleLogout() {

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

    public boolean isInJump() {
        return inJump;
    }

    public boolean isInPvp() {
        return this.inPvp;
    }

    public Jump getJump() {
        return jump;
    }

    public void setJump(Jump jump) {
        this.jump = jump;
    }

    public Jump.CheckPoint getLastCheckpoint() {
        return lastCheckpoint;
    }

    public void setLastCheckpoint(Jump.CheckPoint lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }
}
