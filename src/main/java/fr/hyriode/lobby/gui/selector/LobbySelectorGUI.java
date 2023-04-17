package fr.hyriode.lobby.gui.selector;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.player.model.IHyriFriend;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.api.server.ILobbyAPI;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
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

import java.util.*;

public class LobbySelectorGUI extends LobbyGUI {

    private static final String NBT = "LobbyItem";

    private final ILobbyAPI lobbyAPI;

    public LobbySelectorGUI(HyriLobby plugin, Player owner) {
        super(owner, plugin, () -> "lobby-selector", 54);
        this.lobbyAPI = HyriAPI.get().getLobbyAPI();

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

                if (server == null || server.getState() != HyggServer.State.READY) {
                    this.init();
                    break;
                }

                final List<IHyriPlayer> friends = this.getFriendsServers().get(serverName);

                builder.withLore(this.getLobbyLore(friends == null ? 0 : friends.size(), server)).build();
            }
        }
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.applyDesign(Design.BORDER);

        final Set<HyggServer> servers = this.lobbyAPI.getLobbies();
        final HashMap<String, ItemStack> items = new HashMap<>();

        for (HyggServer server : servers) {
            if (server.getState() != HyggServer.State.READY) {
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
        final List<IHyriFriend> totalFriends = this.account.getFriends().getAll();
        final Map<String, List<IHyriPlayer>> friendsServers = new HashMap<>();

        for (IHyriFriend friend : totalFriends) {
            final IHyriPlayer friendAccount = HyriAPI.get().getPlayerManager().getPlayer(friend.getUniqueId());

            final IHyriPlayerSession friendSession = IHyriPlayerSession.get(friend.getUniqueId());

            if (friendSession != null) {
                final String server = friendSession.getServer();
                final List<IHyriPlayer> players = friendsServers.getOrDefault(server, new ArrayList<>());

                players.add(friendAccount);

                friendsServers.put(server, players);
            }
        }
        return friendsServers;
    }

    private void handleClick(InventoryClickEvent event, String serverToSend) {
        final Player player = (Player) event.getWhoClicked();

        if (this.session.getServer().equals(serverToSend)) {
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
        final IHyriPlayerSession session = IHyriPlayerSession.get(this.owner.getUniqueId());
        final String currentServer = session.getServer();

        list.add(LobbyMessage.LOBBY_PLAYERS_LINE.asString(this.owner) + ChatColor.AQUA + server.getPlayingPlayers().size() + "/" + ILobbyAPI.MAX_PLAYERS);

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
