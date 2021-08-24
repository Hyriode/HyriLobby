package fr.hyriode.hyrilobby.events;

import fr.hyriode.hyrilobby.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private Main main;

    public PlayerQuitListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        main.getScoreBoardManager().onLeave(player);
        e.setQuitMessage(null);
    }
}
