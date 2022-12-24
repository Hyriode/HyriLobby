package fr.hyriode.lobby.gui.selector.game;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.HyriGameType;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.network.counter.IHyriGlobalCounter;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RotatingGameTypeSelectorGUI extends LobbyGUI {

    private final IHyriGameInfo game;

    private final boolean goBack;

    public RotatingGameTypeSelectorGUI(HyriLobby plugin, Player owner, boolean goBack) {
        super(owner, plugin, HyriAPI.get().getGameManager().getRotatingGameManager().getRotatingGame().getInfo().getDisplayName(), 6 * 9);
        this.goBack = goBack;
        this.game = HyriAPI.get().getGameManager().getRotatingGameManager().getRotatingGame().getInfo();

        this.setItem(4, ItemBuilder.asHead(UsefulHead.DICE_1)
                .withName(LobbyMessage.SELECTOR_ROTATING_GAME_ITEM_NAME.asString(this.owner))
                .withLore(LobbyMessage.SELECTOR_ROTATING_GAME_ITEM_DESCRIPTION.asList(this.owner))
                .build());

        this.init();
        this.newUpdate(20L);
    }

    @Override
    public void update() {
        this.addTypesItems();
    }

    @Override
    protected void init() {
        this.customBorder();

        this.setItem(49, new ItemBuilder(Material.ARROW)
                .withName(LobbyMessage.BACK_ITEM.asString(this.owner))
                .build(),
                e -> {
                    if (this.goBack) {
                        new GameSelectorGUI(this.plugin, this.owner).open();
                        return;
                    }
                    this.owner.closeInventory();
                });

        this.addTypesItems();
    }

    private void customBorder() {
        final ItemStack border = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 9)
                .withName(" ")
                .withAllItemFlags()
                .build();

        this.setHorizontalLine(0, 2, border);
        this.setHorizontalLine(6, 8, border);
        this.setHorizontalLine(45, 47, border);
        this.setHorizontalLine(51, 53, border);
    }

    private void addTypesItems() {
        final Set<Map.Entry<String, HyriGameType>> entries = this.game.getTypes().entrySet();
        final Stream<Map.Entry<String, HyriGameType>> entriesStream = entries.stream().sorted(Comparator.comparingInt(o -> o.getValue().getId()));
        final IHyriGlobalCounter playerCount = HyriAPI.get().getNetworkManager().getNetwork().getPlayerCounter();

        for (Map.Entry<String, HyriGameType> entry : entriesStream.collect(Collectors.toList())) {
            final String gameTypeName = entry.getKey();
            final int players = playerCount.getCategory(this.game.getName()).getPlayers(gameTypeName);

            for (int i : SlotConfiguration.getSlots(entries.size())) {
                if(this.inventory.getItem(i) == null) {
                    this.setItem(i, ItemBuilder.asHead(UsefulHead.DICE_1)
                            .withName(ChatColor.AQUA + entry.getValue().getDisplayName())
                            .withLore(LobbyMessage.LOBBY_PLAYERS_LINE.asString(this.owner) + ChatColor.AQUA + players, "", LobbyMessage.PLAY.asLang().getValue(this.owner))
                            .build(), event -> this.sendPlayerToGame(this.owner, gameTypeName));
                    break;
                }
            }
        }
    }

    private void sendPlayerToGame(Player player, String type) {
        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId());
        final IHyriParty party = HyriAPI.get().getPartyManager().getParty(account.getParty());

        if (account.isInModerationMode()) {
            player.sendMessage(LobbyMessage.STAFF_ERROR.asString(player));
        } else if (party != null && !party.isLeader(player.getUniqueId())) {
            player.sendMessage(LobbyMessage.IN_PARTY_ERROR.asString(player));
        } else {
            HyriAPI.get().getQueueManager().addPlayerInQueue(player.getUniqueId(), this.game.getName(), type, null);

            this.owner.closeInventory();
        }
    }

    private enum SlotConfiguration {

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
