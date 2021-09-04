package fr.hyriode.hyrilobby.gui;

import fr.hyriode.common.inventory.AbstractInventory;
import fr.hyriode.common.item.ItemBuilder;
import fr.hyriode.hyrame.language.LanguageManager;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.gui.settings.LanguageGui;
import fr.hyriode.hyrilobby.gui.settings.PlayersVisibilityLevelGui;
import fr.hyriode.hyrilobby.gui.settings.PrivateMessagesLevelGui;
import fr.hyriode.hyrilobby.player.PlayerManager;
import fr.hyriode.hyrilobby.util.UsefulHeads;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import java.util.logging.Logger;

public class SettingsGui extends AbstractInventory {

    private Player player;
    private HyriLobby plugin;
    private LanguageManager lang;
    private PlayerManager manager;
    private IHyriPlayer hyriPlayer;
    private IHyriPlayerSettings hyriSettings;
    private IHyriPlayerManager hyriPlayerManager;

    private boolean isTagSoundEnabled;
    private boolean isPartyRequestsEnabled;
    private boolean isFriendRequestsEnabled;
    private boolean isGlobalChatMessagesEnabled;
    private boolean isPrivateMessagesSoundEnabled;

    private ItemStack fillItem;
    private ItemStack closeItem;
    private ItemStack soundTagItem;
    private ItemStack soundTagSwitch;
    private ItemStack globalChatItem;
    private ItemStack globalChatSwitch;
    private ItemStack partyRequestItem;
    private ItemStack partyRequestSwitch;
    private ItemStack friendRequestItem;
    private ItemStack friendRequestSwitch;
    private ItemStack privateMessagesSoundItem;
    private ItemStack privateMessagesSoundSwitch;

    private ItemStack languageItem;
    private ItemStack privateMessagesLevelItem;
    private ItemStack playersVisibilityLevelItem;

    private Logger logger = Bukkit.getLogger();

    public SettingsGui(Player owner) {
        super(owner, HyriLobby.getInstance().getLanguageManager().getMessageForPlayer(owner, "title.settings.gui"), 54);

        this.player = owner;
        this.plugin = HyriLobby.getInstance();
        this.lang = HyriLobby.getInstance().getLanguageManager();
        this.manager = PlayerManager.getByUuid(this.player.getUniqueId());
        this.hyriPlayer = this.manager.getPlayer();
        this.hyriSettings = this.hyriPlayer.getSettings();
        this.hyriPlayerManager = HyriAPI.get().getPlayerManager();

        this.isTagSoundEnabled = this.hyriSettings.isTagSoundEnabled();
        this.isPartyRequestsEnabled = this.hyriSettings.isPartyRequestsEnabled();
        this.isFriendRequestsEnabled = this.hyriSettings.isFriendRequestsEnabled();
        this.isGlobalChatMessagesEnabled = this.hyriSettings.isGlobalChatMessagesEnabled();
        this.isPrivateMessagesSoundEnabled = this.hyriSettings.isPrivateMessagesSoundEnabled();

        /*
            Create Items
         */
        this.fillItem = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15)
                .withName(" ").build();
        this.soundTagItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.NOTEBLOCK.getTexture())
                .withName(this.lang.getMessageForPlayer(this.player, "item.settings.soundTag")).build();
        this.soundTagSwitch = new ItemBuilder(this.createSwitch(this.isTagSoundEnabled))
                .withName(this.lang.getMessageForPlayer(this.player, "item.settings.switch.base") + this.getSwitchName(this.isTagSoundEnabled)).build();
        this.partyRequestItem = new ItemBuilder(Material.PAPER)
                .withName(this.lang.getMessageForPlayer(this.player, "item.settings.partyRequest")).build();
        this.partyRequestSwitch = new ItemBuilder(this.createSwitch(this.isPartyRequestsEnabled))
                .withName(this.lang.getMessageForPlayer(this.player, "item.settings.switch.base") + this.getSwitchName(this.isPartyRequestsEnabled)).build();
        this.friendRequestItem = new ItemBuilder(Material.PAPER)
                .withName(this.lang.getMessageForPlayer(this.player, "item.settings.friendRequest")).build();
        this.friendRequestSwitch = new ItemBuilder(this.createSwitch(this.isFriendRequestsEnabled))
                .withName(this.lang.getMessageForPlayer(this.player, "item.settings.switch.base") + this.getSwitchName(this.isFriendRequestsEnabled)).build();
        this.globalChatItem = new ItemBuilder(Material.BOOK)
                .withName(this.lang.getMessageForPlayer(this.player, "item.settings.globalChat")).build();
        this.globalChatSwitch = new ItemBuilder(this.createSwitch(this.isGlobalChatMessagesEnabled))
                .withName(this.lang.getMessageForPlayer(this.player, "item.settings.switch.base") + this.getSwitchName(this.isGlobalChatMessagesEnabled)).build();
        this.privateMessagesSoundItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.NOTEBLOCK.getTexture())
                .withName(this.lang.getMessageForPlayer(this.player, "item.settings.privateMessagesSound")).build();
        this.privateMessagesSoundSwitch = new ItemBuilder(this.createSwitch(this.isPrivateMessagesSoundEnabled))
                .withName(this.lang.getMessageForPlayer(this.player, "item.settings.switch.base") + this.getSwitchName(this.isPrivateMessagesSoundEnabled)).build();
        this.languageItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.EARTH.getTexture())
                .withName(this.lang.getMessageForPlayer(this.player, "item.settings.language")).build();
        this.privateMessagesLevelItem = new ItemBuilder(Material.REDSTONE_COMPARATOR)
                .withName(this.lang.getMessageForPlayer(this.player, "item.settings.privateMessagesLevel")).build();
        this.playersVisibilityLevelItem = new ItemBuilder(Material.EYE_OF_ENDER)
                .withName(this.lang.getMessageForPlayer(this.player, "item.settings.playersVisibilityLevel")).build();
        this.closeItem = new ItemBuilder(Material.BARRIER)
                .withName(this.lang.getMessageForPlayer(this.player, "item.settings.quit")).build();

        setFill(this.fillItem);
        setItem(10, this.soundTagItem);
        setItem(19, this.soundTagSwitch, e -> {
            this.isTagSoundEnabled = !this.isTagSoundEnabled;
            this.hyriSettings.setTagSoundEnabled(this.isTagSoundEnabled);
            this.soundTagSwitch = this.createSwitch(this.isTagSoundEnabled);
            e.getInventory().setItem(19, this.soundTagSwitch);
            this.player.updateInventory();
        });
        setItem(11, this.privateMessagesSoundItem);
        setItem(20, this.privateMessagesSoundSwitch, e -> {
            this.isPrivateMessagesSoundEnabled = !this.isPrivateMessagesSoundEnabled;
            this.hyriSettings.setPrivateMessagesSoundEnabled(this.isPrivateMessagesSoundEnabled);
            this.privateMessagesSoundSwitch = this.createSwitch(this.isPrivateMessagesSoundEnabled);
            e.getInventory().setItem(20, this.privateMessagesSoundSwitch);
            this.player.updateInventory();
        });
        setItem(13, this.globalChatItem);
        setItem(22, this.globalChatSwitch, e -> {
            this.isGlobalChatMessagesEnabled = !this.isGlobalChatMessagesEnabled;
            this.hyriSettings.setGlobalChatMessagesEnabled(this.isGlobalChatMessagesEnabled);
            this.globalChatSwitch = this.createSwitch(this.isGlobalChatMessagesEnabled);
            e.getInventory().setItem(22, this.globalChatSwitch);
            this.player.updateInventory();
        });
        setItem(15, this.partyRequestItem);
        setItem(24, this.partyRequestSwitch, e -> {
            this.isPartyRequestsEnabled = !this.isPartyRequestsEnabled;
            this.hyriSettings.setPartyRequestsEnabled(this.isPartyRequestsEnabled);
            this.partyRequestSwitch = this.createSwitch(this.isPartyRequestsEnabled);
            e.getInventory().setItem(24, this.partyRequestSwitch);
            this.player.updateInventory();
        });
        setItem(16, this.friendRequestItem);
        setItem(25, this.friendRequestSwitch, e -> {
            this.isFriendRequestsEnabled = !this.isFriendRequestsEnabled;
            this.hyriSettings.setFriendRequestsEnabled(this.isFriendRequestsEnabled);
            this.friendRequestSwitch = this.createSwitch(this.isFriendRequestsEnabled);
            e.getInventory().setItem(25, this.friendRequestSwitch);
            this.player.updateInventory();
        });
        setItem(38, this.languageItem, e -> {
            new LanguageGui(this.player, this.hyriPlayer, this.hyriPlayerManager, this).open();
        });
        setItem(40, this.privateMessagesLevelItem, e -> {
            new PrivateMessagesLevelGui(this.player, this.hyriPlayer, this.hyriPlayerManager, this).open();
        });
        setItem(42, this.playersVisibilityLevelItem, e -> {
            new PlayersVisibilityLevelGui(this.player, this.hyriPlayer, this.hyriPlayerManager, this).open();
        });
        setItem(49, this.closeItem, e -> {
            e.getWhoClicked().closeInventory();
        });
    }

    private ItemStack createSwitch(boolean option) {
        Dye dye = new Dye(Material.INK_SACK);
        if (option) {
            dye.setColor(DyeColor.LIME);
        } else {
            dye.setColor(DyeColor.RED);
        }
        return new ItemBuilder(dye.toItemStack(1)).withName(this.lang.getMessageForPlayer(this.player, "item.settings.switch.base") + this.getSwitchName(option)).build();
    }

    private String getSwitchName(boolean option) {
        if (option) {
            return this.lang.getMessageForPlayer(this.player, "item.Settings.switch.off");
        } else return this.lang.getMessageForPlayer(this.player, "item.Settings.switch.on");
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        this.hyriPlayerManager.sendPlayer(this.hyriPlayer);
    }
}
