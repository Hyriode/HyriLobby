package fr.hyriode.lobby.vip.casino.game.zzs;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.lobby.vip.casino.game.IGame;
import org.bukkit.entity.Player;

public class ZeroZeroSevenGame implements IGame {
    @Override
    public HyriInventory getInventory(Player player) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public long getHyrisPrice() {
        return 0;
    }

    public boolean play(int playingTime) {
        return false;
    }
}
