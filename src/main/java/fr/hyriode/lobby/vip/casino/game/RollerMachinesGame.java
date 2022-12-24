package fr.hyriode.lobby.vip.casino.game;

import fr.hyriode.api.money.IHyriMoney;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.SplittableRandom;

public class RollerMachinesGame implements IGame {

    @Override
    public String getName() {
        return "roller-machines";
    }

    @Override
    public long getHyrisPrice() {
        return 500L;
    }

    @Override
    public Inventory initGui(Inventory inventory) {
        final ItemStack cyanGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9);
        final ItemStack greenGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);

        inventory.setItem(0, greenGlass);
        inventory.setItem(1, cyanGlass);
        inventory.setItem(2, greenGlass);

        inventory.setItem(6, greenGlass);
        inventory.setItem(7, cyanGlass);
        inventory.setItem(8, greenGlass);

        inventory.setItem(9, cyanGlass);
        inventory.setItem(10, new ItemStack(Material.GOLD_BLOCK));
        inventory.setItem(11, cyanGlass);

        inventory.setItem(15, cyanGlass);
        inventory.setItem(16, new ItemStack(Material.GOLD_BLOCK));
        inventory.setItem(17, cyanGlass);

        inventory.setItem(18, greenGlass);
        inventory.setItem(19, cyanGlass);
        inventory.setItem(20, greenGlass);

        inventory.setItem(24, greenGlass);
        inventory.setItem(25, cyanGlass);
        inventory.setItem(26, greenGlass);
        
        
        return inventory;
    }

    @Override
    public void onWinning() {

    }
}
