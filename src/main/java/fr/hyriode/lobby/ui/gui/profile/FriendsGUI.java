package fr.hyriode.lobby.ui.gui.profile;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.player.model.IHyriFriend;
import fr.hyriode.hyrame.inventory.pagination.PaginatedItem;
import fr.hyriode.hyrame.inventory.pagination.PaginationArea;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.signgui.SignGUI;
import fr.hyriode.hyrame.utils.DurationFormatter;
import fr.hyriode.hyrame.utils.Pagination;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.hyrame.utils.TimeUtil;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.ui.gui.ConfirmGUI;
import fr.hyriode.lobby.ui.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class FriendsGUI extends LobbyGUI {

    public FriendsGUI(HyriLobby plugin, Player owner) {
        super(owner, plugin, () -> "friends", 54);
        this.paginationManager.setArea(new PaginationArea(20, 33));

        this.init();
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

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

        for (IHyriFriend friend : this.account.getFriends().getAll()) {
            pagination.add(PaginatedItem.from(this.createFriendItem(friend), this.clickEvent(friend)));
        }
    }

    private ItemStack createFriendItem(IHyriFriend friend) {
        final UUID friendId = friend.getUniqueId();
        final long whenAdded = friend.whenAdded();
        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(friendId);
        final IHyriPlayerSession session = IHyriPlayerSession.get(friendId);
        final long friendsFor = System.currentTimeMillis() - whenAdded;
        final String formattedFriendsFor = new DurationFormatter()
                .withDays(true)
                .withSeconds(friendsFor < 60 * 1000L)
                .format(this.account.getSettings().getLanguage(), friendsFor);
        final List<String> lore = ListReplacer.replace(LobbyMessage.FRIENDS_FRIEND_ITEM_LORE.asList(this.account), "%online%", session != null ? ChatColor.GREEN + Symbols.TICK_BOLD : ChatColor.RED + Symbols.CROSS_STYLIZED_BOLD)
                .replace("%friends_for%", formattedFriendsFor)
                .replace("%date%", TimeUtil.formatDate(new Date(whenAdded), "dd/MM/yyyy"))
                .replace("%premium%", account.getAuth().isPremium() ? ChatColor.GREEN + Symbols.TICK_BOLD : ChatColor.RED + Symbols.CROSS_STYLIZED_BOLD)
                .replace("%level%", String.valueOf(account.getNetworkLeveling().getLevel()))
                .list();

        return ItemBuilder.asHead()
                .withPlayerHead(friendId)
                .withName(account.getNameWithRank())
                .withLore(lore)
                .build();
    }

    private Consumer<InventoryClickEvent> clickEvent(IHyriFriend friend) {
        return event -> {
            if (event.isRightClick()) {
                new ConfirmGUI(this.owner, this.plugin, new ItemBuilder(this.createFriendItem(friend)).removeLoreLines(2).build())
                        .whenConfirm(e -> {
                            this.account.getFriends().remove(friend.getUniqueId());
                            this.account.update();

                            final IHyriPlayer friendAccount = IHyriPlayer.get(friend.getUniqueId());

                            friendAccount.getFriends().remove(this.owner.getUniqueId());
                            friendAccount.update();

                            this.owner.playSound(this.owner.getLocation(), Sound.FIZZ, 0.5F, 1.0F);

                            this.setupItems();
                            this.paginationManager.updateGUI();

                            this.open();
                        })
                        .whenCancel(e -> {
                            this.init();
                            this.open();
                        })
                        .open();
            }
        };
    }

}
