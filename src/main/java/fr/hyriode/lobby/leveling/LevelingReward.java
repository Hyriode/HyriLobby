package fr.hyriode.lobby.leveling;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.lootbox.HyriLootboxRarity;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.utils.Symbols;
import org.bukkit.ChatColor;

import java.util.function.Supplier;

/**
 * Created by AstFaster
 * on 24/10/2022 at 16:06
 */
public enum LevelingReward {

    LEVEL_1(1, new HyrisHandler(1000L)),
    LEVEL_2(2, new HyrisHandler(1300L)),
    LEVEL_3(3, new HyrisHandler(1600L)),
    LEVEL_4(4, new HyrisHandler(2000L)),
    LEVEL_5(5, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.ONE_STAR, 1))),
    LEVEL_6(6, new CosmeticHandler(0L)),
    LEVEL_7(7, new PlusColorHandler(HyriChatColor.AQUA)),
    LEVEL_8(8, new BoosterHandler(new BoosterHandler.Data(1, 1.75D, 3600))),
    LEVEL_9(9, new HyrisHandler(2800L)),
    LEVEL_10(10, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.ONE_STAR, 2))),
    LEVEL_11(11, null),
    LEVEL_12(12, new BoosterHandler(new BoosterHandler.Data(1, 1.75D, 3600))),
    LEVEL_13(13, null),
    LEVEL_14(14, new PlusColorHandler(HyriChatColor.WHITE)),
    LEVEL_15(15, new HyrisHandler(3300L)),
    LEVEL_16(16, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.ONE_STAR, 2))),
    LEVEL_17(17, new BoosterHandler(new BoosterHandler.Data(1, 1.75, 7200))),
    LEVEL_18(18, new CosmeticHandler(0L)),
    LEVEL_19(19, new HyrisHandler(4000L)),
    LEVEL_20(20, new BoosterHandler(new BoosterHandler.Data(1, 1.75, 7200))),
    LEVEL_21(21, new PlusColorHandler(HyriChatColor.RED)),
    LEVEL_22(22, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.TWO_STARS, 1))),
    LEVEL_23(23, null),
    LEVEL_24(24, new BoosterHandler(new BoosterHandler.Data(1, 1.75, 10800))),
    LEVEL_25(25, new HyrisHandler(5000L)),
    LEVEL_26(26, null),
    LEVEL_27(27, new CosmeticHandler(0L)),
    LEVEL_28(28, new PlusColorHandler(HyriChatColor.GRAY)),
    LEVEL_29(29, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.TWO_STARS, 2))),
    LEVEL_30(30, new BoosterHandler(new BoosterHandler.Data(1, 1.75, 10800))),
    LEVEL_31(31, new HyrisHandler(6000L)),
    LEVEL_32(32, null),
    LEVEL_33(33, null),
    LEVEL_34(34, new BoosterHandler(new BoosterHandler.Data(1, 1.75, 14400))),
    LEVEL_35(35, new PlusColorHandler(HyriChatColor.BLUE)),
    LEVEL_36(36, new HyrisHandler(7000L)),
    LEVEL_37(37, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.TWO_STARS, 4))),
    LEVEL_38(38, new CosmeticHandler(0L)),
    LEVEL_39(39, new BoosterHandler(new BoosterHandler.Data(1, 2.25D, 3600))),
    LEVEL_40(40, new HostHandler(1)),
    LEVEL_41(41, new HyrisHandler(8000L)),
    LEVEL_42(42, new PlusColorHandler(HyriChatColor.YELLOW)),
    LEVEL_43(43, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.THREE_STARS, 1))),
    LEVEL_44(44, new BoosterHandler(new BoosterHandler.Data(1, 2.25D, 3600))),
    LEVEL_45(45, null),
    LEVEL_46(46, new HyrisHandler(9000L)),
    LEVEL_47(47, new CosmeticHandler(0L)),
    LEVEL_48(48, new BoosterHandler(new BoosterHandler.Data(1, 2.25D, 7200))),
    LEVEL_49(49, new PlusColorHandler(HyriChatColor.DARK_GRAY)),
    LEVEL_50(50, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.THREE_STARS, 2))),
    LEVEL_51(51, null),
    LEVEL_52(52, new HyrisHandler(10000L)),
    LEVEL_53(53, new BoosterHandler(new BoosterHandler.Data(1, 2.25D, 7200))),
    LEVEL_54(54, null),
    LEVEL_55(55, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.THREE_STARS, 4))),
    LEVEL_56(56, new PlusColorHandler(HyriChatColor.GREEN)),
    LEVEL_57(57, new HyrisHandler(10000L)),
    LEVEL_58(58, new BoosterHandler(new BoosterHandler.Data(1, 2.25D, 10800))),
    LEVEL_59(59, new CosmeticHandler(0L)),
    LEVEL_60(60, null),
    LEVEL_61(61, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.FOUR_STARS, 1))),
    LEVEL_62(62, new HyrisHandler(10000L)),
    LEVEL_63(63, new PlusColorHandler(HyriChatColor.DARK_RED)),
    LEVEL_64(64, new BoosterHandler(new BoosterHandler.Data(1, 2.25D, 10800))),
    LEVEL_65(65, new HostHandler(1)),
    LEVEL_66(66, null),
    LEVEL_67(67, new HyrisHandler(10000L)),
    LEVEL_68(68, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.FOUR_STARS, 2))),
    LEVEL_69(69, new CosmeticHandler(0L)),
    LEVEL_70(70, new PlusColorHandler(HyriChatColor.DARK_GREEN)),
    LEVEL_71(71, new BoosterHandler(new BoosterHandler.Data(1, 2.25D, 14400))),
    LEVEL_72(72, new HyrisHandler(11000L)),
    LEVEL_73(73, null),
    LEVEL_74(74, new BoosterHandler(new BoosterHandler.Data(1, 2.75D, 3600))),
    LEVEL_75(75, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.FOUR_STARS, 4))),
    LEVEL_76(76, new CosmeticHandler(0L)),
    LEVEL_77(77, new PlusColorHandler(HyriChatColor.DARK_AQUA)),
    LEVEL_78(78, new HyrisHandler(12500L)),
    LEVEL_79(79, new BoosterHandler(new BoosterHandler.Data(1, 2.75D, 3600))),
    LEVEL_80(80, null),
    LEVEL_81(81, new HyrisHandler(14000L)),
    LEVEL_82(82, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.FIVE_STARS, 1))),
    LEVEL_83(83, null),
    LEVEL_84(84, new PlusColorHandler(HyriChatColor.BLACK)),
    LEVEL_85(85, new BoosterHandler(new BoosterHandler.Data(1, 2.75D, 7200))),
    LEVEL_86(86, new HostHandler(1)),
    LEVEL_87(87, new HyrisHandler(16000L)),
    LEVEL_88(88, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.FIVE_STARS, 2))),
    LEVEL_89(89, new CosmeticHandler(0L)),
    LEVEL_90(90, new BoosterHandler(new BoosterHandler.Data(1, 2.75D, 7200))),
    LEVEL_91(91, new PlusColorHandler(HyriChatColor.DARK_PURPLE)),
    LEVEL_92(92, new HyrisHandler(19000L)),
    LEVEL_93(93, null),
    LEVEL_94(94, new CosmeticHandler(0L)),
    LEVEL_95(95, new LootboxHandler(new LootboxHandler.Data(HyriLootboxRarity.FIVE_STARS, 4))),
    LEVEL_96(96, new BoosterHandler(new BoosterHandler.Data(1, 2.75D, 10800))),
    LEVEL_97(97, new HyrisHandler(25000L)),
    LEVEL_98(98, new PlusColorHandler(HyriChatColor.GOLD)),
    LEVEL_99(99, new CosmeticHandler(0L)),
    LEVEL_100(100, null),

    ;

    public static final LevelingReward[] VALUES = values();

    private final int level;
    private final Handler<?> handler;

    LevelingReward(int level, Handler<?> handler) {
        this.level = level;
        this.handler = handler;
    }

    public int getLevel() {
        return this.level;
    }

    public Handler<?> getHandler() {
        return this.handler;
    }

    public static LevelingReward get(int level) {
        for (LevelingReward reward : VALUES) {
            if (reward.getLevel() == level) {
                return reward;
            }
        }
        return null;
    }

    public static abstract class Handler<T> {

        protected Supplier<HyriLanguageMessage> description;

        protected final String name;
        protected final T data;

        public Handler(String name, T data) {
            this.name = name;
            this.data = data;
            this.description = () -> HyriLanguageMessage.get("leveling." + this.name + ".description");
        }

        public abstract void claim(IHyriPlayer player);

        public abstract String getDescription(IHyriPlayer player);

    }

    private static class HyrisHandler extends Handler<Long> {

        public HyrisHandler(Long data) {
            super("hyris", data);
        }

        @Override
        public void claim(IHyriPlayer player) {
            player.getHyris().add(this.data)
                    .withMultiplier(false)
                    .exec();
        }

        @Override
        public String getDescription(IHyriPlayer player) {
            return this.description.get().getValue(player).replace("%amount%", String.valueOf(this.data));
        }

    }

    private static class CosmeticHandler extends Handler<Long> {

        public CosmeticHandler(Long data) {
            super("cosmetic", data);
        }

        @Override
        public void claim(IHyriPlayer player) {

        }

        @Override
        public String getDescription(IHyriPlayer player) {
            return this.description.get().getValue(player);
        }

    }

    private static class LootboxHandler extends Handler<LootboxHandler.Data> {

        public LootboxHandler(Data data) {
            super("lootbox", data);
        }

        @Override
        public void claim(IHyriPlayer player) {
            for (int i = 0; i < this.data.getAmount(); i++) {
                HyriAPI.get().getLootboxManager().giveLootbox(player, this.data.getRarity());
            }
        }

        @Override
        public String getDescription(IHyriPlayer player) {
            final StringBuilder builder = new StringBuilder();

            for (int i = 1; i <= 5; i++) {
                builder.append(this.data.getRarity().getId() < i ? ChatColor.GRAY : ChatColor.YELLOW).append(Symbols.STAR);
            }

            return this.description.get().getValue(player)
                    .replace("%amount%", String.valueOf(this.data.getAmount()))
                    .replace("%stars%", builder.toString());
        }

        private static class Data {

            private final HyriLootboxRarity rarity;
            private final int amount;

            public Data(HyriLootboxRarity rarity, int amount) {
                this.rarity = rarity;
                this.amount = amount;
            }

            public HyriLootboxRarity getRarity() {
                return this.rarity;
            }

            public int getAmount() {
                return this.amount;
            }

        }

    }

    private static class BoosterHandler extends Handler<BoosterHandler.Data> {

        public BoosterHandler(BoosterHandler.Data data) {
            super("booster", data);
        }

        @Override
        public void claim(IHyriPlayer player) {
            for (int i = 0; i < this.data.getAmount(); i++) {
                HyriAPI.get().getBoosterManager().giveBooster(player, this.data.getMultiplier(), this.data.getDuration());
            }
        }

        @Override
        public String getDescription(IHyriPlayer player) {
            return this.description.get().getValue(player)
                    .replace("%amount%", String.valueOf(this.data.getAmount()))
                    .replace("%boost%", String.valueOf((int) this.data.getMultiplier() * 100))
                    .replace("%multiplier%", String.valueOf(this.data.getMultiplier()));
        }

        private static class Data {

            private final int amount;
            private final double multiplier;
            private final long duration;

            public Data(int amount, double multiplier, long duration) {
                this.amount = amount;
                this.multiplier = multiplier;
                this.duration = duration;
            }

            public int getAmount() {
                return this.amount;
            }

            public double getMultiplier() {
                return this.multiplier;
            }

            public long getDuration() {
                return this.duration;
            }
        }

    }

    private static class PlusColorHandler extends Handler<HyriChatColor> {

        public PlusColorHandler(HyriChatColor data) {
            super("plus-color", data);
        }

        @Override
        public void claim(IHyriPlayer player) {
            player.getHyriPlus().addColor(this.data);
        }

        @Override
        public String getDescription(IHyriPlayer player) {
            return this.description.get().getValue(player).replace("%color%", this.data.toString());
        }

    }

    private static class HostHandler extends Handler<Integer> {

        public HostHandler(int data) {
            super("host", data);
        }

        @Override
        public void claim(IHyriPlayer player) {
            player.getHosts().addAvailableHosts(this.data);
        }

        @Override
        public String getDescription(IHyriPlayer player) {
            return this.description.get().getValue(player).replace("%amount%", String.valueOf(this.data));
        }

    }

}
