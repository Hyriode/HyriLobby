package fr.hyriode.hyrilobby.gui.profile;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.friend.IHyriFriendHandler;
import fr.hyriode.api.friend.IHyriFriendManager;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.signgui.SignGUI;
import fr.hyriode.hyrame.utils.DurationConverter;
import fr.hyriode.hyrame.utils.ThreadUtil;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.gui.LobbyInventory;
import fr.hyriode.hyrilobby.language.LobbyMessage;
import fr.hyriode.hyrilobby.util.InventoryUtil;
import fr.hyriode.hyrilobby.util.RandomTools;
import fr.hyriode.hyrilobby.util.UsefulHead;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.*;
import java.util.function.Consumer;

public class FriendsGui extends LobbyInventory {

    private final IHyriFriendManager friendManager;
    private final IHyriFriendHandler friendHandler;

    public FriendsGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "friends", "friends", 54);

        this.friendManager = HyriAPI.get().getFriendManager();
        this.friendHandler = this.friendManager.createHandler(this.owner.getUniqueId());

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fill();

        final List<IHyriFriend> friends = this.friendManager.getFriends(this.owner.getUniqueId());
        final Map<ItemStack, Consumer<InventoryClickEvent>> items = new HashMap<>();

        for (IHyriFriend friend : friends) {
            final IHyriPlayer player = HyriAPI.get().getPlayerManager().getPlayer(friend.getUniqueId());
            final ItemStack item = PLAYER_HEAD_ITEM.apply(friend.getUniqueId()).withName(player.getNameWithRank()).withLore(this.getFriendLore(player)).build();
            items.put(item, e -> {
                if (e.getClick().isRightClick()) {
                    this.friendHandler.removeFriend(friend);
                    this.friendManager.updateFriends(this.friendHandler);
                }
            });
        }

        this.setItem(50, HEAD_ITEM.apply(UsefulHead.PLUS_BUTTON).withName(LobbyMessage.FRIENDS_ADD.getGuiItem(this.guiName).getForPlayer(this.owner)).build(),
                e -> new SignGUI((player, lines) -> {
                    final boolean isName = lines[1] != null && !lines[1].isEmpty();

                    if (!isName) {
                        player.sendMessage(LobbyMessage.FRIENDS_ADD_NOT_NULL.getGuiItem(this.guiName).getForPlayer(player));
                        ThreadUtil.backOnMainThread(this.plugin, () -> {
                            player.closeInventory();
                            this.open();
                        });
                        return;
                    }

                    final UUID uuid = HyriAPI.get().getPlayerManager().getPlayerId(lines[1]);

                    if (uuid == null) {
                        player.sendMessage(LobbyMessage.FRIENDS_ADD_CANT_FOUND.getGuiItem(this.guiName).getForPlayer(player) + lines[1]);
                        ThreadUtil.backOnMainThread(this.plugin, () -> {
                            player.closeInventory();
                            this.open();
                        });
                        return;
                    }

                    if (this.friendHandler.areFriends(uuid)) {
                        player.sendMessage(LobbyMessage.FRIENDS_ADD_ALREADY_FRIEND.getGuiItem(this.guiName).getForPlayer(player) + lines[1]);
                        ThreadUtil.backOnMainThread(this.plugin, () -> {
                            player.closeInventory();
                            this.open();
                        });
                        return;
                    }

                    this.friendHandler.addFriend(uuid);
                    this.friendManager.updateFriends(this.friendHandler);
                    player.sendMessage(LobbyMessage.FRIENDS_ADD_SUCCESS.getGuiItem(this.guiName).getForPlayer(player) + lines[1]);
                }).withLines("", "^^^^^^^^^^^^^^^^", LobbyMessage.FRIENDS_ADD_ENTER_NAME.getGuiItem(this.guiName).getForPlayer(this.owner), " ").open(this.owner)
        );

        final List<Map.Entry<ItemStack, Consumer<InventoryClickEvent>>> entry = new ArrayList<>(items.entrySet());

        int index = 0;
        for (int i : InventoryUtil.getAvailableSlots()) {
            if (index >= items.size()) {
                break;
            }

            if (this.inventory.getItem(i) == null) {
                final Map.Entry<ItemStack, Consumer<InventoryClickEvent>> itemEntry = entry.get(index);
                this.setItem(i, itemEntry.getKey(), event -> itemEntry.getValue().accept(event));
            }
            index++;
        }
    }

    private List<String> getFriendLore(IHyriPlayer player) {
        final List<String> lore = new ArrayList<>();

        lore.add(LobbyMessage.FRIENDS_LEVEL.getGuiItem(this.guiName).getForPlayer(player) + player.getHyris().getAmount());

        final DurationConverter duration = new DurationConverter(Duration.ofMillis(System.currentTimeMillis() - player.getLastLoginDate().getTime()));
        lore.add(LobbyMessage.FRIENDS_LAST_SEEN.getGuiItem(this.guiName).getForPlayer(player) + RandomTools.getDurationMessage(duration, this.owner));

        lore.add("");
        lore.add(LobbyMessage.FRIENDS_REMOVE.getGuiItem(this.guiName).getForPlayer(player));

        return lore;
    }
}
