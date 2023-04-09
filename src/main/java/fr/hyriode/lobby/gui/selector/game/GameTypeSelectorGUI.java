package fr.hyriode.lobby.gui.selector.game;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.game.IHyriGameType;
import fr.hyriode.api.network.counter.IHyriGlobalCounter;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.game.LobbyGame;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class GameTypeSelectorGUI extends LobbyGUI {

    private final LobbyGame game;
    private final boolean goBack;

    public GameTypeSelectorGUI(HyriLobby plugin, Player owner, LobbyGame game, boolean goBack) {
        super(owner, plugin, game.getGameInfo().getDisplayName(), 6 * 9);
        this.game = game;
        this.goBack = goBack;

        this.init();
        this.newUpdate(20L);
    }

    @Override
    public void update() {
        this.addTypesItems();
    }

    @Override
    protected void init() {
        final IHyriGameInfo gameInfo = this.game.getGameInfo();

        this.inventory.clear();

        this.applyDesign(PURIFIED_BORDER);

        this.setItem(49, new ItemBuilder(Material.ARROW)
                .withName(LobbyMessage.BACK_ITEM.asString(this.owner))
                .build(),
                e -> {
                    if (this.goBack) {
                        new GameSelectorGUI(this.plugin, this.owner).open();
                    } else {
                        this.owner.closeInventory();
                    }
        });

        this.setItem(4, new ItemBuilder(this.game.getIcon())
                .withName(ChatColor.DARK_AQUA + gameInfo.getDisplayName())
                .withLore(this.getLore())
                .build());
        this.addTypesItems();
    }

    private void addTypesItems() {
        final IHyriGameInfo gameInfo = this.game.getGameInfo();
        final List<IHyriGameType> types = gameInfo.getTypes().stream().sorted(Comparator.comparingInt(IHyriGameType::getId)).collect(Collectors.toList());
        final IHyriGlobalCounter playerCount = HyriAPI.get().getNetworkManager().getNetwork().getPlayerCounter();

        for (IHyriGameType type : types) {
            final int players = playerCount.getCategory(gameInfo.getName()).getPlayers(type.getName());

            for (int i : SlotConfiguration.getSlots(types.size())) {
                if(this.inventory.getItem(i) == null) {
                    this.setItem(i, new ItemBuilder(this.game.getIcon())
                            .withName(ChatColor.AQUA + type.getDisplayName())
                            .withLore(LobbyMessage.LOBBY_PLAYERS_LINE.asString(this.owner) + ChatColor.AQUA + players, "", LobbyMessage.PLAY.asLang().getValue(this.owner))
                            .build(), event -> this.sendPlayerToGame(this.owner, type.getName()));
                    break;
                }
            }
        }
    }

    private List<String> getLore() {
        final List<String> lore = new ArrayList<>();

        lore.add(this.game.formatType(this.owner));
        lore.add("");
        lore.addAll(game.getDescription(this.owner));
        lore.add("");
        lore.add(LobbyMessage.LOBBY_PLAYERS_LINE.asLang().getValue(this.owner) + ChatColor.AQUA + this.getPlayersCount());

        return lore;
    }

    private int getPlayersCount() {
        return HyriAPI.get().getNetworkManager().getNetwork().getPlayerCounter().getCategory(this.game.getName()).getPlayers();
    }

    private void sendPlayerToGame(Player player, String type) {
        final IHyriPlayerSession session = IHyriPlayerSession.get(player.getUniqueId());

        if (session.isModerating()) {
            player.sendMessage(LobbyMessage.STAFF_ERROR.asString(player));
        } else {
            HyriAPI.get().getQueueManager().addPlayerInQueue(player.getUniqueId(), this.game.getName(), type, null);

            this.owner.closeInventory();
        }
    }

    public enum SlotConfiguration {

        ONE(1, Collections.singletonList(31)),
        TWO(2, Arrays.asList(30, 32)),
        THREE(3, Arrays.asList(30, 31, 32)),
        FOUR(4, Arrays.asList(29, 30, 32, 33)),
        FIVE(5, Arrays.asList(29, 30, 31, 32, 33)),
        SIX(6, Arrays.asList(28, 29, 30, 32, 33, 34));

        private final int totalSlots;
        private final List<Integer> slots;

        SlotConfiguration(int totalSlots, List<Integer> slots) {
            this.totalSlots = totalSlots;
            this.slots = slots;
        }

        public int getTotalSlots() {
            return this.totalSlots;
        }

        public List<Integer> getSlots() {
            return this.slots;
        }

        public static List<Integer> getSlots(int totalSlots) {
            for (SlotConfiguration configuration : values()) {
                if (configuration.getTotalSlots() == totalSlots) {
                    return configuration.getSlots();
                }
            }
            return new ArrayList<>();
        }

    }

}
