package fr.hyriode.lobby.gui.profile;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.HyriBoosterTransaction;
import fr.hyriode.api.booster.IHyriBoosterManager;
import fr.hyriode.hyrame.inventory.pagination.PaginatedItem;
import fr.hyriode.hyrame.inventory.pagination.PaginationArea;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.DurationFormatter;
import fr.hyriode.hyrame.utils.Pagination;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.ConfirmGUI;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * Created by AstFaster
 * on 23/10/2022 at 10:29
 */
public class BoostersGUI extends LobbyGUI {

    public BoostersGUI(HyriLobby plugin, Player owner) {
        super(owner, plugin, () -> "boosters", 54);
        this.paginationManager.setArea(new PaginationArea(20, 33));

        this.init();
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        this.setupItems();
        this.addPagesItems(27, 35);

        this.paginationManager.updateGUI();
    }

    private void setupItems() {
        final Pagination<PaginatedItem> pagination = this.paginationManager.getPagination();

        pagination.clear();

        final Map<String, HyriBoosterTransaction> boosters = HyriAPI.get().getBoosterManager().getPlayerBoosters(this.account);

        for (Map.Entry<String, HyriBoosterTransaction> entry : boosters.entrySet()) {
            final String name = entry.getKey();
            final HyriBoosterTransaction booster = entry.getValue();

            if (booster.isUsed()) {
                continue;
            }

            final ItemStack itemStack = this.createBoosterItem(booster);

            pagination.add(PaginatedItem.from(itemStack, event -> {
                new ConfirmGUI(this.owner, this.plugin, new ItemBuilder(itemStack).removeLoreLines(2).build())
                        .whenConfirm(e -> {
                            this.owner.closeInventory();

                            HyriAPI.get().getBoosterManager().enableBooster(this.owner.getUniqueId(), "global", booster.getMultiplier(), booster.getDuration());

                            booster.setUsed(true);

                            this.account.removeTransaction(HyriBoosterTransaction.TRANSACTIONS_TYPE, name);
                            this.account.addTransaction(HyriBoosterTransaction.TRANSACTIONS_TYPE, name, booster);
                            this.account.update();
                        })
                        .whenCancel(e -> this.open())
                        .open();
            }));
        }
    }

    private ItemStack createBoosterItem(HyriBoosterTransaction booster) {
        final String duration = new DurationFormatter().format(this.account.getSettings().getLanguage(), booster.getDuration() * 1000);
        final List<String> lore = ListReplacer.replace(LobbyMessage.BOOSTERS_BOOSTER_ITEM_LORE.asList(this.account), "%boost%", "+" + ((int) booster.getMultiplier() * 100))
                .replace("%multiplier%", "x" + booster.getMultiplier())
                .replace("%duration%", duration)
                .list();

        return new ItemBuilder(Material.POTION)
                .withAllItemFlags()
                .withName(LobbyMessage.BOOSTERS_BOOSTER_ITEM_NAME.asString(this.account).replace("%type%", (booster.getType().equals(IHyriBoosterManager.GLOBAL_TYPE) ? LobbyMessage.BOOSTERS_BOOSTER_GLOBAL : LobbyMessage.BOOSTERS_BOOSTER_GAME).asString(this.account)))
                .withLore(lore)
                .build();
    }

}
