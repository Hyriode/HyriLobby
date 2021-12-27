package fr.hyriode.lobby.api.chooser;

/**
 * Enum represents the game items
 */
public enum GameItem {

    NEXUS("Nexus", "BEACON"),
    BEDWARS("Bedwars", "BED"),
    RTF("Rush the Flag", "BANNER"),

    NULL(" ", "BARRIER");

    private final String name;
    private final String material;

    /**
     * Constructor of {@link GameItem}
     * @param name The item name
     * @param material The item material
     */
    GameItem(String name, String material) {
        this.name = name;
        this.material = material;
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
