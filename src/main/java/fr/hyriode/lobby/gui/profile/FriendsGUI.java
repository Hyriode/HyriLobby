package fr.hyriode.lobby.gui.profile;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.friend.IHyriFriend;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.inventory.pagination.PaginatedItem;
import fr.hyriode.hyrame.inventory.pagination.PaginationArea;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.signgui.SignGUI;
import fr.hyriode.hyrame.utils.*;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.ListUtil;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FriendsGUI extends LobbyGUI {

    public FriendsGUI(HyriLobby plugin, Player owner) {
        super(owner, plugin, () -> "friends", 54);
        this.paginationManager.setArea(new PaginationArea(20, 33));

        this.init();
    }

    @Override
    protected void init() {
        this.border();

        this.setItem(4, ItemBuilder.asHead()
                .withHeadTexture(UsefulHead.MONITOR_PLUS)
                .withName(LobbyMessage.FRIENDS_ADD_FRIEND_ITEM_NAME.asString(this.account))
                .withLore(LobbyMessage.FRIENDS_ADD_FRIEND_ITEM_LORE.asList(this.account))
                .build(),
                event -> {
                    if (event.isLeftClick()) {
                        this.addFriend();
                    }
                });

        this.addPagesItems(27, 35);
        this.setupItems();

        this.paginationManager.updateGUI();
    }

    private void addFriend() {
        new SignGUI((player, lines) -> {
            this.open();

            this.owner.chat("/friend add " + lines[0]);
        }).withLines("", "^^^^^^^^", LobbyMessage.FRIENDS_ADD_FRIEND_SIGN_LINE.asString(this.account), "").open(this.owner);
    }

    private void setupItems() {
        final Pagination<PaginatedItem> pagination = this.paginationManager.getPagination();

        pagination.clear();

        for (IHyriFriend friend : HyriAPI.get().getFriendManager().getFriends(this.owner.getUniqueId())) {
            pagination.add(PaginatedItem.from(this.createFriendItem(friend), this.clickEvent(friend)));
        }
    }

    private ItemStack createFriendItem(IHyriFriend friend) {
        final UUID friendId = friend.getUniqueId();
        final Date whenAdded = friend.getWhenAdded();
        final IHyriPlayer cachedAccount = HyriAPI.get().getPlayerManager().getCachedPlayer(friendId);
        final boolean online = cachedAccount != null && cachedAccount.isOnline();
        final List<String> lore = LobbyMessage.FRIENDS_FRIEND_ITEM_LORE.asList(this.account);
        final BiConsumer<String, String> replacer = (character, value) -> ListUtil.replace(lore, character, value);
        final long friendsFor = System.currentTimeMillis() - whenAdded.getTime();
        final String formattedFriendsFor = new DurationFormatter()
                .withDays(true)
                .withSeconds(friendsFor < 60 * 1000L)
                .format(this.account.getSettings().getLanguage(), friendsFor);

        replacer.accept("%online%", online ? ChatColor.GREEN + Symbols.TICK_BOLD : ChatColor.RED + Symbols.CROSS_STYLIZED_BOLD);
        replacer.accept("%friends_for%", formattedFriendsFor);
        replacer.accept("%date%", TimeUtil.formatDate(whenAdded, "dd/MM/yyyy"));

        return ItemBuilder.asHead()
                .withPlayerHead(friendId)
                .withName(HyriAPI.get().getPlayerManager().getPrefix(friendId))
                .withLore(lore)
                .build();
    }

    private Consumer<InventoryClickEvent> clickEvent(IHyriFriend friend) {
        return event -> {
            if (event.isRightClick()) {
                HyriAPI.get().getFriendManager().createHandler(this.owner.getUniqueId()).removeFriend(friend.getUniqueId());

                this.owner.playSound(this.owner.getLocation(), Sound.FIZZ, 0.5F, 1.0F);

                this.setupItems();

                this.paginationManager.updateGUI();
            }
        };
    }

}
