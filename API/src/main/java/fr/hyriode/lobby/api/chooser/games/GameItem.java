package fr.hyriode.lobby.api.chooser.games;

import fr.hyriode.lobby.api.items.Item;

import java.util.Arrays;
import java.util.List;

public class GameItem {

    private final Item item;
    private final List<String> modes;

    public GameItem(Item item, String... modes) {
        this(item, Arrays.asList(modes));
    }

    public GameItem(Item item, List<String> modes) {
        this.item = item;
        this.modes = modes;
    }

    public Item getItem() {
        return this.item;
    }

    public List<String> getModes() {
        return this.modes;
    }
}
