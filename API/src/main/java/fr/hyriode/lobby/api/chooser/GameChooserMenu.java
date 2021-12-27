package fr.hyriode.lobby.api.chooser;

import java.util.HashMap;

/**
 * Represents the game chooser menu items
 */
public class GameChooserMenu {

    /**
     * Integer is the custom slot number and {@link GameItem} represents the game item
     */
    private final HashMap<Integer, GameItem> games;

    /**
     * Constructor of {@link GameChooserMenu}
     */
    public GameChooserMenu() {
        this.games = new HashMap<>();

        this.games.put(0, GameItem.BEDWARS);
        this.games.put(1, GameItem.NEXUS);
        this.games.put(2, GameItem.RTF);

        for (int i = 3; i <= 6; i++) {
            this.games.put(i, GameItem.NULL);
        }
    }

    /**
     * Constructor of {@link GameChooserMenu}
     * @param games The map of the game items and their slot
     */
    public GameChooserMenu(HashMap<Integer, GameItem> games) {
        this.games = games;
    }

    /**
     * Set a {@link GameItem} with his slot
     * @param slot The slot of the item
     * @param item The game item to set
     */
    public void setGame(int slot, GameItem item) {
        this.games.replace(slot, item);
    }

    /**
     * Set a {@link GameItem} with his slot
     * @param games The map of the game items and their slot
     */
    public void setGame(HashMap<Integer, GameItem> games) {
        games.forEach(this::setGame);
    }

    /**
     * Get the map of game items and their slot
     * @return The map of game items and their slot
     */
    public HashMap<Integer, GameItem> getGames() {
        return games;
    }
}
