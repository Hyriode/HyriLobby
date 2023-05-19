package fr.hyriode.lobby.ui.gui.profile.booster;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.HyriBoosterTransaction;
import fr.hyriode.api.booster.IHyriBooster;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.StaffRank;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.hyrame.utils.TimeUtil;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.game.LobbyGame;
import fr.hyriode.lobby.ui.gui.ConfirmGUI;
import fr.hyriode.lobby.ui.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by AstFaster
 * on 30/07/2022 at 16:10
 */
public class BoosterGameSelectorGUI extends LobbyGUI {

    private static final int[] GAMES_SLOTS = new int[] {
            20, 21, 22, 23, 24,
            29, 30, 31, 32, 33
    };

    private final String transaction;
    private final HyriBoosterTransaction booster;
    private final ItemStack boosterIcon;

    public BoosterGameSelectorGUI(Player owner, HyriLobby plugin, String transaction, HyriBoosterTransaction booster, ItemStack boosterIcon) {
        super(owner, plugin, () -> "booster-game-selector", 6 * 9);
        this.transaction = transaction;
        this.booster = booster;
        this.boosterIcon = boosterIcon;

        this.init();
    }

    @Override
    protected void init() {
        this.newUpdate(3 * 20L);
        this.applyDesign(Design.BORDER);

        this.setItem(4, this.boosterIcon);

        this.setItem(49, new ItemBuilder(Material.ARROW)
                        .withName(LobbyMessage.BACK_ITEM.asString(this.account))
                        .build(),
                event -> new BoostersGUI(this.plugin, this.owner).open());

        this.addGamesItem();
    }

    @Override
    public void update() {
        this.addGamesItem();
    }

    private void addGamesItem() {
        int slotIndex = 0;
        for (LobbyGame game : this.plugin.getGameManager().getGames()) {
            if (!game.isBoostable()) {
                continue;
            }

            if (GAMES_SLOTS.length <= slotIndex) {
                return;
            }

            final List<IHyriBooster> boosters = HyriAPI.get().getBoosterManager().getBoosters(game.getName())
                    .stream()
                    .filter(booster -> booster.getType() == IHyriBooster.Type.NORMAL)
                    .sorted(Comparator.comparingLong(IHyriBooster::getEnabledDate))
                    .collect(Collectors.toList());
            final long enabledDate = boosters.size() > 0 && !this.account.getRank().is(StaffRank.ADMINISTRATOR) ? boosters.get(boosters.size() - 1).getDisabledDate() : System.currentTimeMillis();
            final long disabledDate = enabledDate + this.booster.getDuration() * 1000;
            final List<String> lore = ListReplacer.replace(
                    LobbyMessage.BOOSTER_GAME_SELECTOR_ITEM_LORE.asList(this.owner), "%time_slot%", this.formatTimeSlot(enabledDate, disabledDate))
                    .list();
            final List<String> queue = boosters.size() > 0 ? this.formatBoosters(boosters) : Collections.singletonList(LobbyMessage.BOOSTER_GAME_SELECTOR_NO_BOOSTER.asString(this.owner));

            lore.addAll(lore.indexOf("%queue%"), queue);
            lore.remove("%queue%");

            final ItemStack itemStack = new ItemBuilder(game.getIcon())
                    .withAllItemFlags()
                    .withName(ChatColor.AQUA + game.getGameInfo().getDisplayName())
                    .withLore(lore)
                    .build();

            this.setItem(GAMES_SLOTS[slotIndex], itemStack, event -> {
                final ItemStack icon = new ItemBuilder(this.boosterIcon)
                        .appendLore(LobbyMessage.BOOSTER_GAME_SELECTOR_ICON_TIME_SLOT.asString(this.owner)
                                .replace("%time_slot%", this.formatTimeSlot(enabledDate, disabledDate)))
                        .build();

                new ConfirmGUI(this.owner, this.plugin, icon)
                        .whenConfirm(e -> {
                            this.owner.closeInventory();
                            this.owner.playSound(this.owner.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);

                            // Set booster as 'used'
                            this.account.getTransactions().get(HyriBoosterTransaction.TRANSACTIONS_TYPE, transaction)
                                    .loadContent(new HyriBoosterTransaction())
                                    .setUsed(true);
                            this.account.update();

                            if (boosters.size() > 0) {
                                this.owner.sendMessage(LobbyMessage.BOOSTER_ADDED_IN_QUEUE_MESSAGE.asString(this.account)
                                        .replace("%enabled_date%", TimeUtil.formatDate(new Date(enabledDate), "HH:mm").replace(":", "h")));
                            }

                            // Enable booster
                            HyriAPI.get().getBoosterManager().enableBooster(this.owner.getUniqueId(), game.getName(), this.booster.getMultiplier(), this.booster.getDuration());
                        })
                        .whenCancel(e -> {
                            this.init();
                            this.open();

                            this.owner.playSound(this.owner.getLocation(), Sound.FIZZ, 0.5F, 1.0F);
                        }).open();
            });

            slotIndex++;
        }
    }

    private List<String> formatBoosters(List<IHyriBooster> boosters) {
        final List<String> formattedBoosters = new ArrayList<>();

        for (IHyriBooster booster : boosters) {
            final StringBuilder formattedBooster = new StringBuilder();

            formattedBooster.append(ChatColor.GRAY).append(Symbols.DOT_BOLD).append(" ")
                    .append(IHyriPlayer.get(booster.getOwner()).getNameWithRank())
                    .append(ChatColor.GRAY).append(" - ")
                    .append(ChatColor.AQUA).append(this.formatTimeSlot(booster.getEnabledDate(), booster.getDisabledDate()));

            if (booster.isEnabled()) {
                formattedBooster.append(" ").append(LobbyMessage.BOOSTER_GAME_SELECTOR_CURRENT.asString(this.account));
            }

            formattedBoosters.add(formattedBooster.toString());
        }
        return formattedBoosters;
    }

    private String formatTimeSlot(long enabledDate, long disabledDate) {
        final Function<Long, String> formatter = time -> TimeUtil.formatDate(new Date(time), "HH:mm").replace(":", "h");

        return LobbyMessage.BOOSTER_GAME_SELECTOR_TIME_SLOT.asString(this.account)
                .replace("%start%", formatter.apply(enabledDate))
                .replace("%end%", formatter.apply(disabledDate));
    }

}
