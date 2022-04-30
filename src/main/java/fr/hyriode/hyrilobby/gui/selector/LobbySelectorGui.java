package fr.hyriode.hyrilobby.gui.selector;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.hyggdrasil.api.lobby.HyggLobbyAPI;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.gui.LobbyInventory;
import fr.hyriode.hyrilobby.language.LobbyMessage;
import fr.hyriode.hyrilobby.util.InventoryUtil;
import fr.hyriode.hyrilobby.util.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        final String name = LobbyMessage.SELECTOR_GUI_LOBBY_NAME.getGuiItem(this.guiName).getForPlayer(this.account) + id;

        ItemBuilder item = ItemBuilder.asHead()
                .withName(name)
                .withLore(this.getLobbyLore(server, this.getFriendsOnServer(server)));

        if (server.getSlots() == server.getPlayers().size()) {
            item.withHeadTexture(UsefulHead.RED_CUBE.getTexture());
        } else if (this.getFriendsOnServer(server) > 0) {
            item.withHeadTexture(UsefulHead.CYAN_CUBE.getTexture());
        } else {
            item.withHeadTexture(UsefulHead.GREEN_CUBE.getTexture());
        }

        return item.build();
    }

    private int getFriendsOnServer(HyggServer server) {
        final List<IHyriFriend> friends = HyriAPI.get().getFriendManager().getFriends(this.owner.getUniqueId());
        final List<IHyriPlayer> accounts = friends.stream().map(friend -> HyriAPI.get().getPlayerManager().getPlayer(friend.getUniqueId())).collect(Collectors.toList());

        return (int) accounts.stream().filter(account -> server.getName().equals(account.getCurrentServer())).count();
    }

    private List<String> getLobbyLore(HyggServer server, int friendsCount) {
        final List<String> list = new ArrayList<>();
        final IHyriPlayer hyriPlayer = HyriAPI.get().getPlayerManager().getPlayer(this.owner.getUniqueId());

        list.add(LobbyMessage.LOBBY_PLAYERS_LINE.get().getForPlayer(this.owner) + ChatColor.AQUA + server.getPlayers().size() + "/" + HyggLobbyAPI.MAX_PLAYERS);

        if (friendsCount > 0) {
            list.add(LobbyMessage.SELECTOR_GUI_LOBBY_FRIENDS_LINE.get().getForPlayer(this.owner) + ChatColor.AQUA + friendsCount);
        }

        if (!hyriPlayer.getCurrentServer().equalsIgnoreCase(server.getName())) {
            list.add(" ");
            list.add(LobbyMessage.LOBBY_CONNECT.get().getForPlayer(hyriPlayer));
        }

        return list;
    }
}
