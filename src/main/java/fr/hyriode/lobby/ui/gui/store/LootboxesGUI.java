package fr.hyriode.lobby.ui.gui.store;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.ui.gui.LobbyGUI;
import fr.hyriode.lobby.store.lootbox.Lootbox;
import fr.hyriode.lobby.store.lootbox.StoreLootbox;
import fr.hyriode.lobby.store.StoreCategory;
import fr.hyriode.lobby.store.StoreItem;
import org.bukkit.entity.Player;

/**
 * Created by AstFaster
 * on 01/07/2022 at 19:24
 */
public class LootboxesGUI extends LobbyGUI {

    private static final StoreCategory CATEGORY = new StoreCategory(null, "lootboxes");

    static {
        for (Lootbox lootbox : Lootbox.values()) {
            final StoreItem item = new StoreLootbox(CATEGORY.getId(), lootbox)
                    .whenPurchased(account -> HyriAPI.get().getLootboxManager().giveLootbox(account, lootbox.getRarity()));

            CATEGORY.addContent(item);
        }
    }

    public LootboxesGUI(Player owner, HyriLobby plugin) {
        super(owner, plugin, () -> "store-lootboxes", 54);

        this.init();
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        int index = 0;
        for (int y = 0; y <= 1; y++) {
            for (int x = 21; x <= 23; x++) {
                if (CATEGORY.getContents().size() <= index) {
                    break;
                }

                final StoreItem item = (StoreItem) CATEGORY.getContents().get(index);

                this.setItem(9 * y + x, item.createItem(this.account), event -> item.purchase(this.plugin, this.owner));
                index++;
            }
        }
    }

}
