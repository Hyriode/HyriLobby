package fr.hyriode.lobby.gui.profile;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.color.HyriChatColor;
import fr.hyriode.api.rank.HyriRankUpdatedEvent;
import fr.hyriode.api.rank.hyriplus.HyriPlus;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by AstFaster
 * on 25/10/2022 at 18:24
 */
public class PlusColorGUI extends LobbyGUI {

    public PlusColorGUI(Player owner, HyriLobby plugin) {
        super(owner, plugin, () -> "plus-color", 6 * 9);

        this.init();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        final List<Color> colors = Arrays.asList(Color.values());

        colors.sort(Comparator.comparingInt(Color::getLevel));

        this.addItem(13, colors.get(0));

        int index = 1;
        for (int y = 2; y <= 3; y++) {
            for (int x = 1; x <= 7; x++) {
                if (colors.size() <= index) {
                    break;
                }

                this.addItem(y * 9 + x, colors.get(index));

                index++;
            }
        }
    }

    private void addItem(int slot, Color color) {
        final HyriChatColor initialColor = color.getInitial();
        final HyriPlus hyriPlus = this.account.getHyriPlus();
        final boolean own = hyriPlus.getColors().contains(initialColor);
        final boolean selected = hyriPlus.getColor() == initialColor;
        final String oldPrefix = HyriPlayerRankType.EPIC.getDefaultPrefix() + hyriPlus.getColor() + "+";
        final String newPrefix = HyriPlayerRankType.EPIC.getDefaultPrefix() + initialColor + "+";
        final List<String> lore = ListReplacer.replace(LobbyMessage.HYRIPLUS_COLOR_LORE.asList(this.account), "%old%", oldPrefix)
                .replace("%new%", newPrefix)
                .list();

        lore.add("");

        if (own) {
            lore.add(LobbyMessage.CLICK_TO_CHANGE.asString(this.account));
        } else if (selected) {
            lore.add(LobbyMessage.HYRIPLUS_SELECTED_LINE.asString(this.account));
        }else {
            lore.add(LobbyMessage.HYRIPLUS_NOT_UNLOCKED_LINE.asString(this.account).replace("%level%", String.valueOf(color.getLevel())));
        }

        final ItemBuilder itemStack = new ItemBuilder(color.getItemStack())
                .withName(newPrefix)
                .withLore(lore);

        if (selected) {
            itemStack.withGlow();
        }

        this.setItem(slot, itemStack.build(), event -> {
            if (own) {
                if (selected) {
                    return;
                }

                hyriPlus.setColor(initialColor);

                HyriAPI.get().getEventBus().publish(new HyriRankUpdatedEvent(this.owner.getUniqueId()));

                this.account.update();
                this.owner.sendMessage(LobbyMessage.HYRIPLUS_CHANGED_MESSAGE.asString(this.account));
            } else {
                this.owner.sendMessage(LobbyMessage.HYRIPLUS_NOT_UNLOCKED_MESSAGE.asString(this.account));
            }
        });
    }

    private enum Color {

        BLACK(DyeColor.BLACK, 84),
        DARK_GREEN(DyeColor.GREEN, 70),
        DARK_AQUA(DyeColor.CYAN, 77),
        DARK_RED(new ItemStack(Material.REDSTONE), 63),
        DARK_PURPLE(DyeColor.PURPLE, 91),
        GOLD(DyeColor.ORANGE, 98),
        GRAY(DyeColor.SILVER, 28),
        DARK_GRAY(DyeColor.GRAY, 49),
        BLUE(DyeColor.BLUE, 35),
        GREEN(DyeColor.LIME, 56),
        AQUA(DyeColor.LIGHT_BLUE, 7),
        RED(DyeColor.RED, 21),
        LIGHT_PURPLE(DyeColor.PINK, 0),
        YELLOW(DyeColor.YELLOW, 42),
        WHITE(DyeColor.WHITE, 14);

        private final HyriChatColor initial;
        private final ItemStack itemStack;
        private final int level;

        Color(ItemStack itemStack, int level) {
            this.itemStack = itemStack;
            this.level = level;
            this.initial = HyriChatColor.valueOf(this.name());
        }

        @SuppressWarnings("deprecation")
        Color(DyeColor dye, int level) {
            this(new ItemBuilder(Material.INK_SACK, 1, dye.getDyeData()).build(), level);
        }

        public HyriChatColor getInitial() {
            return this.initial;
        }
        public int getLevel() {
            return this.level;
        }

        public ItemStack getItemStack() {
            return this.itemStack.clone();
        }

    }

}
