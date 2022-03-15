package fr.hyriode.lobby.gui.chooser.utils;

import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum GameItem {

    RTF(new ItemBuilder(Material.BANNER, 1, 15), "RTF", true, "2v2", "5v5"),
    NEXUS(new ItemBuilder(Material.BEACON), "Nexus", true, "2v2", "5v5", "10v10"),
    BEDWARS(new ItemBuilder(Material.BED), "Bedwars", true, "1v1", "2v2", "4v4", "4v4v4v4"),
    LASER_GAME(new ItemBuilder(Material.IRON_HOE), "LaserGame", true, "4v4"),

    TEST(new ItemBuilder(Material.LEAVES), "TEST", false, "1v1"),
    NULL(new ItemBuilder(Material.BARRIER), " ", false);

    private final String name;
    private final ItemBuilder item;
    private final boolean required;
    private final List<String> modes;
    
    GameItem(ItemBuilder item, String name, boolean required, String... modes) {
        this.name = name;
        this.item = item;
        this.required = required;
        this.modes = Arrays.asList(modes);
        
        this.item.withName(name);
    }
    
    public ItemBuilder getItem() {
        return this.item;
    }

    public boolean isRequired() {
        return this.required;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getModes() {
        return this.modes;
    }

    public static GameItem getByName(String name) {
        for (GameItem item : values()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return GameItem.NULL;
    }

    public static List<ItemBuilder> getRequiredItems() {
        final List<ItemBuilder> required = new ArrayList<>();
        Arrays.asList(values()).forEach(item -> {
            if (item.isRequired()) {
                required.add(item.getItem());
            }
        });
        return required;
    }
}
