package fr.hyriode.lobby.player;

import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.rank.HyriRank;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.util.References;
import fr.hyriode.lobby.util.Utils;
import fr.hyriode.tools.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class PlayerManager {

    private static final HashSet<PlayerManager> PLAYERS = new HashSet<>();

    private final UUID uuid;
    private final String name;
    private final IHyriPlayer player;
    private final HyriRank rank;

    private final HyriLobby plugin;

    public PlayerManager(Player player, HyriLobby plugin) {
        this.plugin = plugin;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.player = plugin.getAPI().getPlayerManager().getPlayer(player.getUniqueId());
        this.rank = plugin.getAPI().getPlayerManager().getPlayer(player.getUniqueId()).getRank();
    }

    public static PlayerManager getByUuid(UUID uuid) {
        return PLAYERS.stream().filter(p -> p.getUuid().compareTo(uuid) == 0).findFirst().orElse(null);
    }

    public void onLogin() {
        final List<String> messages = new ArrayList<>();
        final Player p = Bukkit.getPlayer(uuid);

        PLAYERS.add(this);

        messages.add(ChatColor.GRAY + "Site et Forum: " + ChatColor.AQUA + "www.hyriode.fr");
        messages.add(ChatColor.GRAY + "Boutique du serveur: " + ChatColor.GOLD + "store.hyriode.fr");
        messages.add(ChatColor.GRAY + "Notre discord " + ChatColor.BLUE + "discord.hyriode.fr");

        Title.sendTitle(p, ChatColor.AQUA + "»" + ChatColor.DARK_AQUA + " " + References.SERVER_NAME + " " + ChatColor.AQUA + "«", ChatColor.GRAY + "Bienvenue sur " + ChatColor.AQUA + References.SERVER_IP + ChatColor.GRAY + " !", 12, 30, 12);
        Utils.sendActionBar(plugin, p, messages, 5);

        p.playSound(p.getLocation(), Sound.LEVEL_UP, 5f, 5f);
    }

    public void onLogout() {
        PLAYERS.remove(this);
    }

    public static void onDisable() {
        PLAYERS.clear();
    }

    public static HashSet<PlayerManager> getPlayers() {
        return PLAYERS;
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
