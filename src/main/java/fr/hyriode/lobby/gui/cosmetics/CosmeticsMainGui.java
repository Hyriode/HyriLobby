package fr.hyriode.lobby.gui.cosmetics;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.cosmetics.HyriCosmetics;
import fr.hyriode.cosmetics.common.Cosmetic;
import fr.hyriode.cosmetics.user.CosmeticUser;
import fr.hyriode.cosmetics.utils.StringUtil;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyrameMessage;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class CosmeticsMainGui extends LobbyGUI {

    private final CosmeticUser user;

    public CosmeticsMainGui(Player owner, HyriLobby plugin) {
        super(owner, plugin, name(owner, "gui.cosmetic.name"), 9 * 6);
        this.user = HyriCosmetics.get().getUserProvider().getUser(owner.getUniqueId());

        this.init();
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        this.setItem(51, new ItemBuilder(Material.BARRIER)
                .withName(ChatColor.RED + HyriLanguageMessage.get("gui.cosmetic.unequip.all").getValue(owner))
                .build(), event -> user.unequipCosmetics(false));

        this.addCategoriesItem();
    }

    private void addCategoriesItem() {
        HyriCosmetics.get().getCosmetics().forEach((category, cosmetics) -> {
            final List<Cosmetic> unlockedCosmetics = user.getUnlockedCosmetics(category);
            this.setItem(
                    category.getGuiSlot(),
                    new ItemBuilder(category.getIcon())
                            .withName("§b" + category.getTranslatedName().getValue(owner))
                            .withLore(StringUtil.splitIntoPhrases(category.getTranslatedDescription().getValue(owner), 40))
                            .appendLore("§7")
                            .appendLore(HyriLanguageMessage.get("gui.cosmetic.unlocked").getValue(owner)
                                    .replace("%percentage%", String.valueOf((int) (((double) unlockedCosmetics.size() / (cosmetics.size() == 0.0D ? 1.0D : (double) cosmetics.size())) * 100.0D)))
                                    .replace("%unlocked%", String.valueOf(unlockedCosmetics.size()))
                                    .replace("%total%", String.valueOf(cosmetics.size()))
                            )
                            .appendLore("")
                            .appendLore(HyrameMessage.CLICK_TO_SEE.asString(owner))
                            .build()
                    , event -> new CosmeticsGui(owner, plugin, category).open());
        });
    }
}