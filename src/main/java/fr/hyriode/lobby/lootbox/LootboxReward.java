package fr.hyriode.lobby.lootbox;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.cosmetics.HyriCosmetics;
import fr.hyriode.cosmetics.common.CosmeticInfo;
import fr.hyriode.cosmetics.common.CosmeticRarity;
import fr.hyriode.cosmetics.user.CosmeticUser;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.booster.StoreBooster;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by AstFaster
 * on 21/04/2023 at 14:54
 */
public enum LootboxReward {

    ONE_STAR(new HyrisItem(2000L, 51.5D),
            new CosmeticsItem(CosmeticRarity.COMMON, 25.5D, 2000L),
            new BoosterItem(StoreBooster.Type.ONE_FIVE, 5.0D),
            new HostsItem(1, 7.0D),
            new LootboxItem(Lootbox.TWO_STARS, 1, 5.5D),
            new LootboxItem(Lootbox.THREE_STARS, 1, 3.5D),
            new LootboxItem(Lootbox.FOUR_STARS, 1, 1.5D),
            new LootboxItem(Lootbox.FIVE_STARS, 1, 0.5D),
            new HyodesItem(250L, 0.5D)
    ),

    TWO_STARS(new HyrisItem(5000L, 48.0D),
            new CosmeticsItem(CosmeticRarity.COMMON, 28.5D, 5000L),
            new BoosterItem(StoreBooster.Type.ONE_FIVE, 5.0D),
            new HostsItem(1, 7.0D),
            new LootboxItem(Lootbox.ONE_STAR, 1, 5.5D),
            new LootboxItem(Lootbox.THREE_STARS, 1, 3.5D),
            new LootboxItem(Lootbox.FOUR_STARS, 1, 1.5D),
            new LootboxItem(Lootbox.FIVE_STARS, 1, 0.5D),
            new HyodesItem(300L, 0.5D)),

    THREE_STARS(new HyrisItem(8000L, 40.0D),
            new CosmeticsItem(CosmeticRarity.COMMON, 26.5D, 8000L),
            new CosmeticsItem(CosmeticRarity.RARE, 9.5D, 8000L),
            new BoosterItem(StoreBooster.Type.TWO, 5.0D),
            new HostsItem(2, 7.0D),
            new LootboxItem(Lootbox.ONE_STAR, 2, 5.5D),
            new LootboxItem(Lootbox.TWO_STARS, 1, 3.5D),
            new LootboxItem(Lootbox.FOUR_STARS, 1, 1.5D),
            new LootboxItem(Lootbox.FIVE_STARS, 1, 0.5D),
            new HyodesItem(350L, 1.0D)),

    FOUR_STARS(new HyrisItem(8000L, 37.0D),
            new CosmeticsItem(CosmeticRarity.COMMON, 16.0D, 8000L),
            new CosmeticsItem(CosmeticRarity.RARE, 8.5D, 8000L),
            new CosmeticsItem(CosmeticRarity.EPIC, 6.5D, 8000L),
            new CosmeticsItem(CosmeticRarity.LEGENDARY, 4.5D, 8000L),
            new BoosterItem(StoreBooster.Type.TWO, 3.5D),
            new BoosterItem(StoreBooster.Type.TWO_FIVE, 5.0D),
            new HostsItem(3, 7.0D),
            new LootboxItem(Lootbox.ONE_STAR, 2, 5.5D),
            new LootboxItem(Lootbox.TWO_STARS, 2, 3.5D),
            new LootboxItem(Lootbox.THREE_STARS, 1, 1.5D),
            new LootboxItem(Lootbox.FIVE_STARS, 1, 0.5D),
            new HyodesItem(500L, 1.0D)),

    FIVE_STARS(new HyrisItem(8000L, 32.0D),
            new CosmeticsItem(CosmeticRarity.COMMON, 13.0D, 8000L),
            new CosmeticsItem(CosmeticRarity.RARE, 8.5D, 8000L),
            new CosmeticsItem(CosmeticRarity.EPIC, 9.5D, 8000L),
            new CosmeticsItem(CosmeticRarity.LEGENDARY, 7.5D, 8000L),
            new BoosterItem(StoreBooster.Type.TWO_FIVE, 3.5D),
            new BoosterItem(StoreBooster.Type.THREE, 6.0D),
            new HostsItem(3, 8.0D),
            new LootboxItem(Lootbox.ONE_STAR, 2, 5.5D),
            new LootboxItem(Lootbox.TWO_STARS, 2, 3.5D),
            new LootboxItem(Lootbox.THREE_STARS, 2, 1.5D),
            new LootboxItem(Lootbox.FOUR_STARS, 1, 0.5D),
            new HyodesItem(700L, 1.0D));

    private final List<Item> items;

    LootboxReward(Item... items) {
        this.items = Arrays.asList(items);
    }

    public Item getRandomItem() {
        final double chance = ThreadLocalRandom.current().nextDouble() * 100.0D;
        double cumulative = 0.0D;

        for (Item item : this.items) {
            cumulative += item.getProbability();

            if (chance < cumulative) {
                return item;
            }
        }
        return this.getRandomItem();
    }

    public List<Item> getItems() {
        return this.items;
    }

    public static abstract class Item {

        private final HyriLanguageMessage name;
        private final ItemStack icon;
        private final double probability;

        public Item(HyriLanguageMessage name, ItemStack icon, double probability) {
            this.name = name;
            this.icon = icon;
            this.probability = probability;
        }

        public abstract void give(LootboxGiveContext context);

        public ItemStack getIcon() {
            return this.icon.clone();
        }

        public ItemStack createItem(Player player) {
            return new ItemBuilder(this.icon.clone())
                    .withName(this.getName(player))
                    .withLore(this.getDescription(player))
                    .build();
        }

        public String getName(Player player) {
            return this.name.getValue(player);
        }

        public List<String> getDescription(Player player) {
            return new ArrayList<>();
        }

        public double getProbability() {
            return this.probability;
        }

    }

    public static class HyrisItem extends Item {

        private final long amount;

        public HyrisItem(long amount, double probability) {
            super(HyriLanguageMessage.get("lootbox-reward.hyris.name"), new ItemStack(Material.GOLD_INGOT), probability);
            this.amount = amount;
        }

        @Override
        public ItemStack createItem(Player player) {
            return super.createItem(player);
        }

        @Override
        public String getName(Player player) {
            return super.getName(player).replace("%amount%", String.valueOf(this.amount));
        }

        @Override
        public void give(LootboxGiveContext context) {
            context.getPlayer().getHyris().add(this.amount).withMultiplier(false).exec();
        }

    }

    public static class HyodesItem extends Item {

        private final long amount;

        public HyodesItem(long amount, double probability) {
            super(HyriLanguageMessage.get("lootbox-reward.hyodes.name"), new ItemStack(Material.EMERALD), probability);
            this.amount = amount;
        }

        @Override
        public String getName(Player player) {
            return super.getName(player).replace("%amount%", String.valueOf(this.amount));
        }

        @Override
        public void give(LootboxGiveContext context) {
            context.getPlayer().getHyris().add(this.amount).withMultiplier(false).exec();
        }

    }

    public static class CosmeticsItem extends Item {

        private final CosmeticRarity rarity;
        private final long compensation;

        public CosmeticsItem(CosmeticRarity rarity, double probability, long compensation) {
            super(HyriLanguageMessage.get("lootbox-reward.cosmetics.name"), ItemBuilder.asHead(UsefulHead.COSMETICS_CHEST).build(), probability);
            this.rarity = rarity;
            this.compensation = compensation;
        }

        @Override
        public List<String> getDescription(Player player) {
            final List<String> description = Arrays.asList(HyriLanguageMessage.get("lootbox-reward.cosmetics.description").getValue(player).split("\n"));

            return ListReplacer.replace(description, "%rarity%", this.rarity.getTranslatedName(player)).list();
        }

        @Override
        public void give(LootboxGiveContext context) {
            context.setMessage(false);

            final IHyriPlayer account = context.getPlayer();
            final List<CosmeticInfo> cosmetics = new ArrayList<>();

            for (List<CosmeticInfo> values : HyriCosmetics.get().getRegistry().getCosmetics().values()) {
                for (CosmeticInfo cosmetic : values) {
                    if (!cosmetic.isPurchasable() || cosmetic.isOnlyWithRank()) {
                        continue;
                    }

                    if (cosmetic.getRarity() == rarity) {
                        cosmetics.add(cosmetic);
                    }
                }
            }

            if (cosmetics.size() == 0) {
                return;
            }

            final CosmeticInfo randomCosmetic = cosmetics.get(ThreadLocalRandom.current().nextInt(cosmetics.size()));
            final Player player = Bukkit.getPlayer(account.getUniqueId());
            final CosmeticUser cosmeticUser = HyriCosmetics.get().getUserProvider().getUser(player);

            if (cosmeticUser.hasUnlockedCosmetic(randomCosmetic)) {
                account.getHyris().add(this.compensation).withMultiplier(false).exec();

                player.sendMessage(LobbyMessage.LOOTBOX_COSMETIC_ALREADY_OWNED_MESSAGE.asString(account)
                        .replace("%cosmetic%", randomCosmetic.getTranslatedName().getValue(account)));

                context.setFloatingItem(new ItemStack(Material.BARRIER));
                context.setFloatingText(ChatColor.LIGHT_PURPLE + "+" + compensation + " Hyris");
            } else {
                cosmeticUser.addUnlockedCosmetic(randomCosmetic);

                player.sendMessage(LobbyMessage.LOOTBOX_COSMETIC_REWARD_MESSAGE.asString(account)
                        .replace("%cosmetic%", randomCosmetic.getTranslatedName().getValue(account)));

                context.setFloatingItem(randomCosmetic.getIcon());
                context.setFloatingText(randomCosmetic.getTranslatedName().getValue(account));
            }
        }

    }

    public static class BoosterItem extends Item {

        private final StoreBooster.Type booster;

        public BoosterItem(StoreBooster.Type booster, double probability) {
            super(HyriLanguageMessage.get("lootbox-reward.booster.name"), new ItemStack(Material.POTION), probability);
            this.booster = booster;
        }

        @Override
        public String getName(Player player) {
            return super.getName(player).replace("%boost%", this.booster.format());
        }

        @Override
        public List<String> getDescription(Player player) {
            final List<String> description = Arrays.asList(HyriLanguageMessage.get("lootbox-reward.booster.description").getValue(player).split("\n"));

            return ListReplacer.replace(description, "%boost%", this.booster.format())
                    .replace("%multiplier%", String.valueOf(this.booster.getMultiplier()))
                    .list();
        }

        @Override
        public void give(LootboxGiveContext context) {
            HyriAPI.get().getBoosterManager().giveBooster(context.getPlayer(), this.booster.getMultiplier(), 3600);
        }

    }

    public static class LootboxItem extends Item {

        private final Lootbox lootbox;
        private final int amount;

        public LootboxItem(Lootbox lootbox, int amount, double probability) {
            super(HyriLanguageMessage.get("lootbox-reward.lootbox.name"), ItemBuilder.asHead(UsefulHead.ENDER_CHEST).build(), probability);
            this.lootbox = lootbox;
            this.amount = amount;
        }

        @Override
        public String getName(Player player) {
            return super.getName(player)
                    .replace("%amount%", String.valueOf(this.amount))
                    .replace("%stars%", this.lootbox.format());
        }

        @Override
        public void give(LootboxGiveContext context) {
            for (int i = 0; i < this.amount; i++) {
                HyriAPI.get().getLootboxManager().giveLootbox(context.getPlayer(), this.lootbox.getRarity());
            }
        }

    }

    public static class HostsItem extends Item {

        private final int tickets;

        public HostsItem(int tickets, double probability) {
            super(HyriLanguageMessage.get("lootbox-reward.hosts.name"), new ItemStack(Material.NAME_TAG), probability);
            this.tickets = tickets;
        }

        @Override
        public String getName(Player player) {
            return super.getName(player).replace("%tickets%", String.valueOf(this.tickets));
        }

        @Override
        public void give(LootboxGiveContext context) {
            context.getPlayer().getHosts().addAvailableHosts(this.tickets);
        }

    }

}
