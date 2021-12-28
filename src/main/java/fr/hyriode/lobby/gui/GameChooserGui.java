package fr.hyriode.lobby.gui;

import com.google.common.base.MoreObjects;
import fr.hyriode.hyrame.game.IHyriGameManager;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.api.chooser.GameChooserMenu;
import fr.hyriode.lobby.api.player.LobbyPlayerManager;
import fr.hyriode.lobby.util.UsefulHeads;
import fr.hyriode.tools.inventory.AbstractInventory;
import fr.hyriode.tools.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GameChooserGui extends AbstractInventory {

    private final List<Integer> dontFill = Arrays.asList(28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);

    private final Player player;
    private final IHyriGameManager gm;
    private final GameChooserMenu menu;
    private final LobbyPlayerManager pm;
    private final IHyriLanguageManager lang;

    private final ItemStack fillItem;
    private final ItemStack currentItem;

    public GameChooserGui(HyriLobby plugin, Player owner) {
        super(owner, plugin.getHyrame().getLanguageManager().getMessageValueForPlayer(owner, "title.chooser.gui"), 54);

        this.player = owner;
        this.gm = plugin.getHyrame().getGameManager();
        this.pm = plugin.getLobbyAPI().getPlayerManager();
        this.lang = plugin.getHyrame().getLanguageManager();

        final UUID uuid = owner.getUniqueId();
        this.menu = MoreObjects.firstNonNull(this.pm.getPlayer(uuid), this.pm.createPlayer(uuid)).getMenu();

        this.fillItem = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15).withName(" ").build();
        this.currentItem = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 3).withName(" ").build();

        this.addEarthItem();
        this.addCustomizeItem();

        this.addGameItems();

        this.setCustomFill(false);
    }

    private void addEarthItem() {
        this.setItem(4, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.EARTH.getTexture())
                .withName(this.lang.getMessageValueForPlayer(this.player, "title.chooser.gui")).build()
        );
    }

    private void addCustomizeItem() {
        this.setItem(53, new ItemBuilder(Material.BLAZE_POWDER).withName(this.lang.getMessageValueForPlayer(this.player, "item.chooser.customize")).build());
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
}
