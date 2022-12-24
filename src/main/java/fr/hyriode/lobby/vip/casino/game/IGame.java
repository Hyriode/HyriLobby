package fr.hyriode.lobby.vip.casino.game;

import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.player.IHyriPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public interface IGame {

    default void init(Player player) {
        final IHyriPlayer hyriPlayer = IHyriPlayer.get(player.getUniqueId());
        final long hyris = hyriPlayer.getHyris().getAmount();

        if(hyris >= this.getHyrisPrice()) {
            hyriPlayer.getHyris().remove(500L).exec();
            hyriPlayer.update();
            player.openInventory(initGui(Bukkit.createInventory(null, 27, this.getName())));
        } else {
            //remove before prod
            hyriPlayer.getHyris().add(500L).exec();
            hyriPlayer.update();
            player.sendMessage("§4Vous n'avez pas assez d'Hyris " + hyriPlayer.getName());
        }
    }
    String getName();
    long getHyrisPrice();
    Inventory initGui(Inventory inventory);
    void onWinning();
}
