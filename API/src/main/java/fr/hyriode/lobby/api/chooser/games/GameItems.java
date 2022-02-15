package fr.hyriode.lobby.api.chooser.games;

import fr.hyriode.lobby.api.items.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enum represents the game items
 */
public enum GameItems {

    RTF(new GameItem(new Item("RTF", "BANNER", 15), "2v2", "5v5"), true),
    NEXUS(new GameItem(new Item("Nexus", "BEACON"), "2v2", "5v5", "10v10"), true),
    BEDWARS(new GameItem(new Item("Bedwars", "BED"), "1v1", "2v2", "4v4", "4v4v4v4"), true),
    LASER_GAME(new GameItem(new Item("LaserGame", "IRON_HOE"), "4v4"), true),

    TEST(new GameItem(new Item("TEST", "LEAVES"), "1v1"), false),
    NULL(new GameItem(new Item(" ", "BARRIER"), (String) null), false);

    /**
     * The game item
     */
    private final GameItem item;
    private final boolean required;

    /**
     * Constructor of {@link GameItems}
     * @param item The game item instance
     */
    GameItems(GameItem item, boolean required) {
        this.item = item;
        this.required = required;
    }

    /**
     * Get the {@link GameItem}
     * @return the game item
     */
    public GameItem getGameItem() {
        return this.item;
    }

    public boolean isRequired() {
        return this.required;
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

    public static List<GameItem> getRequiredItems() {
        final List<GameItem> required = new ArrayList<>();
        Arrays.asList(values()).forEach(item -> {
            if (item.isRequired()) {
                required.add(item.getGameItem());
            }
        });
        return required;
    }
}
