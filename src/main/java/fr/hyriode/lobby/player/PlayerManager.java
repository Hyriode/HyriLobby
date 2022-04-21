package fr.hyriode.lobby.player;

import fr.hyriode.api.HyriConstants;
import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.hyrame.title.Title;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.player.LobbyPlayer;
import fr.hyriode.lobby.api.player.LobbyPlayerManager;
import fr.hyriode.lobby.utils.Language;
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

    public PlayerManager(HyriLobby plugin) {
        this.plugin = plugin;
        this.pm = () -> LobbyAPI.get().getPlayerManager();
    }

    public void onLogin(Player player) {
        if (this.pm.get().get(player.getUniqueId().toString()) == null) {
            this.pm.get().save(new LobbyPlayer(player.getUniqueId(), player.getName()), player.getUniqueId().toString());
        }

        this.addMessages(player);

        Title.sendTitle(player, ChatColor.AQUA + "»" + ChatColor.DARK_AQUA + " " + HyriConstants.SERVER_NAME + " " + ChatColor.AQUA + "«",
                Language.getMessage(player, "title.welcome.welcome"), 12, 30, 12);
        this.sendActionBar(this.plugin, player);

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 5f, 5f);
    }

    public void onLogout(Player player) {
        final LobbyPlayer lp = this.pm.get().get(player.getUniqueId().toString());

        lp.setLastCheckpoint(-1);
        this.pm.get().save(lp, lp.getUniqueId().toString());
    }

    private void addMessages(Player player) {
        MESSAGES.clear();
        MESSAGES.add(Language.getMessage(player, "title.welcome.website"));
        MESSAGES.add(Language.getMessage(player, "title.welcome.shop"));
        MESSAGES.add(Language.getMessage(player, "title.welcome.discord"));
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
