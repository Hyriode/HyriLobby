package fr.hyriode.lobby.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.HyriConstants;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.hyrame.title.Title;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.api.player.LobbyPlayer;
import fr.hyriode.lobby.utils.Language;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {

    private static final List<String> MESSAGES = new ArrayList<>();

    private final HyriLobby plugin;

    public PlayerManager(HyriLobby plugin) {
        this.plugin = plugin;
    }

    public void onLogin(Player player) {
        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId());
        this.addMessages(player);

        this.teleportToSpawn(player);

        Title.sendTitle(player, ChatColor.AQUA + "»" + ChatColor.DARK_AQUA + " " + HyriConstants.SERVER_NAME + " " + ChatColor.AQUA + "«", Language.getMessage(player, "title.welcome.welcome"), 12, 30, 12);

        this.sendActionBar(this.plugin, player);

        player.setLevel(account.getNetworkLeveling().getLevel());
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 5f, 5f);

        if (account.getRank().isStaff() || account.getRank().isSuperior(HyriPlayerRankType.VIP_PLUS)) {
            player.setAllowFlight(true);
        }
    }

    public void onLogout(Player player) {
        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId());
        final LobbyPlayer lp = LobbyPlayer.get(account);

        lp.setLastCheckpoint(-1);
        lp.update(account);
        account.update();
    }

    public void teleportToSpawn(Player player) {
        player.teleport(new Location(IHyrame.WORLD.get(), 0.5, 190, 0.5, 90, 0));
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
