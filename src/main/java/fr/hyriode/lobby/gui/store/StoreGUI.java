package fr.hyriode.lobby.gui.store;

import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by AstFaster
 * on 01/07/2022 at 13:49
 */
public class StoreGUI extends LobbyGUI {

    public StoreGUI(Player owner, HyriLobby plugin) {
        super(owner, plugin, () -> "store", 54);

        this.init();
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        // Lootboxes
        this.setItem(21, ItemBuilder.asHead()
                .withName(LobbyMessage.STORE_LOOTBOXES_NAME.asString(this.account))
                .withLore(LobbyMessage.STORE_LOOTBOXES_LORE.asList(this.account))
                .withHeadTexture(UsefulHead.ENDER_CHEST)
                .build(), event -> this.openWithGoBack(49, new LootboxesGUI(this.owner, this.plugin)));

        // Boosters
        this.setItem(22, new ItemBuilder(Material.POTION)
                .withAllItemFlags()
                .withName(LobbyMessage.STORE_BOOSTERS_NAME.asString(this.account))
                .withLore(LobbyMessage.STORE_BOOSTERS_LORE.asList(this.account))
                .build(), event -> this.openWithGoBack(49, new BoostersGUI(this.owner, this.plugin)));

        // Ranks
        this.setItem(23, ItemBuilder.asHead()
                .withName(LobbyMessage.STORE_RANKS_NAME.asString(this.account))
                .withLore(LobbyMessage.STORE_RANKS_LORE.asList(this.account))
                .withHeadTexture(UsefulHead.DIAMOND_BLOCK)
                .build(), event -> this.openWithGoBack(49, new RanksGUI(this.owner, this.plugin)));
        
        // Cosmetics
        this.setItem(31, ItemBuilder.asHead()
                .withHeadTexture(UsefulHead.COSMETICS_CHEST)
                .withName(LobbyMessage.STORE_COSMETICS_NAME.asString(this.account))
                .withLore(LobbyMessage.STORE_COSMETICS_LORE.asList(this.account))
                .build(), event -> this.owner.performCommand("c"));
    }

}
