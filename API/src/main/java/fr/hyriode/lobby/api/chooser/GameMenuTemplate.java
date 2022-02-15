package fr.hyriode.lobby.api.chooser;

import fr.hyriode.lobby.api.chooser.games.GameItem;
import fr.hyriode.lobby.api.chooser.games.GameItems;
import fr.hyriode.lobby.api.items.Item;
import fr.hyriode.lobby.api.utils.HyriReverseMap;

import java.util.List;

public class GameMenuTemplate {

    private static final List<GameItem> REQUIRED = GameItems.getRequiredItems();

    private Item fill;
    private final HyriReverseMap<Integer, GameItem> games;

    public GameMenuTemplate() {
        this.games = new HyriReverseMap<>();
        this.fill = new Item("STAINED_GLASS_PANE", 15);
    }

    public Item getFill() {
        return this.fill;
    }

    public void setFill(Item fill) {
        this.fill = fill;
    }

    public HyriReverseMap<Integer, GameItem> getGames() {
        return this.games;
    }

    public List<GameItem> getRequiredItems() {
        return REQUIRED;
    }
}
