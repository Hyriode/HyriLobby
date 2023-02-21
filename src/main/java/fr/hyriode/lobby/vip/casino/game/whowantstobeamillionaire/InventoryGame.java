package fr.hyriode.lobby.vip.casino.game.whowantstobeamillionaire;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.host.option.IntegerOption;
import fr.hyriode.hyrame.host.option.PreciseIntegerOption;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.item.ItemHead;
import fr.hyriode.hyrame.language.HyrameMessage;
import fr.hyriode.hyrame.utils.HyrameHead;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryGame extends HyriInventory {

    private int value;
    private int[] modifiers = new int[] {1, 10, 20};
    private int maximum = 300;
    private int minimum = 0;

    private final WhoWantsToBeAMillionaire game;

    public InventoryGame(Player owner, WhoWantsToBeAMillionaire game) {
        super(owner, "Qui veut gagner des millions ?", 54);

        this.game = game;

        this.setItem(49, (new ItemBuilder(Material.ARROW)).withName(HyrameMessage.GO_BACK.asString(this.owner)).build(), (event) -> {
            this.owner.getOpenInventory().close();
            this.quit();
        });
        this.setItem(31, ItemBuilder.asHead(HyrameHead.GARBAGE_CAN).withName(HyrameMessage.HOST_RESET_NAME.asString(this.owner)).withLore(HyrameMessage.HOST_RESET_LORE.asList(this.owner)).build(), (event) -> {
            this.value = 0;
            this.addItems();
            this.owner.playSound(this.owner.getLocation(), Sound.FIZZ, 0.5F, 1.0F);
        });
        this.addItems();
        this.addItemStack();

    }

    private void addItemStack() {
        this.setItem(22, (new ItemBuilder(this.createItem(this.owner))).removeLoreLines(2).build(), (event) -> this.play());
    }

    private void addItems() {
        this.createItem(30, 0, false, HyrameHead.WHITE_MINUS, ChatColor.AQUA);
        this.createItem(29, 1, false, HyrameHead.GRAY_MINUS, ChatColor.DARK_AQUA);
        this.createItem(32, 0, true, HyrameHead.WHITE_PLUS, ChatColor.AQUA);
        this.createItem(33, 1, true, HyrameHead.GRAY_PLUS, ChatColor.DARK_AQUA);
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
            this.owner.playSound(this.owner.getLocation(), Sound.CLICK, 0.5F, 2.0F);
            this.addItems();
            this.addItemStack();
        });
    }

    private ItemStack createItem(Player player) {
        ItemBuilder builder = new ItemBuilder(this.defaultItemCreation(player));
        List<String> lore = builder.getLore();

        return builder.withLore(lore).build();
    }

    private ItemStack defaultItemCreation(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.STAINED_CLAY, 1, (short) 5)
                .withName("Confirmer votre mise");

        List<String> lore = builder.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.add("");

        lore.add(HyrameMessage.CLICK_TO_EDIT.asString(player));
        return builder.withLore(lore).withAllItemFlags().build();
    }

    private void quit() {
        this.owner.sendMessage("Vous avez annulé votre mise");
    }

    private void play() {
        final IHyriPlayer player = IHyriPlayer.get(this.owner.getUniqueId());
        if(this.value != 0 && this.value <= player.getHyris().getAmount()) {
            player.getHyris().remove(this.value).exec();
            player.update();

            if(this.game.play()) {
                this.owner.getOpenInventory().close();
                new ContinueInventory(this.owner, this.game, (long) (this.value*1.5)).open();
            } else {
                this.owner.sendMessage(ChatColor.RED + "Vous avez perdu");
                    this.owner.getOpenInventory().close();
            }
        }
    }
}
