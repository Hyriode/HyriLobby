package fr.hyriode.lobby.store;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.store.StoreConfirmGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by AstFaster
 * on 02/07/2022 at 17:22
 */
public class StoreDependentItem extends StoreItem {

    private final StoreItem depends;

    public StoreDependentItem(ItemStack itemStack, String category, String id, StoreItem depends, StorePrice... prices) {
        super(itemStack, category, id, prices);
        this.depends = depends;
    }

    @Override
    public void purchase(HyriLobby plugin, Player player) {
        final IHyriPlayer account = IHyriPlayer.get(player.getUniqueId());

        if (!this.depends.isOwning(account)) {
            player.sendMessage(LobbyMessage.STORE_DEPENDS_NOT_OWNED_MESSAGE.asString(account).replace("%object%", this.depends.getName().getValue(account)));
            return;
        }

        super.purchase(plugin, player);
    }

    @Override
    public ItemStack createItem(IHyriPlayer account) {
        return super.createItem(account);
    }

    public StoreItem getDepends() {
        return this.depends;
    }

}
