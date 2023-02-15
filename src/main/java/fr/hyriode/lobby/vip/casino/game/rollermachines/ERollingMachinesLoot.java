package fr.hyriode.lobby.vip.casino.game.rollermachines;

import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ERollingMachinesLoot {

    //TODO: changer les names par des keys de langues

    QUINTUPLE(0.005F, 5,  new ItemBuilder(Material.DIAMOND_BLOCK).withName("0")),
    QUADRUPLE(0.02F, 4, new ItemBuilder(Material.GOLDEN_APPLE).withName("1")),
    TRIPLE(0.05F, 3, new ItemBuilder(Material.TNT).withName("2")),
    DOUBLE(0.175F, 2, new ItemBuilder(Material.LEVER).withName("3")),
    NOTHING(0.75F, 0);

    private final float probability;
    private final int coefficient;
    private final ItemStack itemStack;

    ERollingMachinesLoot(float probability, int coefficient, ItemBuilder itemBuilder) {
        this(probability, coefficient, itemBuilder.build());
    }

    ERollingMachinesLoot(float probability, int coefficient, ItemStack itemStack) {
        this.probability = probability;
        this.coefficient = coefficient;
        this.itemStack = itemStack;
    }

    ERollingMachinesLoot(float probability, int coefficient) {
        this(probability, coefficient, (ItemStack) null);
    }

    public float getProbability() {
        return probability;
    }


    public ItemStack getItemStack() {
        return itemStack;
    }
}
