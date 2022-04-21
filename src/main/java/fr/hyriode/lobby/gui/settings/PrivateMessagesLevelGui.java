package fr.hyriode.lobby.gui.settings;

import fr.hyriode.api.settings.HyriPrivateMessagesLevel;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHead;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class PrivateMessagesLevelGui extends LobbyInventory {

    private final IHyriPlayerSettings settings;
    private HyriPrivateMessagesLevel level;

    public PrivateMessagesLevelGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "settings", "settings.messages_level", 36);

        this.settings = this.account.getSettings();
        this.level = this.settings.getPrivateMessagesLevel();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fillOutline(FILL_ITEM);

        this.setItem(20, this.getItem(DyeColor.LIME, HyriPrivateMessagesLevel.ALL), e -> this.onLevelClick(e, HyriPrivateMessagesLevel.ALL));
        this.setItem(22, this.getItem(DyeColor.ORANGE, HyriPrivateMessagesLevel.FRIENDS), e -> this.onLevelClick(e, HyriPrivateMessagesLevel.FRIENDS));
        this.setItem(24, this.getItem(DyeColor.RED, HyriPrivateMessagesLevel.NONE), e -> this.onLevelClick(e, HyriPrivateMessagesLevel.NONE));

        this.setupCurrentButton(HEAD_ITEM.apply(UsefulHead.ARROW_DOWN).build(), this.getSlot(), slot -> this.getMessage("current") + this.getLangMessage(this.level), null);
        this.setupReturnButton(new SettingsGui(this.plugin, this.owner), null);
    }

    private void onLevelClick(InventoryClickEvent event, HyriPrivateMessagesLevel level) {
        this.level = level;
        this.settings.setPrivateMessagesLevel(this.level);

        this.updateCurrentButton(this.getSlot(), event);
        this.fillOutline(FILL_ITEM);
    }

    private int getSlot() {
        switch (this.level) {
            case ALL: return 20 - 9;
            case FRIENDS: return 22 - 9;
            case NONE: return 24 - 9;
            default: return 0;
        }
    }

    private String getLangMessage(HyriPrivateMessagesLevel level) {
        return this.getMessage("placeholder", "settings.level." + level.name().toLowerCase());
    }

    private ItemStack getItem(DyeColor color, HyriPrivateMessagesLevel level) {
        return new ItemBuilder(new Wool(color).toItemStack(1)).withName(this.getMessage("button") + this.getLangMessage(level)).build();
    }
}
