package fr.hyriode.lobby.gui.shop.cosmetics;

import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.shop.ShopGui;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHead;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CosmeticsShopGui extends LobbyInventory {

    public CosmeticsShopGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "cosmetics_shop", 54);

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fillOutline(FILL_ITEM);

        this.setItem(20, new ItemBuilder(Material.GREEN_RECORD).withName(this.getMessage("category.gadget.name")).withLore(this.getMessage("category.gadget.lore")).build(),
                e -> new CosmeticsTypeShopGui(this.plugin, this.owner, /*Type enum from HyriCosmetics*/"gadget").open()
        );
        this.setItem(21, HEAD_ITEM.apply(UsefulHead.TIGER).withName(this.getMessage("category.pet.name")).withLore(this.getMessage("category.pet.lore")).build(),
                e -> new CosmeticsTypeShopGui(this.plugin, this.owner, /*Type enum from HyriCosmetics*/"pet").open()
        );
        this.setItem(24, HEAD_ITEM.apply(UsefulHead.CRATE).withName(this.getMessage("owned.name")).withLore(this.getMessage("owned.lore")).build(),
                e -> new OwnedCosmeticsGui(this.plugin, this.owner).open()
        );
        this.setItem(29, new ItemBuilder(Material.BLAZE_POWDER).withName(this.getMessage("category.particle.name")).withLore(this.getMessage("category.particle.lore")).build(),
                e -> new CosmeticsTypeShopGui(this.plugin, this.owner, /*Type enum from HyriCosmetics*/"particle").open()
        );
        this.setItem(30, HEAD_ITEM.apply(UsefulHead.HORSE).withName(this.getMessage("category.mount.name")).withLore(this.getMessage("category.mount.lore")).build(),
                e -> new CosmeticsTypeShopGui(this.plugin, this.owner, /*Type enum from HyriCosmetics*/"mount").open()
        );
        this.setItem(33, HEAD_ITEM.apply(UsefulHead.MONEY).withName(this.getMessage("shop", "money") + this.account.getHyris().getAmount() + ChatColor.LIGHT_PURPLE + " Hyris").build());

        this.setupReturnButton(new ShopGui(this.plugin, this.owner), null);
    }
}
