package fr.hyriode.lobby.vip.casino.game.rollermachines;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.lobby.util.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class RollingMachinesInventory extends HyriInventory {

    private byte itemsPointer = 0;
    private int frameCount = 0;

    private final ERollingMachinesLoot loot = Utils.getRollerMachinesLoot();

    private final ItemStack[] items = new ItemStack[ERollingMachinesLoot.values().length-1];

    public RollingMachinesInventory(Player owner, String name) {
        super(owner, name, 27);

        for (int i = 0; i < items.length; i++) {
            items[i] = ERollingMachinesLoot.values()[i].getItemStack();
        }

        this.init();
        this.newUpdate(3L);
    }

    private void init() {
        final ItemStack cyanGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9);
        final ItemStack greenGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);

        System.out.println(this.loot.name());

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
            case DOUBLE:
                this.itemsPointer = 3;
                return;
            case TRIPLE:
                this.itemsPointer = 2;
                return;
            case QUADRUPLE:
                this.itemsPointer = 1;
        }
    }

    @Override
    public void update() {
        this.itemsPointer++;
        this.frameCount++;

        //avoid 3 same items possibility
        final int firstRandom = this.getRandom();
        final int secondRandom = this.loot == ERollingMachinesLoot.NOTHING ? firstRandom + 1 : 0;
        final int thirdRandom = this.getRandom();

        if(this.itemsPointer >= this.items.length) {
            this.itemsPointer = 0;
        }

        if(this.frameCount >= this.items.length*15 + firstRandom) {
            return;
        }

        this.setItem(5, this.items[(this.itemsPointer + 2) % this.items.length]);
        this.setItem(14, this.items[(this.itemsPointer + 1) % this.items.length]);
        this.setItem(23, this.items[this.itemsPointer % this.items.length]);

        if(this.frameCount >= this.items.length*12 + secondRandom) {
            return;
        }

        this.setItem(4, this.items[(this.itemsPointer + 2) % this.items.length]);
        this.setItem(13, this.items[(this.itemsPointer + 1) % this.items.length]);
        this.setItem(22, this.items[this.itemsPointer % this.items.length]);

        if(this.frameCount >= this.items.length*9 + thirdRandom) {
            return;
        }

        this.setItem(3, this.items[(this.itemsPointer + 2) % this.items.length]);
        this.setItem(12, this.items[(this.itemsPointer + 1) % this.items.length]);
        this.setItem(21, this.items[this.itemsPointer % this.items.length]);
    }

    private int getRandom() {
        if (this.loot == ERollingMachinesLoot.NOTHING) {
            return ThreadLocalRandom.current().nextInt(this.items.length);
        }
        return 0;
    }
}
