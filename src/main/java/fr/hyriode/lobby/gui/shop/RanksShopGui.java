package fr.hyriode.lobby.gui.shop;

import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHead;
import org.bukkit.entity.Player;

public class RanksShopGui extends LobbyInventory {

    public RanksShopGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "ranks_shop", 45);

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fillOutline(FILL_ITEM);
        //TODO Add ranks with design and advantages
        this.setItem(20, HEAD_ITEM.apply(UsefulHead.YELLOW_CUBE).withName(this.getMessage("vip")).withLore(this.getMessage("shop", "ranks.website").split("\n")).build());
        this.setItem(22, HEAD_ITEM.apply(UsefulHead.GREEN_CUBE).withName(this.getMessage("vip+")).withLore(this.getMessage("shop", "ranks.website").split("\n")).build());
        this.setItem(24, HEAD_ITEM.apply(UsefulHead.BLUE_CUBE).withName(this.getMessage("epic")).withLore(this.getMessage("shop", "ranks.website").split("\n")).build());

        this.setupReturnButton(new ShopGui(this.plugin, this.owner), null);
    }
}
