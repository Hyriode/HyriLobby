package fr.hyriode.lobby.gui.utils;

import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;

public enum GameItem {

    RTF(new ItemBuilder(Material.BANNER, 1, 15)),
    NEXUS(new ItemBuilder(Material.BEACON)),
    BEDWARS(new ItemBuilder(Material.BED)),
    LASERGAME(new ItemBuilder(Material.IRON_HOE)),
    NULL(new ItemBuilder(Material.BARRIER));

    private final ItemBuilder item;
    
    GameItem(ItemBuilder item) {
        this.item = item;
    }
    
    public ItemBuilder getItem() {
        return this.item;
    }

    public static GameItem getByName(String name) {
        for (GameItem item : values()) {
            if (item.name().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return GameItem.NULL;
    }
}
