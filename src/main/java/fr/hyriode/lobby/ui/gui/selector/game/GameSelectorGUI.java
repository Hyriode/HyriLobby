package fr.hyriode.lobby.ui.gui.selector.game;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.IHyriBooster;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.game.rotating.IHyriRotatingGame;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.game.LobbyGame;
import fr.hyriode.lobby.ui.gui.LobbyGUI;
import fr.hyriode.lobby.ui.gui.host.HostGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

import static fr.hyriode.lobby.game.LobbyGame.State;

public class GameSelectorGUI extends LobbyGUI {

    private final List<Integer> gameSlots;

    private int rotatingGameAnimation;

    public GameSelectorGUI(HyriLobby plugin, Player owner) {
        super(owner, plugin, () -> "game-selector", 54);

        final List<LobbyGame> games = this.plugin.getGameManager().getGamesInSelector();

        this.gameSlots = SlotConfiguration.getSlots(games.size() + 1);

        this.init();
        this.newUpdate(20L);
    }

    @Override
    public void update() {
        this.rotatingGameAnimation++;

        if (this.rotatingGameAnimation > 2) {
            this.rotatingGameAnimation = 0;
        }

        this.addGameItems();
        this.addRotatingGameItem();
    }

    protected void init() {
        this.applyDesign(PURIFIED_BORDER);

        this.addGameItems();
        this.addOtherItems();
    }

    private void addGameItems() {
        final List<LobbyGame> games = this.plugin.getGameManager().getGamesInSelector();

        int gameIndex = 0;
        for (int slot : this.gameSlots) {
            if (gameIndex == games.size()) {
                break;
            }

            this.addGameItem(slot, games.get(gameIndex));
            gameIndex++;
        }
    }

    private void addOtherItems()  {
        final ItemStack vipItem = new ItemBuilder(Material.GOLD_INGOT)
                .withName(LobbyMessage.SELECTOR_VIP_ITEM_NAME.asString(this.account))
                .withLore(LobbyMessage.SELECTOR_VIP_ITEM_DESCRIPTION.asList(this.account))
                .build();

        final ItemStack jumpItem = new ItemBuilder(Material.FEATHER)
                .withName(LobbyMessage.SELECTOR_JUMP_ITEM_NAME.asString(this.account))
                .withLore(LobbyMessage.SELECTOR_JUMP_ITEM_DESCRIPTION.asList(this.account))
                .build();

        final ItemStack hostItem = ItemBuilder.asHead()
                .withHeadTexture(UsefulHead.CHEST)
                .withName(LobbyMessage.SELECTOR_HOST_ITEM_NAME.asString(this.account))
                .withLore(LobbyMessage.SELECTOR_HOST_ITEM_DESCRIPTION.asList(this.account))
                .appendLore("", LobbyMessage.CLICK_TO_SHOW.asString(this.account))
                .build();

        this.setItem(4, hostItem, event -> new HostGUI(this.owner, this.plugin, false).open());
        this.setItem(18, jumpItem, event -> this.owner.teleport(this.plugin.config().getJumpLocation().asBukkit()));
        this.setItem(27, vipItem, event -> this.owner.teleport(this.plugin.config().getVIPLocation().asBukkit()));

        this.addRotatingGameItem();
    }

    private void addRotatingGameItem() {
        UsefulHead head = null;
        switch (this.rotatingGameAnimation) {
            case 0:
                head = UsefulHead.DICE_1;
                break;
            case 1:
                head = UsefulHead.DICE_2;
                break;
            case 2:
                head = UsefulHead.DICE_3;
                break;
        }

        final IHyriRotatingGame game = HyriAPI.get().getGameManager().getRotatingGameManager().getRotatingGame();
        final List<IHyriBooster> boosters = HyriAPI.get().getBoosterManager().getActiveBoosters(game.getInfo().getName())
                .stream()
                .sorted((o1, o2) -> o2.getType().compareTo(o1.getType()))
                .collect(Collectors.toList());
        final List<String> lore = LobbyMessage.SELECTOR_ROTATING_GAME_ITEM_DESCRIPTION.asList(this.owner);

        if (boosters.size() > 0){
            final IHyriBooster booster = boosters.get(0);

            lore.add(lore.indexOf("%booster%"), LobbyMessage.GAME_BOOSTER_LINE.asString(this.account)
                    .replace("%owner%", IHyriPlayer.get(booster.getOwner()).getNameWithRank())
                    .replace("%boost%", String.valueOf(((int) (booster.getMultiplier() * 100 - 100)))));
        }

        lore.add(lore.indexOf("%booster%") + 1, "");
        lore.remove("%booster%");

        final ItemStack rotatingGameItem = ItemBuilder.asHead(head)
                .withName(LobbyMessage.SELECTOR_ROTATING_GAME_ITEM_NAME.asString(this.owner))
                .withLore(lore)
                .build();

        this.setItem(this.gameSlots.get(this.gameSlots.size() - 1), rotatingGameItem, event -> new RotatingGameTypeSelectorGUI(this.plugin, this.owner, true).open());
    }

    private void addGameItem(int slot, LobbyGame game) {
        final IHyriGameInfo gameInfo = game.getGameInfo();
        final State state = game.getState();
        final List<String> lore = new ArrayList<>();

        final List<IHyriBooster> boosters = HyriAPI.get().getBoosterManager().getActiveBoosters(game.getName())
                .stream()
                .sorted((o1, o2) -> o2.getType().compareTo(o1.getType()))
                .collect(Collectors.toList());

        lore.add(game.formatType(this.owner));
        lore.add("");
        lore.addAll(game.getDescription(this.owner));
        lore.add("");
        lore.add(LobbyMessage.LOBBY_PLAYERS_LINE.asString(this.account) + ChatColor.AQUA + game.getPlayers());

        if (boosters.size() > 0) {
            final IHyriBooster booster = boosters.get(0);

            lore.add(LobbyMessage.GAME_BOOSTER_LINE.asString(this.account)
                    .replace("%owner%", IHyriPlayer.get(booster.getOwner()).getNameWithRank())
                    .replace("%boost%", String.valueOf(((int) (booster.getMultiplier() * 100 - 100)))));
        }

        lore.add("");
        lore.add(LobbyMessage.PLAY.asString(this.owner));

        this.setItem(slot, new ItemBuilder(game.getIcon())
                .withName(ChatColor.AQUA + (gameInfo == null ? "Unknown" : gameInfo.getDisplayName()) + (state != State.OPENED ? " " + state.getDisplay().getValue(this.owner) : ""))
                .withLore(lore)
                .build(),
                event -> {
                    if (game.isEnabled()) {
                        new GameTypeSelectorGUI(this.plugin, this.owner, game, true).open();
                    }
                });
    }

    private enum SlotConfiguration {

        ONE(1, Collections.singletonList(22)),
        TWO(2, Arrays.asList(22, 31)),
        THREE(3, Arrays.asList(21, 22, 23)),
        FOUR(4, Arrays.asList(21, 22, 23, 31)),
        FIVE(5, Arrays.asList(21, 22, 23, 30, 32)),
        SIX(6, Arrays.asList(21, 22, 23, 30, 31, 32)),
        SEVEN(7, Arrays.asList(21, 22, 23, 30, 31, 32, 40)),
        EIGHT(8, Arrays.asList(20, 21, 22, 23, 24, 30, 31, 32)),
        NINE(9, Arrays.asList(20, 21, 22, 23, 24, 30, 31, 32, 40)),
        TEN(10, Arrays.asList(20, 21, 22, 23, 24, 29, 30, 31, 32, 33));

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
