package fr.hyriode.lobby.api.chooser;

import java.util.Arrays;
import java.util.List;

/**
 * Enum represents the game items
 */
public enum GameItem {

    NEXUS("Nexus", "BEACON", Arrays.asList("4v4")),
    BEDWARS("Bedwars", "BED", Arrays.asList("1v1", "2v2", "4v4")),
    RTF("RTF", "BANNER", Arrays.asList("2v2", "5v5")),

    NULL(" ", "BARRIER", null);

    private final String name;
    private final String material;
    private final List<String> modes;

    /**
     * Constructor of {@link GameItem}
     * @param name The item name
     * @param material The item material
     * @param modes The game modes
     */
    GameItem(String name, String material, List<String> modes) {
        this.name = name;
        this.material = material;
        this.modes = modes;
    }

    /**
     * Get the item name
     * @return The item name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the item material
     * @return The item material
     */
    public String getMaterial() {
        return material;
    }

    /**
     * Get the game modes
     * @return A {@link List<String>} of game modes
     */
    public List<String> getModes() {
        return modes;
    }

    /**
     * Get an item by his name
     * @param name The item name
     * @return The item with the wanted name
     */
    public static GameItem getByName(String name) {
        for (GameItem item : values()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}
