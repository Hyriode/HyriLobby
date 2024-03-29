package fr.hyriode.lobby.ui.gui.profile.booster;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.HyriBoosterTransaction;
import fr.hyriode.hyrame.inventory.pagination.PaginatedItem;
import fr.hyriode.hyrame.inventory.pagination.PaginationArea;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.DurationFormatter;
import fr.hyriode.hyrame.utils.Pagination;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.ui.gui.LobbyGUI;
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
            final HyriBoosterTransaction booster = entry.getValue();

            if (booster.isUsed()) {
                continue;
            }

            final ItemStack itemStack = this.createBoosterItem(booster);

            pagination.add(PaginatedItem.from(itemStack, event -> this.openWithGoBack(49,
                    new BoosterGameSelectorGUI(this.owner, this.plugin, entry.getKey(), booster, new ItemBuilder(itemStack.clone())
                    .removeLoreLines(2)
                    .build()))));
        }
    }

    private ItemStack createBoosterItem(HyriBoosterTransaction booster) {
        final String boost = "+" + ((int) (booster.getMultiplier() * 100 - 100)) + "%";
        final String duration = new DurationFormatter().format(this.account.getSettings().getLanguage(), booster.getDuration() * 1000);
        final List<String> lore = ListReplacer.replace(LobbyMessage.BOOSTERS_BOOSTER_ITEM_LORE.asList(this.account), "%boost%", boost)
                .replace("%multiplier%", "x" + booster.getMultiplier())
                .replace("%duration%", duration)
                .list();

        return new ItemBuilder(Material.POTION)
                .withAllItemFlags()
                .withName(LobbyMessage.BOOSTERS_BOOSTER_ITEM_NAME.asString(this.account).replace("%boost%", boost))
                .withLore(lore)
                .build();
    }

}
