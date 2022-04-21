package fr.hyriode.lobby.gui.shop.cosmetics;

import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import org.bukkit.entity.Player;

public class OwnedCosmeticsGui extends LobbyInventory {

    public OwnedCosmeticsGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "cosmetics_shop.owned", 54);

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fillOutline(FILL_ITEM);

        //TODO Use the method to get the owned cosmetics in HyriCosmetics to display owned cosmetics with paging system
        //TODO Follow fr.hyriode.lobby.gui.LobbySelectorGui, from lines #40 to #70

        this.setupReturnButton(new CosmeticsShopGui(this.plugin, this.owner), null);
    }
}
