package fr.hyriode.lobby.gui;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.settings.LanguageGui;
import fr.hyriode.lobby.gui.settings.PlayersVisibilityLevelGui;
import fr.hyriode.lobby.gui.settings.PrivateMessagesLevelGui;
import fr.hyriode.lobby.util.References;
import fr.hyriode.lobby.util.UsefulHeads;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

public class SettingsGui extends HyriInventory {

    private static final String BASE = "item.settings.";

    private final HyriLobby plugin;
    private final IHyriLanguageManager lang;

    private final IHyriPlayer player;
    private final IHyriPlayerManager pm;
    private final IHyriPlayerSettings settings;

    private boolean isTagSoundEnabled;
    private boolean isPartyRequestsEnabled;
    private boolean isFriendRequestsEnabled;
    private boolean isGlobalChatMessagesEnabled;
    private boolean isPrivateMessagesSoundEnabled;

    public SettingsGui(HyriLobby plugin, Player owner) {
        super(owner, plugin.getHyrame().getLanguageManager().getValue(owner, "title.settings.gui"), 54);

        this.plugin = plugin;
        this.lang = plugin.getHyrame().getLanguageManager();

        this.pm = plugin.getAPI().getPlayerManager();
        this.player = this.pm.getPlayer(this.owner.getUniqueId());

        this.settings = this.player.getSettings();

        this.isTagSoundEnabled = this.settings.isTagSoundEnabled();
        this.isPartyRequestsEnabled = this.settings.isPartyRequestsEnabled();
        this.isFriendRequestsEnabled = this.settings.isFriendRequestsEnabled();
        this.isGlobalChatMessagesEnabled = this.settings.isGlobalChatMessagesEnabled();
        this.isPrivateMessagesSoundEnabled = this.settings.isPrivateMessagesSoundEnabled();

        this.init();
    }

    private void init() {
        this.inventory.clear();

        //Items part
        this.setItem(10, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.NOTEBLOCK.getTexture()).withName(this.lang.getValue(this.player, "item.settings.soundTag")).build());
        this.setItem(11, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.NOTEBLOCK.getTexture()).withName(this.lang.getValue(this.player, "item.settings.privateMessagesSound")).build());
        this.setItem(13, new ItemBuilder(Material.BOOK).withName(this.lang.getValue(this.player, BASE + "globalChat")).build());
        this.setItem(15, new ItemBuilder(Material.PAPER).withName(this.lang.getValue(this.player, BASE + "partyRequest")).build());
        this.setItem(16, new ItemBuilder(Material.PAPER).withName(this.lang.getValue(this.player, BASE + "friendRequest")).build());

        //Items with Consumer part
        this.setItem(38, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.EARTH.getTexture()).withName(this.lang.getValue(this.player, "item.settings.language")).build(),
                e -> new LanguageGui(this.plugin, this.owner, this).open()
        );
        this.setItem(40, new ItemBuilder(Material.REDSTONE_COMPARATOR).withName(this.lang.getValue(this.player, BASE + "privateMessagesLevel")).build(),
                e -> new PrivateMessagesLevelGui(plugin, this.owner, this).open()
        );
        this.setItem(42, new ItemBuilder(Material.EYE_OF_ENDER).withName(this.lang.getValue(this.player, BASE + "playersVisibilityLevel")).build(),
                e-> new PlayersVisibilityLevelGui(plugin, this.owner, this).open()
        );
        this.setItem(49, new ItemBuilder(Material.BARRIER).withName(this.lang.getValue(this.player, BASE + "quit")).build(),
                e -> e.getWhoClicked().closeInventory()
        );

        //Switch part
        this.setItem(19, new ItemBuilder(this.createSwitch(this.isTagSoundEnabled)).build(), e -> {
            e.getInventory().setItem(19, this.createSwitch(this.isTagSoundEnabled = !this.isTagSoundEnabled, e.getCurrentItem()));
            this.settings.setTagSoundEnabled(this.isTagSoundEnabled);
        });
        this.setItem(20, new ItemBuilder(this.createSwitch(this.isPrivateMessagesSoundEnabled)).build(), e -> {
            e.getInventory().setItem(20, createSwitch(this.isPrivateMessagesSoundEnabled = !this.isPrivateMessagesSoundEnabled, e.getCurrentItem()));
            this.settings.setPrivateMessagesSoundEnabled(this.isPrivateMessagesSoundEnabled);
        });
        this.setItem(22, new ItemBuilder(this.createSwitch(this.isGlobalChatMessagesEnabled)).build(), e -> {
            e.getInventory().setItem(22, this.createSwitch(this.isGlobalChatMessagesEnabled = !this.isGlobalChatMessagesEnabled, e.getCurrentItem()));
            this.settings.setGlobalChatMessagesEnabled(this.isGlobalChatMessagesEnabled);
        });
        this.setItem(24, new ItemBuilder(this.createSwitch(this.isPartyRequestsEnabled)).build(), e -> {
            e.getInventory().setItem(24, this.createSwitch(this.isPartyRequestsEnabled = !this.isPartyRequestsEnabled, e.getCurrentItem()));
            this.settings.setPartyRequestsEnabled(this.isPartyRequestsEnabled);
        });
        this.setItem(25, new ItemBuilder(this.createSwitch(this.isFriendRequestsEnabled)).build(), e -> {
            e.getInventory().setItem(25, this.createSwitch(this.isFriendRequestsEnabled = !this.isFriendRequestsEnabled, e.getCurrentItem()));
            this.settings.setFriendRequestsEnabled(this.isFriendRequestsEnabled);
        });

        //Fill part
        this.setFill(References.FILL_ITEM);
    }

    private ItemStack createSwitch(boolean option) {
        Dye dye = new Dye(Material.INK_SACK);
        dye.setColor(option ? DyeColor.LIME : DyeColor.RED);
        return new ItemBuilder(dye.toItemStack(1)).withName(this.lang.getValue(this.player, BASE + "switch.base") + this.getSwitchName(option)).build();
    }

    private ItemStack createSwitch(boolean option, ItemStack item) {
        item.setDurability((short) (option ? 10 : 1));
        return new ItemBuilder(item).withName(this.lang.getValue(this.player, BASE + "switch.base") + this.getSwitchName(option)).build();
    }

    private String getSwitchName(boolean option) {
        final String baseKey = BASE + "switch.";
        return option ? this.lang.getValue(this.player, baseKey + "off") : this.lang.getValue(this.player, baseKey + "on");
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        this.pm.sendPlayer(this.player);
    }
}
