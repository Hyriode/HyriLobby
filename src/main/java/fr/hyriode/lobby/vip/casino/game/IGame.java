package fr.hyriode.lobby.vip.casino.game;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.lobby.vip.casino.game.rollermachines.RollingMachinesInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IGame {

    default void init(Player player) {
        final IHyriPlayer hyriPlayer = IHyriPlayer.get(player.getUniqueId());
        final long hyris = hyriPlayer.getHyris().getAmount();

        if(this.getHyrisPrice() != 0L) {
            if(hyris >= this.getHyrisPrice()) {
                hyriPlayer.getHyris().remove(this.getHyrisPrice()).exec();
                hyriPlayer.update();
            } else {
                player.sendMessage("§4Vous n'avez pas assez d'Hyris");
                return;
            }
        }

        this.getInventory(player).open();
    }
    HyriInventory getInventory(Player player);
    String getName();
    long getHyrisPrice();


    default void onWinning() {

    }
}
