package fr.hyriode.lobby.vip.casino.game.rollermachines;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ThreadLocalRandom;

public class RollingMachinesInventory extends HyriInventory {

    private final RollerMachinesGame game;
    private byte itemsPointer = 0;
    private int frameCount = 0;
    private boolean end = false;

    private final ERollingMachinesLoot loot = this.getLoot();
    private final ItemStack[] items = new ItemStack[ERollingMachinesLoot.values().length];

    public RollingMachinesInventory(Player owner, RollerMachinesGame game) {
        super(owner, game.getName(), 27);

        this.game = game;
        for (int i = 0; i < items.length; i++) {
            items[i] = ERollingMachinesLoot.values()[i].getItemStack();
        }

        this.init();
        this.newUpdate(2L);
    }

    private void init() {
        final ItemStack cyanGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9);
        final ItemStack greenGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);

        this.setItem(0, greenGlass);
        this.setItem(1, cyanGlass);
        this.setItem(2, greenGlass);

        this.setItem(6, greenGlass);
        this.setItem(7, cyanGlass);
        this.setItem(8, greenGlass);

        this.setItem(9, cyanGlass);
        this.setItem(10, new ItemStack(Material.GOLD_BLOCK));
        this.setItem(11, cyanGlass);

        this.setItem(15, cyanGlass);
        this.setItem(16, new ItemStack(Material.GOLD_BLOCK));
        this.setItem(17, cyanGlass);

        this.setItem(18, greenGlass);
        this.setItem(19, cyanGlass);
        this.setItem(20, greenGlass);

        this.setItem(24, greenGlass);
        this.setItem(25, cyanGlass);
        this.setItem(26, greenGlass);


        switch (this.loot) {
            case FIFTY:
                this.itemsPointer = 6;
                break;
            case ONE_HUNDRED:
                this.itemsPointer = 5;
                break;
            case TWO_HUNDRED_FIFTY:
                this.itemsPointer = 4;
                break;
            case FIVE_HUNDRED:
                this.itemsPointer = 3;
                break;
            case SEVEN_HUNDRED_FIFTY:
                this.itemsPointer = 2;
                break;
            case ONE_THOUSAND:
                this.itemsPointer = 1;
                break;
            case TEN_THOUSAND:
                this.itemsPointer = 0;
                break;
            default:
                break;
        }
    }

    @Override
    public void update() {
        if(this.end) {
            return;
        }

        this.itemsPointer++;
        this.frameCount++;

        if (this.itemsPointer >= this.items.length) {
            this.itemsPointer = 0;
        }

        if (this.frameCount >= this.items.length * 15) {
            this.end = true;
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(HyriLobby.class), () -> this.game.onWinning(this.loot.getHyris()), 20L);
            return;
        }

        this.setItem(5, this.items[(this.itemsPointer + 2) % this.items.length]);
        this.setItem(14, this.items[(this.itemsPointer + 1) % this.items.length]);
        this.setItem(23, this.items[this.itemsPointer % this.items.length]);

        if (this.frameCount >= this.items.length * 12) {
            return;
        }

        this.setItem(4, this.items[(this.itemsPointer + 2) % this.items.length]);
        this.setItem(13, this.items[(this.itemsPointer + 1) % this.items.length]);
        this.setItem(22, this.items[this.itemsPointer % this.items.length]);

        if (this.frameCount >= this.items.length * 9) {
            return;
        }

        this.setItem(3, this.items[(this.itemsPointer + 2) % this.items.length]);
        this.setItem(12, this.items[(this.itemsPointer + 1) % this.items.length]);
        this.setItem(21, this.items[this.itemsPointer % this.items.length]);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        super.onClose(event);
        if (!this.end) Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(HyriLobby.class), this::open, 1L);
    }

    private ERollingMachinesLoot getLoot() {
        final float random = ThreadLocalRandom.current().nextFloat();
        for (ERollingMachinesLoot probability : ERollingMachinesLoot.values()) {
            if(random <= probability.getProbability()) {
                return probability;
            }
        }
        return null;
    }
}
