package fr.hyriode.lobby.vip.casino.game.inventory;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.item.ItemHead;
import fr.hyriode.hyrame.language.HyrameMessage;
import fr.hyriode.hyrame.utils.HyrameHead;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.vip.casino.game.AGame;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

class GameInventory extends HyriInventory {

    private final int minimum;
    private final int maximum;
    private final int[] modifiers;
    private final InventoryValidatorCallback callback;
    private int value = 0;

    protected GameInventory(AGame game, int size, int minimum, int maximum, int[] modifiers, InventoryValidatorCallback callback) {
        super(game.getPlayer(), game.getName(), size);

        this.modifiers = modifiers;
        this.minimum = minimum;
        this.maximum = maximum;
        this.callback = callback;

        this.addItems();
    }

    private void addItems() {
        this.setItem(22, new ItemBuilder(Material.STAINED_CLAY, 1, (short) 5).withName(LobbyMessage.CASINO_CONFIRM_BET.asString(this.owner)).build(), (event) -> {
            if(this.value != 0) {
                this.callback.call(this.value);
            }
        });
        this.setItem(49, (new ItemBuilder(Material.ARROW)).withName(HyrameMessage.GO_BACK.asString(this.owner)).build(), (event) -> {
            if(event.isRightClick()) return;
            this.owner.getOpenInventory().close();
            this.owner.sendMessage(LobbyMessage.CASINO_CANCEL_BET.asString(this.owner));

        });

        this.createItem(30, 0, false, HyrameHead.WHITE_MINUS, ChatColor.AQUA);
        this.createItem(29, 1, false, HyrameHead.GRAY_MINUS, ChatColor.DARK_AQUA);
        this.createItem(32, 0, true, HyrameHead.WHITE_PLUS, ChatColor.AQUA);
        this.createItem(33, 1, true, HyrameHead.GRAY_PLUS, ChatColor.DARK_AQUA);

        if(this.modifiers.length == 3) {
            this.createItem(28, 2, false, HyrameHead.WHITE_MINUS, ChatColor.DARK_AQUA);
            this.createItem(34, 2, true, HyrameHead.WHITE_PLUS, ChatColor.AQUA);
        }
    }

    private void createItem(int slot, int index, boolean plus, ItemHead head, ChatColor color) {
        int modifier = this.modifiers[index];
        ItemStack itemStack = ItemBuilder.asHead(head).withName(color + (plus ? "+" : "-") + modifier)
                .withLore(ListReplacer.replace(HyrameMessage.HOST_MULTIPLE_MODIFIERS_LORE.asList(this.owner),
                                "%value%",
                                String.valueOf(this.value))
                        .replace("%maximum%", String.valueOf(this.maximum))
                        .replace("%minimum%", String.valueOf(this.minimum)).list())
                .build();
        this.setItem(slot, itemStack, (event) -> {
            this.value = plus ? this.value + modifier  : this.value - modifier;
            if(this.value > this.maximum) {
                this.value = this.maximum;
            } else if(this.value < this.minimum) {
                this.value = this.minimum;
            }
            this.addItems();
            this.owner.playSound(this.owner.getLocation(), Sound.NOTE_STICKS, 0.5F, 1.0F);
        });
    }
}
