package fr.hyriode.lobby.gui.chooser;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.games.LobbyGame;
import fr.hyriode.lobby.gui.utils.GameItem;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

public class GamesChooserGui extends LobbyInventory {

    private static final List<Integer> DONT_FILL = Arrays.asList(28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);

    public GamesChooserGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "games_selector", 54);

        this.init();
    }

    protected void init() {
        this.inventory.clear();

        this.fillOutline(FILL_ITEM);

        final Set<LobbyGame> games = LobbyAPI.get().getGameRegistry().getValues();
        final Map<ItemStack, Consumer<InventoryClickEvent>> items = new HashMap<>();

        for (LobbyGame game : games) {
            final GameItem item = GameItem.getByName(game.getName());
            final ItemBuilder builder = item.getItem();

            builder.withName(ChatColor.DARK_AQUA + game.getDisplayName());

            items.put(builder.build(), e -> this.onGameClick(e.getSlot(), builder, game));
        }

        this.tryToFill(10, 0, items);
        this.setHorizontalLine(18, 26, FILL_ITEM);
        this.setHorizontalLine(27, 35, FILL_ITEM);
        this.setHorizontalLine(36, 44, FILL_ITEM);
    }

    private void onGameClick(int itemSlot, ItemBuilder item, LobbyGame game) {
        DONT_FILL.forEach(slot -> this.setItem(slot, new ItemStack(Material.AIR)));

        final int[] slot = {27};
        game.getTypes().forEach(type -> {
            if (slot[0] != 26 && slot[0] != 35 && slot[0] != 34 && slot[0] != 43) {
                this.setItem(slot[0] += 1, item.withName(ChatColor.AQUA + game.getDisplayName() + " " + type).build(), e -> {
                    this.owner.sendMessage(this.getMessage("connecting"));
                    HyriAPI.get().getQueueManager().addPlayerInQueueWithPartyCheck(this.owner.getUniqueId(), game.getName(), type);
                });
            }
        });

        this.inventory.remove(this.currentButton);

        this.fillOutline(FILL_ITEM);
        this.setHorizontalLine(18, 26, FILL_ITEM);

        this.setupCurrentButton(HEAD_ITEM.apply(UsefulHead.ARROW_DOWN).build(), itemSlot + 9, name -> " ", null);
    }
}
