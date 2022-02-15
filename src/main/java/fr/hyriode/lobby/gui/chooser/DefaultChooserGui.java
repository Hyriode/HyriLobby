package fr.hyriode.lobby.gui.chooser;

import fr.hyriode.hyrame.game.IHyriGameManager;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.api.chooser.games.GameItem;
import fr.hyriode.lobby.api.items.Item;
import fr.hyriode.lobby.api.player.LobbyPlayer;
import fr.hyriode.lobby.api.player.LobbyPlayerManager;
import fr.hyriode.lobby.util.LobbyInventory;
import fr.hyriode.lobby.util.References;
import fr.hyriode.lobby.util.UsefulHeads;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DefaultChooserGui extends LobbyInventory {

    private static final List<Integer> DONT_FILL = Arrays.asList(28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);

    private final Player player;
    private final IHyriGameManager gm;

    private final LobbyPlayer lp;
    private final LobbyPlayerManager pm;

    private final ItemStack currentItem;
    private final HashMap<Integer, GameItem> gameItems;

    public DefaultChooserGui(HyriLobby plugin, Player owner) {
        super(owner, plugin.getHyrame(), "item.chooser.", "title.chooser.gui", 54);

        this.player = owner;
        this.gm = plugin.getHyrame().getGameManager();

        this.pm = plugin.getLobbyAPI().getPlayerManager();
        this.lp = this.pm.getPlayer(owner.getUniqueId());

        this.currentItem = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 3).withName(" ").build();

        this.gameItems = new HashMap<>();
        final int[] slots = {10};
        this.lp.getMenu().getRequiredItems().forEach(item -> this.gameItems.put(slots[0]++, item));

        this.init();
    }

    protected void init() {
        //TODO Handle custom gui
        if (this.lp.isUsingCustomMenu()) {

        } else {
            this.inventory.clear();

            //Items part
            this.setItem(4, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.EARTH.getTexture()).withName(this.getKey("name")).build());

            //Items with Consumer part
            this.setItem(53, new ItemBuilder(Material.BLAZE_POWDER).withName(this.getKey("customized")).build(), e -> {
                this.lp.setUsingCustomMenu(!this.lp.isUsingCustomMenu());
                this.init();
            });

            this.gameItems.forEach((slot, gameItem) -> {
                final Item item = gameItem.getItem();
                this.setItem(slot, new ItemBuilder(Material.getMaterial(item.getMaterial())).withName("§f" + item.getName()).build(),
                        e -> this.onGameClick(slot, Material.getMaterial(item.getMaterial()), item.getName(), gameItem.getModes())
                );
            });

            //Fill part
            this.setCustomFill(false);
        }
    }

    private void onGameClick(int itemSlot, Material material, String gameName, List<String> modes) {
        DONT_FILL.forEach(slot -> this.setItem(slot, new ItemStack(Material.AIR)));

        final int[] slot = {27};
        modes.forEach(type -> {
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
                if (selected) {
                    if (!DONT_FILL.contains(i)) {
                        this.setItem(i, References.FILL_ITEM);
                    }
                } else {
                    this.setItem(i, References.FILL_ITEM);
                }
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        this.pm.sendPlayer(this.lp);
    }
}
