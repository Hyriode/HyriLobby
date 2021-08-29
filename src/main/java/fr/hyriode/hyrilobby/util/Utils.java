package fr.hyriode.hyrilobby.util;

import fr.hyriode.common.chat.ActionBar;
import fr.hyriode.hyrilobby.HyriLobby;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Utils {


    public static void sendActionBar(HyriLobby lobby, Player p, List<String> messages, int stay) {
        ActionBar bar = new ActionBar(messages.stream().findFirst().get());
        new BukkitRunnable() {
            int i;

            @Override
            public void run() {
                bar.setMessage(messages.get(i));
                bar.sendPermanent(lobby, p);
                i++;
                if (i == messages.size())
                    i = 0;
            }
        }.runTaskTimer(lobby, 0, stay * 20l);
    }
}
