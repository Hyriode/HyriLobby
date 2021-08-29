package fr.hyriode.hyrilobby.player;

import fr.hyriode.common.title.Title;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.rank.HyriRank;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.util.References;
import fr.hyriode.hyrilobby.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class PlayerManager {

    private HyriLobby lobby;
    private static HashSet<PlayerManager> players = new HashSet<>();
    private UUID uuid;
    private String name;
    private IHyriPlayer player;
    private HyriRank rank;

    public PlayerManager(Player player, HyriLobby hyriLobby) {
        this.lobby = hyriLobby;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.player = lobby.getAPI().getPlayerManager().getPlayer(player.getUniqueId());
        this.rank = lobby.getAPI().getPlayerManager().getPlayer(player.getUniqueId()).getRank();
    }

    public static PlayerManager getByUuid(UUID uuid) {
        return players.stream().filter(p -> p.getUuid().compareTo(uuid) == 0).findFirst().orElse(null);
    }

    public void onLogin() {
        players.add(this);
        List<String> messages = new ArrayList<>();
        messages.add(ChatColor.GRAY + "Site et Forum: " + ChatColor.AQUA + "www.hyriode.fr");
        messages.add(ChatColor.GRAY + "Boutique du serveur: " + ChatColor.GOLD + "store.hyriode.fr");
        messages.add(ChatColor.GRAY + "Notre discord " + ChatColor.BLUE + "discord.hyriode.fr");
        Player p = Bukkit.getPlayer(uuid);
        Title.setTitle(p, ChatColor.AQUA + "»" + ChatColor.DARK_AQUA + " " + References.SERVER_NAME + " " + ChatColor.AQUA + "«", ChatColor.GRAY + "Bienvenue sur " + ChatColor.AQUA + References.SERVER_IP + ChatColor.GRAY + " !", 40, 60, 20);
        p.playSound(p.getLocation(), Sound.LEVEL_UP, 5f, 5f);
        if (p.isOp()) {
            Bukkit.broadcastMessage(ChatColor.RED + player.getDisplayName() + ChatColor.GOLD + " a rejoint le lobby !");
        }
        Utils.sendActionBar(lobby, p, messages, 5);
    }

    public void onLogout() {
        players.remove(this);
    }

    public static void onDisable() {
        players.clear();
    }

    public static HashSet<PlayerManager> getPlayers() {
        return players;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public IHyriPlayer getPlayer() {
        return player;
    }

    public HyriRank getRank() {
        return rank;
    }
}
