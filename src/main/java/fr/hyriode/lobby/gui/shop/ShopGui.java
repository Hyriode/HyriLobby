package fr.hyriode.lobby.gui.shop;

import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.shop.cosmetics.CosmeticsShopGui;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHead;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class ShopGui extends LobbyInventory {

    public ShopGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "shop", 54);

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fillOutline(FILL_ITEM);

        this.setItem(20, HEAD_ITEM.apply(UsefulHead.DIAMOND_BLOCK).withName(this.getMessage("ranks_shop", "name")).withLore(this.getMessage("ranks.lore")).build(),
                e -> new RanksShopGui(this.plugin, this.owner).open()
        );
        //TODO Add Hyri+ lore with design and advantages
        this.setItem(23, new ItemBuilder(new Potion(PotionType.WATER)).withName(ChatColor.DARK_AQUA + "Boosters").withLore(this.getMessage("global", "not_implemented")).build());
        this.setItem(24, HEAD_ITEM.apply(UsefulHead.ENDERCHEST).withName(ChatColor.DARK_AQUA + "Box").withLore(this.getMessage("global", "not_implemented")).build());
        this.setItem(29, HEAD_ITEM.apply(UsefulHead.GOLD_BLOCK).withName(this.getMessage("hyriplus")).withLore(this.getMessage("ranks.website").split("\n")).build());
        this.setItem(32, HEAD_ITEM.apply(UsefulHead.CHEST).withName(this.getMessage("cosmetics_shop", "name")).withLore(this.getMessage("cosmetics_shop", "lore")).build(),
                e -> new CosmeticsShopGui(this.plugin, this.owner).open()
        );
        this.setItem(33, HEAD_ITEM.apply(UsefulHead.CRATE).withName(this.getMessage("games_shop", "name")).withLore(/*this.getMessage("games_shop", "lore"), */this.getMessage("global", "not_implemented")).build());
    }
}
