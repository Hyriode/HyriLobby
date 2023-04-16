package fr.hyriode.lobby.vip.casino.game.rollermachines;

import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ERollingMachinesLoot {

    TEN_THOUSAND(0.02F, 10000L, new ItemBuilder(Material.DIAMOND_BLOCK).withName("§r10 000 Hyris")),
    ONE_THOUSAND(0.08F, 1000L, new ItemBuilder(Material.EMERALD).withName("§r1 000 Hyris")),
    SEVEN_HUNDRED_FIFTY(0.23F, 750L, new ItemBuilder(Material.GOLD_BLOCK).withName("§r750 Hyris")),
    FIVE_HUNDRED(0.28F, 500L, new ItemBuilder(Material.GOLDEN_APPLE).withName("§r500 Hyris")),
    TWO_HUNDRED_FIFTY(0.53F, 250, new ItemBuilder(Material.REDSTONE).withName("§r250 Hyris")),
    ONE_HUNDRED(0.77F, 100, new ItemBuilder(Material.TNT).withName("§r100 Hyris")),
    FIFTY(1.0F, 50, new ItemBuilder(Material.LEVER).withName("§r50 Hyris"));

    private final float probability;
    private final long hyris;
    private final ItemStack itemStack;

    ERollingMachinesLoot(float probability, long hyris, ItemBuilder itemBuilder) {
        this.probability = probability;
        this.hyris = hyris;
        this.itemStack = itemBuilder.build();
    }


    public float getProbability() {
        return probability;
    }

    public long getHyris() {
        return hyris;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
