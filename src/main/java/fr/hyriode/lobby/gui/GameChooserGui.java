package fr.hyriode.lobby.gui;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.util.UsefulHeads;
import fr.hyriode.tools.inventory.AbstractInventory;
import fr.hyriode.tools.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GameChooserGui extends AbstractInventory {

    private final Player player;
    private final IHyrame hyrame;
    private final IHyriLanguageManager lang;

    public GameChooserGui(HyriLobby plugin, Player owner) {
        super(owner, plugin.getHyrame().getLanguageManager().getMessageValueForPlayer(owner, "title.chooser.gui"), 54);

        this.player = owner;
        this.hyrame = plugin.getHyrame();
        this.lang = this.hyrame.getLanguageManager();

        this.addEarthItem();
        this.addGameItems();
    }

    private void addEarthItem() {
        this.setItem(4, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.EARTH.getTexture())
                .withName(this.lang.getMessageValueForPlayer(this.player, "title.chooser.gui")).build()
        );
    }

    private void addGameItems() {

    }
}
