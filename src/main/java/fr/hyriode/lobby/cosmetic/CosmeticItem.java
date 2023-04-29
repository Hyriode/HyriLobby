package fr.hyriode.lobby.cosmetic;

import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.cosmetics.HyriCosmetics;
import fr.hyriode.cosmetics.common.Cosmetic;
import fr.hyriode.cosmetics.common.CosmeticCategory;
import fr.hyriode.cosmetics.common.CosmeticInfo;
import fr.hyriode.cosmetics.user.CosmeticUser;
import fr.hyriode.cosmetics.utils.StringUtil;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CosmeticItem {

    private final Cosmetic cosmetic;
    private final CosmeticInfo info;

    public CosmeticItem(Cosmetic cosmetic) {
        this.cosmetic = cosmetic;
        this.info = cosmetic.getInfo();
    }

    public ItemStack toItemStack(final Player player, boolean withAction) {
        final CosmeticUser user = HyriCosmetics.get().getUserProvider().getUser(player);
        final IHyriPlayer hyriPlayer = user.asHyriPlayer();
        final CosmeticCategory category = this.info.getCategory();

        final ItemBuilder builder = new ItemBuilder(user.hasUnlockedCosmetic(this.cosmetic) ? this.info.getIcon() :
                new ItemStack(Material.INK_SACK, 1, (short) 8))
                .withName(ChatColor.RESET + "" + ChatColor.AQUA + this.info.getTranslatedName().getValue(player))
                .withLore(StringUtil.splitIntoPhrases(info.getTranslatedDescription().getValue(player), 35))
                .appendLore("")
                .appendLore(getRarityInfo(player));

        if (withAction) {
            String footer;
            if (!user.hasUnlockedCosmetic(this.cosmetic)) {
                footer = getUnlockInfo(player, hyriPlayer, builder);
            } else {
                footer = getEquipInfo(player, user, category, builder);
            }
            builder.appendLore("").appendLore(footer);
        }

        return builder.build();
    }

    private String getRarityInfo(final Player player) {
        final String rarityColor = this.info.getRarity().getColor().toString();
        final String rarityName = HyriChatColor.BOLD + this.info.getRarity().getTranslatedName(player).toUpperCase();
        final String rarityLabel = getTranslation(player, "gui.cosmetic.rarity") + ": ";
        return rarityLabel + rarityColor + rarityName;
    }

    private String getUnlockInfo(final Player player, final IHyriPlayer hyriPlayer, ItemBuilder builder) {
        String footer;
        if (this.info.isBuyable() && this.info.canBuyIt(player)) {
            final String priceInfo = getPriceInfo(player, hyriPlayer, builder);
            builder.appendLore(priceInfo);
            footer = getTranslation(player, "gui.cosmetic.click_to_buy");
        } else {
            footer = getTranslation(player, "gui.cosmetic.cant_unlock");
        }
        return footer;
    }

    private String getPriceInfo(final Player player, final IHyriPlayer hyriPlayer, ItemBuilder builder) {
        String priceInfo = "";
        if (this.info.isBuyable()) {
            builder.appendLore("");
            if (this.info.getHyodesPrice() > 0 && this.info.getHyrisPrice() > 0) {
                priceInfo = getTranslation(player, "gui.cosmetic.price_two_option")
                        .replace("%price1%", hyriPlayer.getHyris().getColor().toString() + info.getHyrisPrice() + " ⛃")
                        .replace("%price2%", hyriPlayer.getHyodes().getColor().toString() + info.getHyodesPrice() + " ✦");
            } else if (this.info.getHyrisPrice() > 0) {
                priceInfo = getTranslation(player, "gui.cosmetic.price_one_option")
                        .replace("%price1%", hyriPlayer.getHyris().getColor().toString() + info.getHyrisPrice() + " ⛃");
            } else if (this.info.getHyodesPrice() > 0) {
                priceInfo = getTranslation(player, "gui.cosmetic.price_one_option")
                        .replace("%price1%", hyriPlayer.getHyodes().getColor().toString() + info.getHyodesPrice() + " ✦");
            }
        }
        if (!info.isRequireRank() && info.getRank() != null && info.getRank() != PlayerRank.PLAYER) {
            builder.appendLore(getTranslation(player, "gui.cosmetic.offered_with_rank").replace("%rank%", info.getRank().getDefaultPrefix()));
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
