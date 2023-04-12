package fr.hyriode.lobby.gui.profile;

import fr.hyriode.api.leveling.NetworkLeveling;
import fr.hyriode.hyrame.inventory.pagination.PaginatedItem;
import fr.hyriode.hyrame.inventory.pagination.PaginationArea;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Pagination;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.leveling.LevelingReward;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by AstFaster
 * on 24/10/2022 at 12:24
 */
public class LevelingRewardsGUI extends LobbyGUI {

    private final ItemStack levelingItem;

    public LevelingRewardsGUI(Player owner, HyriLobby plugin, ItemStack levelingItem) {
        super(owner, plugin, () -> "leveling", 6 * 9);
        this.levelingItem = levelingItem;

        this.paginationManager.setArea(new PaginationArea(20, 33));

        this.init();
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        this.setItem(4, this.levelingItem);

        this.addPagesItems(27, 35);
        this.setupItems();
    }

    private void setupItems() {
        final Pagination<PaginatedItem> pagination = this.paginationManager.getPagination();

        pagination.clear();

        for (LevelingReward reward : LevelingReward.VALUES) {
            final NetworkLeveling leveling = this.account.getNetworkLeveling();
            final int level = reward.getLevel();
            final boolean claimed = leveling.getClaimedRewards().contains(level);
            final boolean notLevel = leveling.getLevel() < level;

            pagination.add(PaginatedItem.from(this.createItem(reward, claimed, notLevel), event -> {
                if (reward.getHandler() == null) {
                    return;
                }

                if (claimed) {
                    this.owner.sendMessage(LobbyMessage.LEVELING_REWARD_ALREADY_CLAIMED.asString(this.account));
                } else {
                    if (notLevel) {
                        this.owner.sendMessage(LobbyMessage.LEVELING_REWARD_NOT_LEVEL.asString(this.account));
                        return;
                    }

                    reward.getHandler().claim(this.account);

                    leveling.claimReward(level);

                    this.account.update();
                    this.owner.sendMessage(LobbyMessage.LEVELING_REWARD_CLAIMED.asString(this.account).replace("%level%", String.valueOf(level)));

                    this.setupItems();
                }
            }));
        }

        this.paginationManager.updateGUI();
    }

    private ItemStack createItem(LevelingReward reward, boolean claimed, boolean notLevel) {
        final List<String> lore = new ArrayList<>();
        final LevelingReward.Handler<?> handler = reward.getHandler();

        lore.add("");
        lore.addAll(handler == null ? Collections.singletonList(ChatColor.DARK_GRAY + "Soon...") : Arrays.asList(handler.getDescription(this.account).split("\n")));
        lore.add("");
        lore.add((claimed ? LobbyMessage.LEVELING_REWARD_ALREADY_CLAIMED_LINE : (notLevel ? LobbyMessage.LEVELING_REWARD_NOT_LEVEL_LINE : LobbyMessage.CLICK_TO_CLAIM)).asString(this.account));

        return new ItemBuilder(claimed ? Material.HOPPER_MINECART : Material.STORAGE_MINECART)
                .withName(LobbyMessage.LEVELING_REWARD_ITEM.asString(this.account).replace("%level%", String.valueOf(reward.getLevel())))
                .withLore(lore)
                .build();
    }

}
