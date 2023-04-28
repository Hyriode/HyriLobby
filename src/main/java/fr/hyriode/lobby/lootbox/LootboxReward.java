package fr.hyriode.lobby.lootbox;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.cosmetics.HyriCosmetics;
import fr.hyriode.cosmetics.common.Cosmetic;
import fr.hyriode.cosmetics.user.CosmeticUser;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.booster.StoreBooster;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by AstFaster
 * on 21/04/2023 at 14:54
 */
public enum LootboxReward {

    ONE_STAR(new HyrisItem(1000L, 35.0D),
            new HyodesItem(100L, 10.0D),
            new BoosterItem(StoreBooster.Type.ONE_FIVE, 10.0D),
            new HostsItem(1, 7.0D),
            new LootboxItem(Lootbox.TWO_STARS, 5.5D),
            new LootboxItem(Lootbox.THREE_STARS, 3.5D),
            new LootboxItem(Lootbox.FOUR_STARS, 1.5D),
            new LootboxItem(Lootbox.FIVE_STARS, 0.5D)
            ),

    TWO_STARS(),
    THREE_STARS(),
    FOUR_STARS(),
    FIVE_STARS();

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

    public List<Item> getSortedItems() {
        return this.items.stream()
                .sorted(Comparator.comparingDouble(Item::getProbability))
                .collect(Collectors.toList());
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

        public abstract void give(IHyriPlayer account);

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
        public void give(IHyriPlayer account) {
            account.getHyris().add(this.amount).withMultiplier(false).exec();
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
        public void give(IHyriPlayer account) {
            account.getHyris().add(this.amount).withMultiplier(false).exec();
        }

    }

    public static class ExperienceItem extends Item {

        private final double experience;

        public ExperienceItem(double experience, double probability) {
            super(HyriLanguageMessage.get("lootbox-reward.experience.name"), new ItemStack(Material.EMERALD), probability);
            this.experience = experience;
        }

        @Override
        public String getName(Player player) {
            return super.getName(player).replace("%amount%", String.valueOf(this.experience));
        }

        @Override
        public void give(IHyriPlayer account) {
            account.getNetworkLeveling().addExperience(this.experience);
            account.update();
        }

    }

    public static class CosmeticItem extends Item {

        private final Cosmetic cosmetic;

        public CosmeticItem(Cosmetic cosmetic, double probability) {
            super(null, null, probability);
            this.cosmetic = cosmetic;
        }

        @Override
        public String getName(Player player) {
            return this.cosmetic.getTranslatedName().getValue(player);
        }

        @Override
        public ItemStack createItem(Player player) {
            return this.cosmetic.toItemStack(Bukkit.getPlayer(player.getUniqueId()), false);
        }

        @Override
        public ItemStack getIcon() {
            return this.cosmetic.getIcon();
        }

        @Override
        public void give(IHyriPlayer account) {
            final CosmeticUser user = HyriCosmetics.get().getUserProvider().getUser(account.getUniqueId());

            user.addUnlockedCosmetic(this.cosmetic);
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
            return ListReplacer.replace(super.getDescription(player), "%boost%", this.booster.format())
                    .replace("%multiplier%", String.valueOf(this.booster.getMultiplier()))
                    .list();
        }

        @Override
        public void give(IHyriPlayer account) {
            HyriAPI.get().getBoosterManager().giveBooster(account, this.booster.getMultiplier(), 3600);
        }

    }

    public static class LootboxItem extends Item {

        private final Lootbox lootbox;

        public LootboxItem(Lootbox lootbox, double probability) {
            super(HyriLanguageMessage.get("lootbox-reward.booster.name"), ItemBuilder.asHead(UsefulHead.ENDER_CHEST).build(), probability);
            this.lootbox = lootbox;
        }

        @Override
        public String getName(Player player) {
            return super.getName(player).replace("%stars%", this.lootbox.format());
        }

        @Override
        public void give(IHyriPlayer account) {
            HyriAPI.get().getLootboxManager().giveLootbox(account, this.lootbox.getRarity());
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
        public void give(IHyriPlayer account) {
            account.getHosts().addAvailableHosts(this.tickets);
            account.update();
        }

    }

}
