package fr.hyriode.lobby.ui.gui.selector.game;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyrame.inventory.pagination.PaginatedItem;
import fr.hyriode.hyrame.inventory.pagination.PaginationArea;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Pagination;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.ui.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.ServerStateWrapper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class ServerSelectorGUI extends LobbyGUI {

    private final String game;
    private final String gameType;

    public ServerSelectorGUI(HyriLobby plugin, Player owner, String game, String gameType) {
        super(owner, plugin, () -> "server-selector", 54);
        this.game = game;
        this.gameType = gameType;
        this.paginationManager.setArea(new PaginationArea(9, 44));

        this.init();
    }

    @Override
    protected void init() {
        this.applyDesign(TWO_LINES_BORDER);

        this.addPagesItems(45, 53);
        this.setupServers();
    }

    private void setupServers() {
        final Pagination<PaginatedItem> pagination = this.paginationManager.getPagination();

        pagination.clear();

        final List<HyggServer> servers = new ArrayList<>(HyriAPI.get().getServerManager().getServers(this.game))
                .stream()
                .filter(server -> Objects.equals(server.getGameType(), this.gameType))
                .filter(server -> server.getAccessibility() == HyggServer.Accessibility.PUBLIC)
                .filter(server -> server.getState() == HyggServer.State.PLAYING || server.getState() == HyggServer.State.READY)
                .collect(Collectors.toList());

        for (HyggServer server : servers) {
            final ServerStateWrapper state = ServerStateWrapper.from(server.getState());
            final List<String> lore = ListReplacer.replace(LobbyMessage.SELECTOR_SERVER_ITEM_DESCRIPTION.asList(this.account), "%state%", state.getDisplayName().getValue(this.account))
                    .replace("%players%", String.valueOf(server.getPlayingPlayers().size()))
                    .replace("%slots%", String.valueOf(server.getSlots()))
                    .list();
            final ItemStack itemStack = new ItemBuilder(state.getItem())
                    .withName(ChatColor.AQUA + server.getName())
                    .withLore(lore)
                    .build();

            pagination.add(PaginatedItem.from(itemStack, event -> HyriAPI.get().getServerManager().sendPlayerToServer(this.owner.getUniqueId(), server.getName())));
        }

        this.paginationManager.updateGUI();
    }

    @Override
    public void update() {
        this.setupServers();
    }

}
