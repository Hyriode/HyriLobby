package fr.hyriode.lobby.gui;

import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.settings.LanguageGui;
import fr.hyriode.lobby.gui.settings.PlayersVisibilityLevelGui;
import fr.hyriode.lobby.gui.settings.PrivateMessagesLevelGui;
import fr.hyriode.lobby.player.PlayerManager;
import fr.hyriode.lobby.util.UsefulHeads;
import fr.hyriode.tools.inventory.AbstractInventory;
import fr.hyriode.tools.item.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

public class SettingsGui extends AbstractInventory {

    private final Player player;
    private final HyriLobby plugin;
    private final IHyriPlayer hyriPlayer;
    private final IHyriLanguageManager lang;
    private final IHyriPlayerSettings hyriSettings;
    private final IHyriPlayerManager hyriPlayerManager;

    private boolean isTagSoundEnabled;
    private boolean isPartyRequestsEnabled;
    private boolean isFriendRequestsEnabled;
    private boolean isGlobalChatMessagesEnabled;
    private boolean isPrivateMessagesSoundEnabled;

    public SettingsGui(HyriLobby plugin, Player owner) {
        super(owner, plugin.getHyrame().getLanguageManager().getMessageValueForPlayer(owner, "title.settings.gui"), 54);

        this.player = owner;
        this.plugin = plugin;
        this.lang = plugin.getHyrame().getLanguageManager();
        PlayerManager manager = PlayerManager.getByUuid(this.player.getUniqueId());
        this.hyriPlayer = manager.getPlayer();
        this.hyriSettings = this.hyriPlayer.getSettings();
        this.hyriPlayerManager = HyriAPI.get().getPlayerManager();

        this.isTagSoundEnabled = this.hyriSettings.isTagSoundEnabled();
        this.isPartyRequestsEnabled = this.hyriSettings.isPartyRequestsEnabled();
        this.isFriendRequestsEnabled = this.hyriSettings.isFriendRequestsEnabled();
        this.isGlobalChatMessagesEnabled = this.hyriSettings.isGlobalChatMessagesEnabled();
        this.isPrivateMessagesSoundEnabled = this.hyriSettings.isPrivateMessagesSoundEnabled();

        this.addFillItem();
        this.addSoundTagItem();
        this.addSoundTagSwitch();
        this.addPrivateMessagesSoundItem();
        this.addPrivateMessagesSoundSwitch();
        this.addGlobalChatItem();
        this.addGlobalChatSwitch();
        this.addPartyRequestItem();
        this.addPartyRequestSwitch();
        this.addFriendRequestItem();
        this.addFriendRequestSwitch();
        this.addLanguageItem();
        this.addPrivateMessagesLevelItem();
        this.addPlayersVisibilityLevelItem();
        this.addCloseItem();
    }

    private void addFillItem() {
        this.setFill(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15)
                .withName(" ").build());
    }

    private void addSoundTagItem() {
        this.setItem(10, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.NOTEBLOCK.getTexture())
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.soundTag")).build());
    }

    private void addSoundTagSwitch() {
        this.setItem(19, new ItemBuilder(this.createSwitch(this.isTagSoundEnabled))
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.switch.base") + this.getSwitchName(this.isTagSoundEnabled))
                .build(), e -> e.getInventory().setItem(19, this.createSwitch(this.isTagSoundEnabled = !this.isTagSoundEnabled, e.getCurrentItem())));
    }

    private void addPartyRequestItem() {
        this.setItem(15, new ItemBuilder(Material.PAPER)
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.partyRequest")).build());
    }

    private void addPartyRequestSwitch() {
        this.setItem(24, new ItemBuilder(this.createSwitch(this.isPartyRequestsEnabled))
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.switch.base") + this.getSwitchName(this.isPartyRequestsEnabled))
                .build(), e -> {
                    this.isPartyRequestsEnabled = !this.isPartyRequestsEnabled;
                    this.hyriSettings.setPartyRequestsEnabled(this.isPartyRequestsEnabled);
                    e.getInventory().setItem(24, this.createSwitch(this.isPartyRequestsEnabled, e.getCurrentItem()));
                });
    }

    private void addFriendRequestItem() {
        this.setItem(16, new ItemBuilder(Material.PAPER)
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.friendRequest")).build());
    }

    private void addFriendRequestSwitch() {
        this.setItem(25, new ItemBuilder(this.createSwitch(this.isFriendRequestsEnabled))
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.switch.base") + this.getSwitchName(this.isFriendRequestsEnabled))
                .build(), e -> {
                    this.isFriendRequestsEnabled = !this.isFriendRequestsEnabled;
                    this.hyriSettings.setFriendRequestsEnabled(this.isFriendRequestsEnabled);
                    e.getInventory().setItem(25, this.createSwitch(this.isFriendRequestsEnabled, e.getCurrentItem()));
                });
    }

    private void addGlobalChatItem() {
        this.setItem(13, new ItemBuilder(Material.BOOK)
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.globalChat")).build());
    }

    private void addGlobalChatSwitch() {
        this.setItem(22, new ItemBuilder(this.createSwitch(this.isGlobalChatMessagesEnabled))
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.switch.base") + this.getSwitchName(this.isGlobalChatMessagesEnabled))
                .build(), e -> {
                    this.isGlobalChatMessagesEnabled = !this.isGlobalChatMessagesEnabled;
                    this.hyriSettings.setGlobalChatMessagesEnabled(this.isGlobalChatMessagesEnabled);
                    e.getInventory().setItem(22, this.createSwitch(this.isGlobalChatMessagesEnabled, e.getCurrentItem()));
                });
    }

    private void addPrivateMessagesSoundItem() {
        this.setItem(11, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.NOTEBLOCK.getTexture())
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.privateMessagesSound")).build());
    }

    private void addPrivateMessagesSoundSwitch() {
        this.setItem(20, new ItemBuilder(this.createSwitch(this.isPrivateMessagesSoundEnabled))
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.switch.base") + this.getSwitchName(this.isPrivateMessagesSoundEnabled))
                .build(), e -> {
                    this.isPrivateMessagesSoundEnabled = !this.isPrivateMessagesSoundEnabled;
                    this.hyriSettings.setPrivateMessagesSoundEnabled(this.isPrivateMessagesSoundEnabled);
                    e.getInventory().setItem(20, createSwitch(this.isPrivateMessagesSoundEnabled, e.getCurrentItem()));
                });
    }

    private void addLanguageItem() {
        this.setItem(38, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.EARTH.getTexture())
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.language"))
                .build(), e -> new LanguageGui(this.plugin, this.player, this.hyriPlayer, this.hyriPlayerManager, this).open());
    }

    private void addPrivateMessagesLevelItem() {
        this.setItem(40, new ItemBuilder(Material.REDSTONE_COMPARATOR)
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.privateMessagesLevel"))
                .build(), e -> new PrivateMessagesLevelGui(plugin, this.player, this.hyriPlayer, this.hyriPlayerManager, this).open());
    }

    private void addPlayersVisibilityLevelItem() {
        this.setItem(42, new ItemBuilder(Material.EYE_OF_ENDER)
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.playersVisibilityLevel"))
                .build(), e-> new PlayersVisibilityLevelGui(plugin, this.player, this.hyriPlayer, this.hyriPlayerManager, this).open());
    }

    private void addCloseItem() {
        this.setItem(49, new ItemBuilder(Material.BARRIER)
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.quit"))
                .build(), e -> e.getWhoClicked().closeInventory());
    }

    private ItemStack createSwitch(boolean option) {
        Dye dye = new Dye(Material.INK_SACK);
        if (option) {
            dye.setColor(DyeColor.LIME);
        } else {
            dye.setColor(DyeColor.RED);
        }
        return new ItemBuilder(dye.toItemStack(1)).withName(this.lang.getMessageValueForPlayer(this.player, "item.settings.switch.base") + this.getSwitchName(option)).build();
    }

    private ItemStack createSwitch(boolean option, ItemStack itemStack) {
        final ItemMeta meta = itemStack.getItemMeta();
        if (option) {
            itemStack.setDurability((short) 10);
        } else {
            itemStack.setDurability((short) 1);
        }
        meta.setDisplayName(this.lang.getMessageValueForPlayer(this.player, "item.settings.switch.base") + this.getSwitchName(option));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private String getSwitchName(boolean option) {
        if (option) {
            return this.lang.getMessageValueForPlayer(this.player, "item.Settings.switch.off");
        } else return this.lang.getMessageValueForPlayer(this.player, "item.Settings.switch.on");
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        this.hyriPlayerManager.sendPlayer(this.hyriPlayer);
    }
}
