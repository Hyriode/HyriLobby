package fr.hyriode.hyrilobby.gui.settings;

import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.gui.LobbyInventory;
import fr.hyriode.hyrilobby.language.LobbyMessage;
import fr.hyriode.hyrilobby.util.UsefulHead;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SettingsGui extends LobbyInventory {

    private final IHyriPlayerSettings settings;

    public SettingsGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "settings", "settings", 54);

        this.settings = this.account.getSettings();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fill();

        //Items part
        this.setItem(10, HEAD_ITEM.apply(UsefulHead.NOTE_BLOCK).withName(LobbyMessage.SETTINGS_MSG_SOUND.get().getForPlayer(this.owner)).build());
        this.setItem(12, new ItemBuilder(Material.BOOK).withName(LobbyMessage.SETTINGS_GLOBAL_CHAT.get().getForPlayer(this.owner)).build());
        this.setItem(14, new ItemBuilder(Material.PAPER).withName(LobbyMessage.SETTINGS_PARTY_REQUEST.get().getForPlayer(this.owner)).build());
        this.setItem(16, new ItemBuilder(Material.PAPER).withName(LobbyMessage.SETTINGS_FRIEND_REQUEST.get().getForPlayer(this.owner)).build());

        //Items with Consumer part
        this.setItem(38, HEAD_ITEM.apply(UsefulHead.EARTH).withName(LobbyMessage.SETTINGS_LANG.get().getForPlayer(this.owner)).build(),
                e -> new LanguageGui(this.plugin, this.owner).open()
        );
        this.setItem(40, new ItemBuilder(Material.REDSTONE_COMPARATOR).withName(LobbyMessage.SETTINGS_MSG_LEVELS.get().getForPlayer(this.owner)).build(),
                e -> new PrivateMessagesLevelGui(this.plugin, this.owner).open()
        );
        this.setItem(42, new ItemBuilder(Material.EYE_OF_ENDER).withName(LobbyMessage.SETTINGS_VISIBILITY_LEVELS.get().getForPlayer(this.owner)).build(),
                e-> new PlayersVisibilityLevelGui(this.plugin, this.owner).open()
        );

        //Switch part
        this.setItem(19, new ItemBuilder(this.createSwitch(this.settings.isPrivateMessagesSoundEnabled())).build(), e -> {
            this.settings.setPrivateMessagesSoundEnabled(!this.settings.isPrivateMessagesSoundEnabled());
            e.getInventory().setItem(19, this.updateSwitch(this.settings.isPrivateMessagesSoundEnabled(), e.getCurrentItem()));
        });
        this.setItem(21, new ItemBuilder(this.createSwitch(this.settings.isGlobalChatMessagesEnabled())).build(), e -> {
            this.settings.setGlobalChatMessagesEnabled(!this.settings.isGlobalChatMessagesEnabled());
            e.getInventory().setItem(21, this.updateSwitch(this.settings.isGlobalChatMessagesEnabled(), e.getCurrentItem()));
        });
        this.setItem(23, new ItemBuilder(this.createSwitch(this.settings.isPartyRequestsEnabled())).build(), e -> {
            this.settings.setPartyRequestsEnabled(!this.settings.isPartyRequestsEnabled());
            e.getInventory().setItem(23, this.updateSwitch(this.settings.isPartyRequestsEnabled(), e.getCurrentItem()));
        });
        this.setItem(25, new ItemBuilder(this.createSwitch(this.settings.isFriendRequestsEnabled())).build(), e -> {
            this.settings.setFriendRequestsEnabled(!this.settings.isFriendRequestsEnabled());
            e.getInventory().setItem(25, this.updateSwitch(this.settings.isFriendRequestsEnabled(), e.getCurrentItem()));
        });
    }
}
