package fr.hyriode.lobby.player;

import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.hyrame.title.Title;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.api.player.LobbyPlayerManager;
import fr.hyriode.lobby.util.References;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PlayerManager {

    private static final List<String> MESSAGES = new ArrayList<>();

    private final HyriLobby plugin;
    private final Supplier<LobbyPlayerManager> pm;
    private final Supplier<IHyriLanguageManager> lang;

    public PlayerManager(HyriLobby plugin) {
        this.plugin = plugin;
        this.pm = () -> plugin.getLobbyAPI().getPlayerManager();
        this.lang = () -> plugin.getHyrame().getLanguageManager();
    }

    public void onLogin(Player player) {
        if (this.pm.get().getPlayer(player.getUniqueId()) == null) {
            this.pm.get().createPlayer(player.getUniqueId());
        }

        this.addMessages(player);

        Title.sendTitle(player, ChatColor.AQUA + "»" + ChatColor.DARK_AQUA + " " + References.SERVER_NAME + " " + ChatColor.AQUA + "«",
                ChatColor.GRAY + this.lang.get().getValue(player, "title.welcome.welcome") + " " + ChatColor.AQUA + References.SERVER_IP + ChatColor.GRAY + " !",
                12, 30, 12);
        this.sendActionBar(this.plugin, player);

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 5f, 5f);
    }

    private void addMessages(Player player) {
        MESSAGES.clear();
        MESSAGES.add(ChatColor.GRAY + this.lang.get().getValue(player, "title.welcome.website") + ": " + ChatColor.AQUA + References.WEBSITE_URL);
        MESSAGES.add(ChatColor.GRAY + this.lang.get().getValue(player, "title.welcome.shop") + ": " + ChatColor.GOLD + References.STORE_WEBSITE_URL);
        MESSAGES.add(ChatColor.GRAY + this.lang.get().getValue(player, "title.welcome.discord") + ": " + ChatColor.BLUE + References.DISCORD_URL);
    }

    private void sendActionBar(HyriLobby plugin, Player player) {
        new BukkitRunnable() {
            final ActionBar bar = new ActionBar(MESSAGES.get(0));
            int i = 0;
            @Override
            public void run() {
                bar.setMessage(MESSAGES.get(i));
                bar.sendPermanent(plugin, player);
                i++;

                if (i == MESSAGES.size()) {
                    i = 0;
                }
            }
        }.runTaskTimer(this.plugin, 0, 100);
    }
}
