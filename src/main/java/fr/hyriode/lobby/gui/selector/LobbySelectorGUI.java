package fr.hyriode.lobby.gui.selector;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyggdrasil.api.server.HyggServerState;
import fr.hyriode.hylios.api.lobby.LobbyAPI;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.item.ItemNBT;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
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

public class LobbySelectorGUI extends LobbyGUI {

    private static final String NBT = "LobbyItem";

    private final IHyriServerManager serverManager;

    public LobbySelectorGUI(HyriLobby plugin, Player owner) {
        super(owner, plugin, () -> "lobby-selector", 54);
        this.serverManager = HyriAPI.get().getServerManager();

        this.newUpdate(3 * 20L);
        this.init();
    }

    @Override
    public void update() {
        for (ItemStack itemStack : this.inventory.getContents()) {
            if (itemStack == null) {
                continue;
            }

            final ItemBuilder builder = new ItemBuilder(itemStack);
            final ItemNBT nbt = builder.nbt();

            if (nbt.hasTag(NBT)) {
                final String serverName = nbt.getString(NBT);
                final HyggServer server = HyriAPI.get().getServerManager().getServer(serverName);

                if (server == null) {
                    this.init();
                    break;
                }

                builder.withLore(this.getLobbyLore(this.getFriendsServers().get(serverName).size(), server)).build();
            }
        }
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.border();

        final List<HyggServer> servers = this.serverManager.getLobbies();
        final HashMap<String, ItemStack> items = new HashMap<>();

        for (HyggServer server : servers) {
            if (server.getState() != HyggServerState.READY) {
                continue;
            }

            final String serverName = server.getName();
            final List<IHyriPlayer> friends = this.getFriendsServers().get(serverName);

            items.put(server.getName(), this.getLobbyItem(friends == null ? 0 : friends.size(), server));
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

    private Map<String, List<IHyriPlayer>> getFriendsServers() {
        final List<IHyriFriend> totalFriends = HyriAPI.get().getFriendManager().getFriends(this.account.getUniqueId());
        final Map<String, List<IHyriPlayer>> friendsServers = new HashMap<>();

        for (IHyriFriend friend : totalFriends) {
            final IHyriPlayer friendAccount = HyriAPI.get().getPlayerManager().getCachedPlayer(friend.getUniqueId());

            if (friendAccount != null && friendAccount.isOnline()) {
                final String server = friendAccount.getCurrentServer();
                final List<IHyriPlayer> players = friendsServers.getOrDefault(server, new ArrayList<>());

                players.add(friendAccount);

                friendsServers.put(server, players);
            }
        }
        return friendsServers;
    }

    private void handleClick(InventoryClickEvent event, String serverToSend) {
        final Player player = (Player) event.getWhoClicked();

        if (this.account.getCurrentServer().equals(serverToSend)) {
            return;
        }

        HyriAPI.get().getServerManager().sendPlayerToServer(player.getUniqueId(), serverToSend);
    }

    private ItemStack getLobbyItem(int friends, HyggServer server) {
        final String id = server.getName().split("-")[1];
        final String name = LobbyMessage.SELECTOR_LOBBY_ID.asString(this.account) + ChatColor.AQUA + id;
        final ItemBuilder item = ItemBuilder.asHead()
                .withName(name)
                .withLore(this.getLobbyLore(friends, server))
                .nbt()
                .setString(NBT, server.getName())
                .toBuilder();

        if (server.getPlayingPlayers().size() >= server.getSlots()) {
            item.withHeadTexture(UsefulHead.RED_CUBE);
        } else if (friends > 0) {
            item.withHeadTexture(UsefulHead.CYAN_CUBE);
        } else {
            item.withHeadTexture(UsefulHead.GREEN_CUBE);
        }

        return item.build();
    }


    private List<String> getLobbyLore(int friends, HyggServer server) {
        final List<String> list = new ArrayList<>();
        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(this.owner.getUniqueId());
        final String currentServer = account.getCurrentServer();

        list.add(LobbyMessage.LOBBY_PLAYERS_LINE.asString(this.owner) + ChatColor.AQUA + server.getPlayingPlayers().size() + "/" + LobbyAPI.MAX_PLAYERS);

        if (friends > 0) {
            list.add(LobbyMessage.FRIENDS.asString(this.owner) + ChatColor.AQUA + friends);
        }

        list.add(" ");

        if (currentServer != null && !currentServer.equals(server.getName())) {
            list.add(LobbyMessage.LOBBY_CONNECT.asString(account));
        } else {
            list.add(LobbyMessage.CONNECTED_LINE.asString(account));
        }
        return list;
    }

}
