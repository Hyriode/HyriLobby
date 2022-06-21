package fr.hyriode.lobby.gui.selector;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.hyggdrasil.api.lobby.HyggLobbyAPI;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyInventory;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.InventoryUtil;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbySelectorGui extends LobbyInventory {

    private final IHyriServerManager serverManager;

    public LobbySelectorGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "lobby_selector", "lobby-selector", 54);

        this.serverManager = HyriAPI.get().getServerManager();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fill();

        final List<HyggServer> servers = this.serverManager.getLobbies();
        final HashMap<String, ItemStack> items = new HashMap<>();

        for (HyggServer server : servers) {
            items.put(server.getName(), this.getLobbyItem(server));
        }

        final List<Map.Entry<String, ItemStack>> entry = new ArrayList<>(items.entrySet());

        int index = 0;
        for (int i : InventoryUtil.getAvailableSlots()) {
            if (index >= items.size()) {
                break;
            }

            if (this.inventory.getItem(i) == null) {
                final Map.Entry<String, ItemStack> itemEntry = entry.get(index);
                this.setItem(i, itemEntry.getValue(), event -> this.handleClick(event, itemEntry.getKey()));
            }
            index++;
        }
    }

    private void handleClick(InventoryClickEvent event, String serverToSend) {
        final Player player = (Player) event.getWhoClicked();

        HyriAPI.get().getServerManager().sendPlayerToServer(player.getUniqueId(), serverToSend);
    }

    private ItemStack getLobbyItem(HyggServer server) {
        final String id = server.getName().split("-")[1];
        final String name = LobbyMessage.SELECTOR_LOBBY_ID.asLang().getForPlayer(this.account) + ChatColor.AQUA + id;

        ItemBuilder item = ItemBuilder.asHead()
                .withName(name)
                .withLore(this.getLobbyLore(server));

        if (server.getSlots() == server.getPlayers().size()) {
            item.withHeadTexture(UsefulHead.RED_CUBE.getTexture());
        } else {
            item.withHeadTexture(UsefulHead.GREEN_CUBE.getTexture());
        }

        return item.build();
    }


    private List<String> getLobbyLore(HyggServer server) {
        final List<String> list = new ArrayList<>();
        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(this.owner.getUniqueId());

        list.add(LobbyMessage.LOBBY_PLAYERS_LINE.asLang().getForPlayer(this.owner) + ChatColor.AQUA + server.getPlayers().size() + "/" + HyggLobbyAPI.MAX_PLAYERS);

        if (account.getCurrentServer() != null && !account.getCurrentServer().equalsIgnoreCase(server.getName())) {
            list.add(" ");
            list.add(LobbyMessage.LOBBY_CONNECT.asLang().getForPlayer(account));
        } else {
            list.add(" ");
            list.add(LobbyMessage.CONNECTED_LINE.asLang().getForPlayer(account));
        }

        return list;
    }
}
