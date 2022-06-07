package fr.hyriode.hyrilobby.gui.settings;

import fr.hyriode.api.settings.HyriPrivateMessagesLevel;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.gui.LobbyInventory;
import fr.hyriode.hyrilobby.language.LobbyMessage;
import fr.hyriode.hyrilobby.util.UsefulHead;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class PrivateMessagesLevelGui extends LobbyInventory {

    private final IHyriPlayerSettings settings;
    private HyriPrivateMessagesLevel level;

    public PrivateMessagesLevelGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "settings_messages_level", "settings.messages-level", 54);

        this.settings = this.account.getSettings();
        this.level = this.settings.getPrivateMessagesLevel();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fill();

        this.setItem(29, this.getItem(DyeColor.LIME, HyriPrivateMessagesLevel.ALL), e -> this.onLevelClick(e, HyriPrivateMessagesLevel.ALL));
        this.setItem(31, this.getItem(DyeColor.ORANGE, HyriPrivateMessagesLevel.FRIENDS), e -> this.onLevelClick(e, HyriPrivateMessagesLevel.FRIENDS));
        this.setItem(33, this.getItem(DyeColor.RED, HyriPrivateMessagesLevel.NONE), e -> this.onLevelClick(e, HyriPrivateMessagesLevel.NONE));

        this.setupCurrentButton(HEAD_ITEM.apply(UsefulHead.ARROW_DOWN).build(), this.getSlot(), slot -> LobbyMessage.CURRENT_ITEM.get().getForPlayer(this.owner) + this.getLangMessage(this.level), null);
    }

    private void onLevelClick(InventoryClickEvent event, HyriPrivateMessagesLevel level) {
        this.level = level;
        this.settings.setPrivateMessagesLevel(this.level);

        this.account.update();

        this.updateCurrentButton(this.getSlot(), event);
        this.fill();
    }

    private int getSlot() {
        switch (this.level) {
            case ALL: return 29 - 9;
            case FRIENDS: return 31 - 9;
            case NONE: return 33 - 9;
            default: return 0;
        }
    }

    private String getLangMessage(HyriPrivateMessagesLevel level) {
        return HyriLobby.getLanguageManager().getMessage("settings.level." + level.name().toLowerCase()).getForPlayer(this.owner);
    }

    private ItemStack getItem(DyeColor color, HyriPrivateMessagesLevel level) {
        return new ItemBuilder(new Wool(color).toItemStack(1)).withName(this.getLangMessage(level)).build();
    }
}
