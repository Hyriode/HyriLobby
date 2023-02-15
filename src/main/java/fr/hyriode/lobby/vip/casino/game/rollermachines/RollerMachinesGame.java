package fr.hyriode.lobby.vip.casino.game.rollermachines;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.lobby.vip.casino.game.IGame;
import org.bukkit.entity.Player;

public class RollerMachinesGame implements IGame {

    @Override
    public HyriInventory getInventory(Player player) {
        return new RollingMachinesInventory(player, this.getName());
    }

    @Override
    public String getName() {
        return "roller-machines";
    }

    @Override
    public long getHyrisPrice() {
        return 500L;
    }


    @Override
    public void onWinning() {

    }
}
