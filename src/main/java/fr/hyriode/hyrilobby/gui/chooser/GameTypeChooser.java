package fr.hyriode.hyrilobby.gui.chooser;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.HyriGameType;
import fr.hyriode.api.network.HyriNetworkCount;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.game.LobbyGame;
import fr.hyriode.hyrilobby.gui.LobbyInventory;
import fr.hyriode.hyrilobby.language.LobbyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class GameTypeChooser extends LobbyInventory {

    private final LobbyGame game;
    private final ItemBuilder builder;

    public GameTypeChooser(HyriLobby plugin, Player owner, LobbyGame game) {
        super(owner, plugin, "game_type_selector", "game-type-selector", game.getGame().getTypes().size() > 5 ? 54 : 45);

        this.game = game;
        this.builder = new ItemBuilder(game.getIcon());

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.setItem(0, new ItemBuilder(Material.COMPASS).withName(LobbyMessage.BACK_ITEM.get().getForPlayer(this.owner)).build(), e -> new GamesChooserGui(this.plugin, this.owner).open());
        this.setItem(4, this.builder.clone()
                .withName(ChatColor.DARK_AQUA + this.game.getGame().getDisplayName())
                .withLore(this.getLore())
                .build());

        final Stream<Map.Entry<String, HyriGameType>> entries = this.game.getGame().getTypes().entrySet().stream().sorted(Comparator.comparingInt(o -> o.getValue().getId()));

        final HyriNetworkCount playerCount = HyriAPI.get().getNetworkManager().getNetwork().getPlayerCount();

        entries.forEachOrdered(entry -> {
            final String gameTypeName = entry.getKey();
            final int players = playerCount.getCategory(this.game.getGame().getName()).getType(gameTypeName);
            final HyriGameType gameType = entry.getValue();

            for (int i : this.getSlotsBySize(this.game.getGame().getTypes().size())) {
                if(this.inventory.getItem(i) == null) {
                    this.setItem(i, this.builder.clone()
                            .withName(ChatColor.DARK_AQUA + entry.getValue().getDisplayName())
                            .withLore(LobbyMessage.LOBBY_PLAYERS_LINE.get().getForPlayer(this.owner) + ChatColor.AQUA + players, "", LobbyMessage.CONNECT_LINE.get().getForPlayer(this.owner))
                            .build(), event -> this.sendPlayerToGame(this.owner, this.game, gameTypeName, gameType.getDisplayName()));
                    break;
                }
            }

        });
    }

    private List<String> getLore() {
        final List<String> lore = new ArrayList<>();

        lore.add(game.getGameTypeLine(this.owner));
        lore.add("");
        lore.addAll(game.getDescription(this.owner));
        lore.add("");
        lore.add(LobbyMessage.LOBBY_PLAYERS_LINE.get().getForPlayer(this.owner) + ChatColor.AQUA + this.getPlayersCount());

        return lore;
    }

    private int getPlayersCount() {
        return HyriAPI.get().getNetworkManager().getNetwork().getPlayerCount().getCategory(this.game.getGame().getName()).getPlayers();
    }

    private int[] getSlotsBySize(int i) {
        if(i == 1) {
            return new int[]{22};

        } else if(i == 2) {
            return new int[]{21, 23};

        } else if(i == 3) {
            return new int[]{21, 22, 23};

        } else if(i == 4) {
            return new int[]{21, 22, 23, 31};

        } else if(i == 5) {
            return new int[]{20, 21, 22, 23, 24};

        } else return new int[0];
    }

    private void sendPlayerToGame(Player player, LobbyGame game, String type, String display) {
        final IHyriPlayer hyriPlayer = HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId());

        if (hyriPlayer.isInModerationMode() || hyriPlayer.isInVanishMode()) {
            player.sendMessage(LobbyMessage.STAFF_ERROR.get().getForPlayer(player));
        } else if((hyriPlayer.hasParty()) && (!HyriAPI.get().getPartyManager().getParty(hyriPlayer.getParty()).isLeader(hyriPlayer.getUniqueId()))) {
            player.sendMessage(LobbyMessage.IN_PARTY_ERROR.get().getForPlayer(player));
        } else {
            player.sendMessage(LobbyMessage.JOIN_QUEUE_MESSAGE.get()
                    .getForPlayer(player)
                    .replace("%game%", game.getGame().getDisplayName() + " " + display)
            );
            HyriAPI.get().getQueueManager().addPlayerInQueueWithPartyCheck(player.getUniqueId(), game.getGame().getName(), type);
        }
    }

}
