package fr.hyriode.lobby.gui.settings;

import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHead;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class SettingsGui extends LobbyInventory {

    private final IHyriPlayerSettings settings;

    public SettingsGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "settings", 54);

        this.settings = this.account.getSettings();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fillOutline(FILL_ITEM);
        this.placeQuitButton(e -> this.owner.closeInventory());

        //Items part
        this.setItem(10, HEAD_ITEM.apply(UsefulHead.NOTEBLOCK).withName(this.getMessage("sound_tag")).build());
        this.setItem(11, HEAD_ITEM.apply(UsefulHead.NOTEBLOCK).withName(this.getMessage("messages_sound")).build());
        this.setItem(13, new ItemBuilder(Material.BOOK).withName(this.getMessage("global_chat")).build());
        this.setItem(15, new ItemBuilder(Material.PAPER).withName(this.getMessage("party_request")).build());
        this.setItem(16, new ItemBuilder(Material.PAPER).withName(this.getMessage("friend_request")).build());

        //Items with Consumer part
        this.setItem(38, HEAD_ITEM.apply(UsefulHead.EARTH).withName(this.getMessage("language")).build(),
                e -> new LanguageGui(this.plugin, this.owner).open()
        );
        this.setItem(40, new ItemBuilder(Material.REDSTONE_COMPARATOR).withName(this.getMessage("messages_level")).build(),
                e -> new PrivateMessagesLevelGui(this.plugin, this.owner).open()
        );
        this.setItem(42, new ItemBuilder(Material.EYE_OF_ENDER).withName(this.getMessage("visibility_level")).build(),
                e-> new PlayersVisibilityLevelGui(this.plugin, this.owner).open()
        );

        //Switch part
        this.setItem(19, new ItemBuilder(this.createSwitch(this.settings.isTagSoundEnabled())).build(), e -> {
            this.settings.setTagSoundEnabled(!this.settings.isTagSoundEnabled());
            e.getInventory().setItem(19, this.updateSwitch(this.settings.isTagSoundEnabled(), e.getCurrentItem()));
        });
        this.setItem(20, new ItemBuilder(this.createSwitch(this.settings.isPrivateMessagesSoundEnabled())).build(), e -> {
            this.settings.setPrivateMessagesSoundEnabled(!this.settings.isPrivateMessagesSoundEnabled());
            e.getInventory().setItem(20, this.updateSwitch(this.settings.isPrivateMessagesSoundEnabled(), e.getCurrentItem()));
        });
        this.setItem(22, new ItemBuilder(this.createSwitch(this.settings.isGlobalChatMessagesEnabled())).build(), e -> {
            this.settings.setGlobalChatMessagesEnabled(!this.settings.isGlobalChatMessagesEnabled());
            e.getInventory().setItem(22, this.updateSwitch(this.settings.isGlobalChatMessagesEnabled(), e.getCurrentItem()));
        });
        this.setItem(24, new ItemBuilder(this.createSwitch(this.settings.isPartyRequestsEnabled())).build(), e -> {
            this.settings.setPartyRequestsEnabled(!this.settings.isPartyRequestsEnabled());
            e.getInventory().setItem(24, this.updateSwitch(this.settings.isPartyRequestsEnabled(), e.getCurrentItem()));
        });
        this.setItem(25, new ItemBuilder(this.createSwitch(this.settings.isFriendRequestsEnabled())).build(), e -> {
            this.settings.setFriendRequestsEnabled(!this.settings.isFriendRequestsEnabled());
            e.getInventory().setItem(25, this.updateSwitch(this.settings.isFriendRequestsEnabled(), e.getCurrentItem()));
        });
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        this.playerManager.sendPlayer(this.account);
    }
}
