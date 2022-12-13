package fr.hyriode.lobby.vip.casino.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class RollerMachinesGame implements IGame {

    @Override
    public void init(Player player) {
        player.sendMessage("test " + this.getName());
    }

    @Override
    public String getName() {
        return "roller-machines";
    }

    @Override
    public void initGui(Inventory inventory) {

    }

    @Override
    public void onWinning() {

    }
}
