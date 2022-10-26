package fr.hyriode.lobby.gui.store;

import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.booster.StoreBooster;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.store.StoreCategory;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by AstFaster
 * on 01/07/2022 at 19:24
 */
public class BoostersGUI extends LobbyGUI {

    private final StoreBooster booster;

    public BoostersGUI(Player owner, HyriLobby plugin) {
        super(owner, plugin, () -> "store-boosters", 54);

        final StoreCategory category = new StoreCategory(null, "boosters");

        category.addContent(this.booster = new StoreBooster(category.getId()));

        this.init();
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        this.addBoosterItem();
        this.addTypeItem();
        this.addGlobalItem();
    }

    private void addBoosterItem() {
        this.setItem(22, this.booster.createItem(this.account), event -> this.booster.purchase(this.plugin, this.owner));
    }

    private void addTypeItem() {
        final List<String> lore = LobbyMessage.STORE_BOOSTERS_TYPE_DESCRIPTION.asList(this.account);
        final List<String> values = new ArrayList<>();

        for (StoreBooster.Type type : StoreBooster.Type.values()) {
            values.add(ChatColor.DARK_GRAY + Symbols.DOT_BOLD + " " + (this.booster.getType() == type ? ChatColor.AQUA : ChatColor.GRAY) + type.format());
        }

        lore.addAll(lore.indexOf("%values%"), values);
        lore.remove("%values%");

        final ItemStack item = new ItemBuilder(Material.PAPER)
                .withName(LobbyMessage.STORE_BOOSTERS_TYPE_NAME.asString(this.account))
                .withLore(lore)
                .build();

        this.setItem(30, item, event -> {
            final List<StoreBooster.Type> types = Arrays.asList(StoreBooster.Type.values());

            int nextIndex = types.indexOf(this.booster.getType()) + 1;

            if (nextIndex >= types.size()) {
                nextIndex = 0;
            }

            this.owner.playSound(this.owner.getLocation(), Sound.CLICK, 0.5F, 2.0F);
            this.booster.setType(types.get(nextIndex));

            this.addBoosterItem();
            this.addTypeItem();
        });
    }

    private void addGlobalItem() {
        final List<String> lore = ListReplacer.replace(LobbyMessage.STORE_BOOSTERS_GLOBAL_DESCRIPTION.asList(this.account), "%yes_color%", String.valueOf(this.booster.isGlobal() ? ChatColor.AQUA : ChatColor.GRAY))
                .replace("%no_color%", String.valueOf(!this.booster.isGlobal() ? ChatColor.AQUA : ChatColor.GRAY))
                .list();
        final ItemStack item = ItemBuilder.asHead(UsefulHead.GLOBE)
                .withName(LobbyMessage.STORE_BOOSTERS_GLOBAL_NAME.asString(this.account))
                .withLore(lore)
                .build();

        this.setItem(32, item, event -> {
            this.owner.playSound(this.owner.getLocation(), Sound.CLICK, 0.5F, 2.0F);
            this.booster.setGlobal(!this.booster.isGlobal());

            this.addBoosterItem();
            this.addGlobalItem();
        });
    }

}
