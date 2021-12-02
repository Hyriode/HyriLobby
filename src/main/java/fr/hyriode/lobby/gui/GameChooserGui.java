package fr.hyriode.lobby.gui;

import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.tools.inventory.AbstractInventory;
import org.bukkit.entity.Player;

public class GameChooserGui extends AbstractInventory {


    public GameChooserGui(HyriLobby plugin, Player owner) {
        super(owner, plugin.getHyrame().getLanguageManager().getMessageValueForPlayer(owner, "title.chooser.gui"), 54);
    }
}
