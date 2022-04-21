package fr.hyriode.lobby.gui.shop.cosmetics;

import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import org.bukkit.entity.Player;

public class CosmeticsTypeShopGui extends LobbyInventory {

    private final String category;

    public CosmeticsTypeShopGui(HyriLobby plugin, Player owner, String category) {
        super(owner, plugin, "cosmetics_shop.category." + category, 54);

        this.category = category;

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fillOutline(FILL_ITEM);

        //TODO Add cosmetics with HyriCosmetics API, using category
        //TODO Implements cosmetics with paging, follow fr.hyriode.lobby.gui.LobbySelectorGui, from lines #40 to #70
        //TODO We should use https://github.com/Hyriode/HyriCosmetics/blob/develop/API/src/main/java/fr/hyriode/cosmetics/api/IHyriCosmeticsManager.java#L22
        //TODO And simply order the given list by rarity, before sending it to the paging system

        this.setupReturnButton(new CosmeticsShopGui(this.plugin, this.owner), null);
    }
}
