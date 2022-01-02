package fr.hyriode.lobby.gui;

import com.google.common.base.MoreObjects;
import fr.hyriode.hyrame.game.IHyriGameManager;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.api.chooser.GameChooserMenu;
import fr.hyriode.lobby.api.chooser.GameItem;
import fr.hyriode.lobby.api.player.LobbyPlayer;
import fr.hyriode.lobby.api.player.LobbyPlayerManager;
import fr.hyriode.lobby.gui.chooser.GameCustomizerGui;
import fr.hyriode.lobby.util.UsefulHeads;
import fr.hyriode.tools.inventory.AbstractInventory;
import fr.hyriode.tools.item.ItemBuilder;
import fr.hyriode.tools.item.enchant.HyriEnchant;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GameChooserGui extends AbstractInventory {

    private final List<Integer> dontFill = Arrays.asList(28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);
    private final List<Integer> enchantedSlots;

    public final Player player;
    public final IHyriGameManager gm;
    public final GameChooserMenu menu;
    public final IHyriLanguageManager lang;

    public final LobbyPlayer lp;
    public final LobbyPlayerManager pm;

    public final ItemStack fillItem;
    public final ItemStack currentItem;

    public boolean isCustomizeEnabled;

    public GameChooserGui(HyriLobby plugin, Player owner) {
        super(owner, plugin.getHyrame().getLanguageManager().getValue(owner, "title.chooser.gui"), 54);

        this.player = owner;
        this.gm = plugin.getHyrame().getGameManager();
        this.lang = plugin.getHyrame().getLanguageManager();

        final UUID uuid = owner.getUniqueId();
        this.pm = plugin.getLobbyAPI().getPlayerManager();
        this.lp = MoreObjects.firstNonNull(this.pm.getPlayer(uuid), this.pm.createPlayer(uuid));

        this.menu = this.lp.getMenu();

        this.enchantedSlots = new ArrayList<>();
        this.menu.getGames().keySet().forEach(slot -> {
            this.enchantedSlots.add(slot + 10);
        });
        this.enchantedSlots.add(53);

        this.fillItem = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15).withName(" ").build();
        this.currentItem = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 3).withName(" ").build();

        this.isCustomizeEnabled = false;

        this.init();
    }

    private void init() {
        this.inventory.clear();
        this.setItem(4, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.EARTH.getTexture())
                .withName(this.lang.getValue(this.player, "title.chooser.gui")).build()
        );

        this.setItem(53, new ItemBuilder(Material.BLAZE_POWDER).withName(this.lang.getValue(this.player, "item.chooser.customize")).build(), e -> {
            this.switchWithEnchantedItems(this.isCustomizeEnabled = !this.isCustomizeEnabled, this.enchantedSlots);
        });

        this.addGameItems();
        this.setCustomFill(false);
    }

    private void addGameItems() {
        this.menu.getGames().forEach((itemSlot, item) -> {
            final int slot = itemSlot + 10;
            this.setItem(slot, new ItemBuilder(Material.getMaterial(item.getMaterial())).withName("§f" + item.getName()).build(), e -> {
                this.onGameClick(slot, Material.getMaterial(item.getMaterial()), item.getName(), item.getModes());
            });
        });
    }

    private void onGameClick(int itemSlot, Material material, String gameName, List<String> modes) {
        if (this.isCustomizeEnabled) {
            this.onEnchantedGameClick(itemSlot);
            return;
        }

        this.dontFill.forEach(slot -> this.setItem(slot, new ItemStack(Material.AIR)));

        final int[] slot = {27};
        modes.forEach(type -> {
            //Check for GUI style
            if (slot[0] != 26 && slot[0] != 35 && slot[0] != 34 && slot[0] != 43) {
                this.setItem(slot[0] += 1, new ItemBuilder(material).withName("§f" + gameName + " " + type).build(), e -> {
                    this.player.sendMessage(gameName + " " + type + ": " + this.gm.getGames(gameName.toLowerCase(), type));
                });
            }
        });

        this.inventory.remove(this.currentItem);
        this.setItem(itemSlot + 9, this.currentItem);

        this.setCustomFill(true);
    }

    private void onEnchantedGameClick(int slot) {
        new GameCustomizerGui(this.menu.getGames().get(slot - 10), slot, this).open();
    }

    public void enchantedGameClickCallback(GameItem oldItem, int oldSlot, GameItem newItem, int newSlot) {
        this.menu.setGame(oldSlot, newItem);
        this.menu.setGame(newSlot, oldItem);

        this.init();
        this.switchWithEnchantedItems(this.isCustomizeEnabled, this.enchantedSlots);
    }

    private void setCustomFill(boolean selected) {
        for (int i = 0; i < this.inventory.getSize(); i++) {
            if (this.inventory.getItem(i) == null) {
                //Check for GUI style
                if (selected) {
                    if (!this.dontFill.contains(i)) {
                        this.setItem(i, this.fillItem);
                    }
                } else {
                    this.setItem(i, this.fillItem);
                }
            }
        }
    }

    private void switchWithEnchantedItems(boolean toEnchanted, List<Integer> slots) {
        if (toEnchanted) {
            slots.forEach(slot -> {
                final ItemStack item = this.inventory.getItem(slot);
                if (item != null) {
                    this.setItem(slot, new ItemBuilder(item).withName(this.getCustomizerName(false, item)).withGlow().build(), this.getClickConsumers().get(slot));
                }
            });
        } else {
            slots.forEach(slot -> {
                ItemStack item = this.inventory.getItem(slot);
                if (item != null) {
                    item.removeEnchantment(HyriEnchant.GLOW);
                    this.setItem(slot, new ItemBuilder(item).withName(this.getCustomizerName(true, item)).build(), this.getClickConsumers().get(slot));
                }
            });
        }
    }

    private String getCustomizerName(boolean isEnchanted, ItemStack item) {
        final String displayName = item.getItemMeta().getDisplayName();
        if (item.getType() == Material.BLAZE_POWDER) {
            final String name;
            if (isEnchanted) {
                name = displayName.equalsIgnoreCase(this.lang.getValue(this.player, "item.customize.replacer"))
                        ? this.lang.getValue(this.player, "item.chooser.customize") : displayName;
            } else {
                name = item.getItemMeta().getDisplayName().equalsIgnoreCase(this.lang.getValue(this.player, "item.chooser.customize"))
                        ? this.lang.getValue(this.player, "item.customize.replacer") : displayName;
            }
            return name;
        }
        return displayName;
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        this.pm.sendPlayer(this.lp);
    }
}
