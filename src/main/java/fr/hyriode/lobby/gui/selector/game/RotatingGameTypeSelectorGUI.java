package fr.hyriode.lobby.gui.selector.game;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.game.IHyriGameType;
import fr.hyriode.api.network.counter.IHyriGlobalCounter;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayerSession;
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
        this.applyDesign(PURIFIED_BORDER);

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

    private void addTypesItems() {
        final List<IHyriGameType> types = this.game.getTypes().stream().sorted(Comparator.comparingInt(IHyriGameType::getId)).collect(Collectors.toList());
        final IHyriGlobalCounter playerCount = HyriAPI.get().getNetworkManager().getNetwork().getPlayerCounter();

        for (IHyriGameType type : types) {
            final int players = playerCount.getCategory(this.game.getName()).getPlayers(type.getName());

            for (int i : SlotConfiguration.getSlots(types.size())) {
                if(this.inventory.getItem(i) == null) {
                    this.setItem(i, ItemBuilder.asHead(UsefulHead.DICE_1)
                            .withName(ChatColor.AQUA + type.getDisplayName())
                            .withLore(LobbyMessage.LOBBY_PLAYERS_LINE.asString(this.owner) + ChatColor.AQUA + players, "", LobbyMessage.PLAY.asLang().getValue(this.owner))
                            .build(), event -> this.sendPlayerToGame(this.owner, type.getName()));
                    break;
                }
            }
        }
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
