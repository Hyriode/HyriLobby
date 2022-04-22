package fr.hyriode.lobby.gui;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.server.IHyriServerManager;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHead;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LobbySelectorGui extends LobbyInventory {

    private final IHyriServerManager serverManager;

    public LobbySelectorGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "lobby_selector", 54);

        this.serverManager = HyriAPI.get().getServerManager();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fillOutline(FILL_ITEM);

        final List<HyggServer> servers = this.serverManager.getLobbies();
        final Map<ItemStack, Consumer<InventoryClickEvent>> items = new HashMap<>();

        for (HyggServer server : servers) {
            final ItemStack item = this.getLobbyItem(server);

            items.put(item, e -> {
                if (server.getPlayers().size() == server.getSlots()) {
                    this.owner.sendMessage(this.getMessage("full"));
                    return;
                }

                if (this.account.getCurrentServer().equals(server.getName())) {
                    this.owner.sendMessage(this.getMessage("already_connected"));
                    return;
                }

                this.owner.sendMessage(this.getMessage("connecting") + server.getName().split("-")[1] + "...");
                this.serverManager.sendPlayerToServer(this.owner.getUniqueId(), server.getName());
            });
        }

        this.setupPagination(servers.size(), e -> this.init());
        this.tryToFill(10, this.getIndexFromPage(), items);
    }

    private ItemStack getLobbyItem(HyggServer server) {
        final int friends = this.getFriendsOnServer(server);
        final UsefulHead texture = server.getPlayers().size() == server.getSlots() ? UsefulHead.RED_CUBE : (friends > 0 ? UsefulHead.CYAN_CUBE : UsefulHead.GREEN_CUBE);

        return HEAD_ITEM.apply(texture).withName(ChatColor.WHITE + "Lobby ID: " + ChatColor.AQUA + server.getName().split("-")[1]).withLore(this.getServerLore(server, friends)).build();
    }

    private int getFriendsOnServer(HyggServer server) {
        final List<IHyriFriend> friends = HyriAPI.get().getFriendManager().getFriends(this.owner.getUniqueId());
        final List<IHyriPlayer> accounts = friends.stream().map(friend -> this.playerManager.getPlayer(friend.getUniqueId())).collect(Collectors.toList());

        return (int) accounts.stream().filter(account -> server.getName().equals(account.getCurrentServer())).count();
    }

    private List<String> getServerLore(HyggServer server, int friends) {
        final boolean isFull = server.getPlayers().size() == server.getSlots();
        final List<String> lore = new ArrayList<>();

        lore.add(this.getMessage("connected") + (isFull ? ChatColor.RED : ChatColor.AQUA) + server.getPlayers() + "/50");

        if (friends > 0) {
            lore.add(this.getMessage("friends") + ChatColor.AQUA + friends);
        }

        lore.add("");
        lore.add(this.getMessage(isFull ? "full" : "click"));

        return lore;
    }
}
