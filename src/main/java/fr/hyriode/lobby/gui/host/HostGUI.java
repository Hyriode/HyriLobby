package fr.hyriode.lobby.gui.host;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.game.IHyriGameType;
import fr.hyriode.api.host.HostData;
import fr.hyriode.api.host.HostType;
import fr.hyriode.api.host.IHostManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyrame.inventory.pagination.PaginatedItem;
import fr.hyriode.hyrame.inventory.pagination.PaginationArea;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Pagination;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.gui.selector.game.GameSelectorGUI;
import fr.hyriode.lobby.host.HostHandler;
import fr.hyriode.lobby.host.HostState;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

/**
 * Created by AstFaster
 * on 29/07/2022 at 23:46
 */
public class HostGUI extends LobbyGUI {

    private boolean spectating;

    private final boolean cancelOnBack;

    public HostGUI(Player owner, HyriLobby plugin, boolean cancelOnBack) {
        super(owner, plugin, () -> "host", 6 * 9);
        this.cancelOnBack = cancelOnBack;
        this.paginationManager.setArea(new PaginationArea(20, 33));

        this.setItem(4, ItemBuilder.asHead(UsefulHead.CHEST)
                .withName(LobbyMessage.SELECTOR_HOST_ITEM_NAME.asString(this.account))
                .withLore(LobbyMessage.SELECTOR_HOST_ITEM_DESCRIPTION.asList(this.account))
                .build());

        this.init();

        this.setSpectatingItem();

        this.setItem(51, ItemBuilder.asHead(UsefulHead.MONITOR_PLUS)
                .withName(LobbyMessage.HOST_CREATE_ITEM_NAME.asString(this.account))
                .withLore(ListReplacer.replace(LobbyMessage.HOST_CREATE_ITEM_LORE.asList(this.account), "%available_hosts%", this.account.getHyriPlus().has() ? LobbyMessage.HOST_UNLIMITED_WORD.asString(this.account) : String.valueOf(this.account.getHosts().getAvailableHosts())).list())
                .build(),
                event -> {
                    if (this.session.isModerating()) {
                        this.owner.sendMessage(LobbyMessage.STAFF_ERROR.asString(this.account));
                        return;
                    }

                    if (this.account.getHosts().getAvailableHosts() < 1) {
                        this.owner.sendMessage(LobbyMessage.HOST_CREATE_ERROR.asString(this.account));
                        return;
                    } else if (this.plugin.getHostHandler().isWaiting(this.owner.getUniqueId())) {
                        return;
                    }

                    if (HyriAPI.get().getHostManager().getHosts().size() >= HostHandler.MAX_HOSTS) {
                        this.owner.sendMessage(LobbyMessage.HOST_LIMIT_EXCEEDED_MESSAGE.asString(this.account));
                        return;
                    }

                    new HostGameSelectorGUI(this.owner, this.plugin).open();
                });

        this.setupItems();
    }

    private void setSpectatingItem() {
        final List<String> lore = ListReplacer.replace(LobbyMessage.HOST_SPECTATING_ITEM_LORE.asList(this.account), "%enabled_color%", String.valueOf(this.spectating ? ChatColor.AQUA : ChatColor.GRAY))
                .replace("%disabled_color%", String.valueOf(this.spectating ? ChatColor.GRAY : ChatColor.AQUA))
                .list();

        this.setItem(47, new ItemBuilder(this.spectating ? Material.EYE_OF_ENDER : Material.ENDER_PEARL)
                .withName(LobbyMessage.HOST_SPECTATING_ITEM_NAME.asString(this.account))
                .withLore(lore)
                .build(),
                event -> {
                    this.owner.playSound(this.owner.getLocation(), Sound.CLICK, 0.5F, 2.0F);
                    this.spectating = !this.spectating;

                    this.setSpectatingItem();
                    this.setupItems();
                });
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        this.setItem(49, new ItemBuilder(Material.ARROW)
                .withName(LobbyMessage.BACK_ITEM.asString(this.account))
                .build(),
                event -> {
                    if (this.cancelOnBack) {
                        this.owner.closeInventory();
                        return;
                    }

                    new GameSelectorGUI(this.plugin, this.owner).open();
                });

        this.addPagesItems(27, 35);
    }

    private void setupItems() {
        final Pagination<PaginatedItem> pagination = this.paginationManager.getPagination();
        final IHostManager hostAPI = HyriAPI.get().getHostManager();

        pagination.clear();

        for (HyggServer server : hostAPI.getHosts()) {
            final HyggServer.State state = server.getState();

            if ((state == HyggServer.State.PLAYING && !this.spectating) || (state != HyggServer.State.PLAYING && this.spectating)) {
                continue;
            }

            final HostData hostData = hostAPI.getHostData(server);

            if (Objects.requireNonNull(hostData).getType() == HostType.PRIVATE && !hostData.getWhitelistedPlayers().contains(this.owner.getUniqueId())) {
                continue;
            }

            if (this.spectating && !hostData.isSpectatorsAllowed()) {
                continue;
            }

            final ItemStack itemStack = this.createItem(server, hostData);

            if (itemStack == null) {
                continue;
            }

            pagination.add(PaginatedItem.from(itemStack, event -> {
                if (this.spectating || this.session.isModerating()) {
                    HyriAPI.get().getServerManager().sendPlayerToServer(this.owner.getUniqueId(), server.getName());
                } else {
                    this.sendToHost(server.getName());
                }
            }));
        }

        this.paginationManager.updateGUI();
    }

    private void sendToHost(String serverName) {
        final IHyriPlayerSession session = IHyriPlayerSession.get(this.owner.getUniqueId());

        if (session.isModerating()) {
            this.owner.sendMessage(LobbyMessage.STAFF_ERROR.asString(this.account));
        } else {
            HyriAPI.get().getQueueManager().addPlayerInQueue(this.owner.getUniqueId(), serverName);

            this.owner.closeInventory();
        }
    }

    @Override
    public void update() {
        this.setupItems();
    }

    private ItemStack createItem(HyggServer server, HostData hostData) {
        final HostState state = HostState.getFromServer(server);

        if (state == null) {
            return null;
        }

        final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(server.getType());

        if (gameInfo == null) {
            return null;
        }

        final IHyriGameType gameType = gameInfo.getType(server.getGameType());

        if (gameType == null) {
            return null;
        }

        final int players = server.getPlayingPlayers().size();
        final List<String> lore = ListReplacer.replace(LobbyMessage.HOST_ITEM_LORE.asList(this.account), "%owner%", IHyriPlayer.get(hostData.getOwner()).getNameWithRank())
                .replace("%game%", gameInfo.getDisplayName())
                .replace("%game_type%", gameType.getDisplayName())
                .replace("%state%", state.getDisplay().getValue(this.account))
                .replace("%players%", String.valueOf(players))
                .replace("%slots%", String.valueOf(server.getSlots()))
                .list();

        lore.add("");
        lore.add(this.spectating ? LobbyMessage.CLICK_TO_SPECTATE.asString(this.account) : LobbyMessage.CLICK_TO_JOIN.asString(this.account));

        return new ItemBuilder(Material.SKULL_ITEM, Math.min(players, 64), 3)
                .withHeadTexture(state.getHead())
                .withName(ChatColor.AQUA + hostData.getName())
                .withLore(lore)
                .build();
    }

}
