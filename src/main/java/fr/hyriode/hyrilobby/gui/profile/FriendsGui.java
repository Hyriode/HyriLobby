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

        this.setItem(50, HEAD_ITEM.apply(UsefulHead.PLUS_BUTTON).withName(this.getMessage("add")).build(),
                e -> new SignGUI((player, lines) -> {
                    final boolean isName = lines[1] != null && !lines[1].isEmpty();

                    if (!isName) {
                        player.sendMessage(this.getMessage("add.not_null"));
                        ThreadUtil.backOnMainThread(this.plugin, () -> {
                            player.closeInventory();
                            this.open();
                        });
                        return;
                    }

                    final UUID uuid = HyriAPI.get().getPlayerManager().getPlayerId(lines[1]);

                    if (uuid == null) {
                        player.sendMessage(this.getMessage("add.cant_found") + lines[1]);
                        ThreadUtil.backOnMainThread(this.plugin, () -> {
                            player.closeInventory();
                            this.open();
                        });
                        return;
                    }

                    if (this.friendHandler.areFriends(uuid)) {
                        player.sendMessage(this.getMessage("add.already_friend") + lines[1]);
                        ThreadUtil.backOnMainThread(this.plugin, () -> {
                            player.closeInventory();
                            this.open();
                        });
                        return;
                    }

                    this.friendHandler.addFriend(uuid);
                    this.friendManager.updateFriends(this.friendHandler);
                    player.sendMessage(this.getMessage("add.success") + lines[1]);
                }).withLines("", "^^^^^^^^^^^^^^^^", this.getMessage("add.name"), " ").open(this.owner)
        );

        this.setupReturnButton(new ProfileGui(this.plugin, this.owner), null);

        this.setupPagination(friends.size(), e -> this.init());
        this.tryToFill(10, this.getIndexFromPage(), items);
    }

    private List<String> getFriendLore(IHyriPlayer player) {
        final List<String> lore = new ArrayList<>();

        lore.add(this.getMessage("level") + player.getHyris().getAmount());

        final DurationConverter duration = new DurationConverter(Duration.ofMillis(System.currentTimeMillis() - player.getLastLoginDate().getTime()));
        lore.add(this.getMessage("last_seen") + RandomTools.getDurationMessage(duration, this.owner));

        lore.add("");
        lore.add(this.getMessage("remove"));

        return lore;
    }
}
