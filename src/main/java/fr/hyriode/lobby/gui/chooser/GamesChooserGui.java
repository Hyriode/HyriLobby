package fr.hyriode.lobby.gui.chooser;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.hyrame.game.IHyriGameManager;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.player.LobbyPlayer;
import fr.hyriode.lobby.api.player.LobbyPlayerManager;
import fr.hyriode.lobby.gui.utils.GameItem;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHead;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class GamesChooserGui extends LobbyInventory {

    private static final List<Integer> DONT_FILL = Arrays.asList(28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);

    private final IHyriGameManager gm;
    private final IHyriServerManager sm;

    private final LobbyPlayer lp;
    private final Supplier<LobbyPlayerManager> pm;

    private final ItemStack currentItem;
    private final HashMap<Integer, ItemStack> gameItems;

    public GamesChooserGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "games_selector", 54);

        this.gm = plugin.getHyrame().getGameManager();
        this.sm = HyriAPI.get().getServerManager();

        this.pm = () -> LobbyAPI.get().getPlayerManager();
        this.lp = this.pm.get().get(owner.getUniqueId());

        this.currentItem = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 3).withName(" ").build();

        this.gameItems = new HashMap<>();
        final int[] slots = {10};
        GameItem.getRequiredItems().forEach(item -> this.gameItems.put(slots[0]++, item.build()));

        this.init();
    }

    protected void init() {
        this.inventory.clear();

        //Items part
        this.setItem(4, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHead.EARTH.getTexture()).withName(this.getMessage("name")).build());

        this.gameItems.forEach((slot, item) -> {
            final String name = item.getItemMeta().getDisplayName();
            this.setItem(slot, item, e -> this.onGameClick(slot, item.getType(), name, GameItem.getByName(name).getModes()));
        });

        //Fill part
        this.setCustomFill(false);
    }

    private void onGameClick(int itemSlot, Material material, String gameName, List<String> modes) {
        DONT_FILL.forEach(slot -> this.setItem(slot, new ItemStack(Material.AIR)));

        final int[] slot = {27};
        modes.forEach(type -> {
            if (slot[0] != 26 && slot[0] != 35 && slot[0] != 34 && slot[0] != 43) {
                this.setItem(slot[0] += 1, new ItemBuilder(material).withName("§f" + gameName + " " + type).build(), e -> {
                    this.sm.sendPlayerToServer(this.owner.getUniqueId(), this.gm.getGames(gameName.toLowerCase(), type).get(0));
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
                        this.setItem(i, FILL_ITEM);
                    }
                } else {
                    this.setItem(i, FILL_ITEM);
                }
            }
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        this.pm.get().save(this.lp);
    }
}
