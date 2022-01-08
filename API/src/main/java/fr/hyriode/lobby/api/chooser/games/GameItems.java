package fr.hyriode.lobby.api.chooser.games;

import fr.hyriode.lobby.api.items.Item;

/**
 * Enum represents the game items
 */
public enum GameItems {

    RTF(new GameItem(new Item("RTF", "BANNER", 15), "2v2", "5v5")),
    NEXUS(new GameItem(new Item("Nexus", "BEACON"), "2v2", "5v5", "10v10")),
    BEDWARS(new GameItem(new Item("Bedwars", "BED"), "1v1", "2v2", "4v4", "4v4v4v4")),

    TEST(new GameItem(new Item("TEST", "LEAVES"), "1v1")),

    NULL(new GameItem(new Item(" ", "BARRIER"), (String) null));

    /**
     * The game item
     */
    private final GameItem item;

    /**
     * Constructor of {@link GameItems}
     * @param item The game item instance
     */
    GameItems(GameItem item) {
        this.item = item;
    }

    /**
     * Get the {@link GameItem}
     * @return the game item
     */
    public GameItem getGameItem() {
        return this.item;
    }

    /**
     * Get an item by his name
     * @param name The item name
     * @return The item with the wanted name
     */
    public static GameItems getByName(String name) {
        for (GameItems item : values()) {
            if (item.getGameItem().getItem().getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}
