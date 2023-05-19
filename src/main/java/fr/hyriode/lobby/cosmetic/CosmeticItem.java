package fr.hyriode.lobby.cosmetic;

import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.cosmetics.HyriCosmetics;
import fr.hyriode.cosmetics.common.CosmeticCategory;
import fr.hyriode.cosmetics.common.CosmeticInfo;
import fr.hyriode.cosmetics.user.CosmeticUser;
import fr.hyriode.cosmetics.utils.StringUtil;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.store.StorePrice;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fr.hyriode.cosmetics.transaction.CosmeticPrice.Currency;

public class CosmeticItem {

    private final CosmeticInfo cosmetic;

    public CosmeticItem(CosmeticInfo cosmetic) {
        this.cosmetic = cosmetic;
    }

    public ItemStack toItemStack(final Player player, boolean withAction) {
        final CosmeticUser user = HyriCosmetics.get().getUserProvider().getUser(player);
        final CosmeticCategory category = this.cosmetic.getCategory();

        final ItemBuilder builder = new ItemBuilder(user.hasUnlockedCosmetic(this.cosmetic) ? this.cosmetic.getIcon() :
                new ItemStack(Material.INK_SACK, 1, (short) 8))
                .withName(ChatColor.RESET + "" + ChatColor.AQUA + this.cosmetic.getTranslatedName().getValue(player))
                .withLore(StringUtil.splitIntoPhrases(this.cosmetic.getTranslatedDescription().getValue(player), 35))
                .appendLore("")
                .appendLore(getRarityInfo(player));

        if (withAction) {
            String footer;
            if (!user.hasUnlockedCosmetic(this.cosmetic)) {
                footer = getUnlockInfo(player, builder);
            } else {
                footer = getEquipInfo(player, user, category, builder);
            }
            builder.appendLore("").appendLore(footer);
        }

        return builder.build();
    }

    private String getRarityInfo(final Player player) {
        final String rarityColor = this.cosmetic.getRarity().getColor().toString();
        final String rarityName = HyriChatColor.BOLD + this.cosmetic.getRarity().getTranslatedName(player).toUpperCase();
        final String rarityLabel = getTranslation(player, "gui.cosmetic.rarity") + ": ";
        return rarityLabel + rarityColor + rarityName;
    }

    private String getUnlockInfo(final Player player, ItemBuilder builder) {
        String footer;
        if (this.cosmetic.isPurchasable() && this.cosmetic.canBuyIt(player)) {
            final String priceInfo = getPriceInfo(player, builder);
            builder.appendLore(priceInfo);
            footer = getTranslation(player, "gui.cosmetic.click_to_buy");
        } else {
            footer = getTranslation(player, "gui.cosmetic.cant_unlock");
        }
        return footer;
    }

    private String getPriceInfo(final Player player, ItemBuilder builder) {
        String priceInfo = "";
        if (this.cosmetic.isPurchasable()) {
            builder.appendLore("");

            final StringBuilder prices = new StringBuilder();
            final List<Map.Entry<Currency, Integer>> entries = new ArrayList<>(this.cosmetic.getPrice().getValues().entrySet());

            for (int i = 0; i < entries.size(); i++) {
                final Map.Entry<Currency, Integer> entry = entries.get(i);

                prices.append(StorePrice.Currency.valueOf(entry.getKey().name()).formatAmount(entry.getValue())).append(i + 1 == entries.size() ? "" : ChatColor.GRAY + " / ");
            }

            priceInfo = prices.toString();
        }
        if (!this.cosmetic.isOnlyWithRank() && this.cosmetic.getRank() != null && this.cosmetic.getRank() != PlayerRank.PLAYER) {
            builder.appendLore(getTranslation(player, "gui.cosmetic.offered_with_rank").replace("%rank%", this.cosmetic.getRank().getDefaultPrefix()));
        }
        return priceInfo;
    }

    private String getEquipInfo(final Player player, final CosmeticUser user, final CosmeticCategory category, ItemBuilder builder) {
        String footer;
        if (user.hasEquippedCosmetic(category) && user.getEquippedCosmetic(category) == this.cosmetic) {
            if (!user.getEquippedCosmetics().get(category).getAbstractCosmetic().hasVariants()) {
                footer = getTranslation(player, "gui.cosmetic.already_equipped");
            } else {
                footer = getTranslation(player, "gui.cosmetic.click_to_edit");
            }
            builder.withGlow();
        } else {
            footer = getTranslation(player, "gui.cosmetic.click_to_equip");
        }
        return footer;
    }

    private String getTranslation(Player player, String key) {
        return HyriLanguageMessage.get(key).getValue(player);
    }


}
