package fr.hyriode.lobby.vip.casino.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IGame {

    void init(Player player);
    String getName();
    void initGui(Inventory inventory);
    void onWinning();
    //Material getBlock();
}
