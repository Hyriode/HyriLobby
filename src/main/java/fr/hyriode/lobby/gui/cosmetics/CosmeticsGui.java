package fr.hyriode.lobby.gui.cosmetics;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.cosmetics.HyriCosmetics;
import fr.hyriode.cosmetics.common.Cosmetic;
import fr.hyriode.cosmetics.common.CosmeticCategory;
import fr.hyriode.cosmetics.user.CosmeticUser;
import fr.hyriode.cosmetics.user.PlayerCosmetic;
import fr.hyriode.cosmetics.utils.StringUtil;
import fr.hyriode.hyrame.inventory.pagination.PaginatedItem;
import fr.hyriode.hyrame.inventory.pagination.PaginationArea;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Pagination;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.cosmetic.CosmeticItem;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.gui.store.StoreConfirmGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CosmeticsGui extends LobbyGUI {

    private final CosmeticUser user;
    private final CosmeticCategory category;
    private final List<Cosmetic> cosmetics;
    private final List<Cosmetic> unlockedCosmetics;

    public CosmeticsGui(final Player owner, HyriLobby plugin, final CosmeticCategory category) {
        super(owner, plugin, name(owner, "gui.cosmetic.name"), 9 * 6);
        this.user = HyriCosmetics.get().getUserProvider().getUser(owner.getUniqueId());
        this.category = category;
        this.cosmetics = new ArrayList<>(HyriCosmetics.get().getCosmetics().get(category));
        this.unlockedCosmetics = new ArrayList<>(user.getUnlockedCosmetics(category));

        this.paginationManager.setArea(new PaginationArea(20, 33));

        this.init();

        this.setupItems();
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        this.setItem(4,
                new ItemBuilder(category.getIcon())
                        .withName("§b" + category.getTranslatedName().getValue(owner))
                        .withLore(StringUtil.splitIntoPhrases(category.getTranslatedDescription().getValue(owner), 40))
                        .appendLore("§7")
                        .appendLore(HyriLanguageMessage.get("gui.cosmetic.unlocked").getValue(owner)
                                .replace("%percentage%", String.valueOf((int) (((double) unlockedCosmetics.size() / (cosmetics.size() == 0.0D ? 1.0D : (double) cosmetics.size())) * 100.0D)))
                                .replace("%unlocked%", String.valueOf(unlockedCosmetics.size()))
                                .replace("%total%", String.valueOf(cosmetics.size()))
                        ).build()
        );

        this.setItem(51,
                new ItemBuilder(Material.BARRIER).withName(name(owner, "gui.cosmetic.click_to_unequip")).build(),
                event -> {
                    if (user.hasEquippedCosmetic(category)) {
                        user.unequipCosmetic(category, true);
                        user.updateData();
                        event.getWhoClicked().closeInventory();
                        update();
                        new CosmeticsGui((Player) event.getWhoClicked(), plugin, category).open();
                        this.owner.playSound(this.owner.getLocation(), Sound.FIZZ, 0.5F, 1.0F);
                    }
                }
        );

        this.setItem(49, new ItemBuilder(Material.ARROW)
                .withName(LobbyMessage.BACK_ITEM.asString(this.account))
                .build(), event -> new CosmeticsMainGui(this.owner, this.plugin).open());

        this.addPagesItems(27, 35);
    }

    @Override
    public void updatePagination(int page, List<PaginatedItem> items) {
        this.addDefaultPagesItems(27, 35);
    }

    private void setupItems() {
        final Pagination<PaginatedItem> pagination = this.paginationManager.getPagination();

        pagination.clear();

        if (!user.hasEquippedCosmetic(category)) {
            pagination.add(PaginatedItem.from(
                    new ItemBuilder(Material.BARRIER)
                    .withName(name(owner, "gui.cosmetic.equipped.none.name").replace("%type%", category.getTranslatedName().getValue(owner)))
                    .build()
            ));
        } else {
            pagination.add(PaginatedItem.from(
                    new CosmeticItem(user.getEquippedCosmetic(category)).toItemStack(owner, true),
                    this.clickEvent(user.getEquippedCosmetic(category))
            ));
        }

        List<Cosmetic> cosmetics = new ArrayList<>(HyriCosmetics.get().getFilteredCosmetics(user, category));
        if (user.hasEquippedCosmetic(category)) {
            cosmetics.remove(user.getEquippedCosmetic(category));
        }
        for (Cosmetic cosmetic : cosmetics) {
            pagination.add(PaginatedItem.from(new CosmeticItem(cosmetic).toItemStack(owner, true), this.clickEvent(cosmetic)));
        }

        this.paginationManager.updateGUI();
    }

    private Consumer<InventoryClickEvent> clickEvent(Cosmetic cosmetic) {
        return event -> {
            if (event.isLeftClick() || event.isRightClick()) {
                if (!user.hasUnlockedCosmetic(cosmetic)) {
                    if (cosmetic.getInfo().isBuyable()) {
                        this.openWithGoBack(49, new CosmeticsPurchaseConfirmGui(this.owner, this.plugin, cosmetic, event.getCurrentItem()));
                        return;
                    }
                    return;
                } else {
                    if (user.hasEquippedCosmetic(category) && user.getPlayerCosmetic(category).getAbstractCosmetic().getType() == cosmetic) {
                        PlayerCosmetic<?> equippedCosmetic = user.getPlayerCosmetic(category);
                        if (equippedCosmetic.getAbstractCosmetic().hasVariants()) {
                            this.openWithGoBack(49, new CosmeticVariantsGui(this.owner, this.plugin, equippedCosmetic));
                            return;
                        }
                        return;
                    }
                }

                this.user.equipCosmetic(cosmetic, true);
                this.user.updateData();
                this.owner.playSound(this.owner.getLocation(), Sound.VILLAGER_IDLE, 0.5F, 1.0F);

                new CosmeticsGui(this.owner, this.plugin, this.category).open();
            }
        };
    }
}
