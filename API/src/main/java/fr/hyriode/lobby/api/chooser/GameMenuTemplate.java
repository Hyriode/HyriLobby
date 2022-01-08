package fr.hyriode.lobby.api.chooser;

import fr.hyriode.lobby.api.chooser.games.GameItem;
import fr.hyriode.lobby.api.items.Item;

import java.util.HashMap;
import java.util.Map;

public class GameMenuTemplate {

    private final Item header;
    private final Map<Integer, GameItem> games;

    public GameMenuTemplate() {
        this.header = new Item("TEST");
        this.games = new HashMap<>();
    }
}
