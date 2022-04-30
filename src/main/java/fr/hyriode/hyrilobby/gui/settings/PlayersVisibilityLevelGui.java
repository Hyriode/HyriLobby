package fr.hyriode.hyrilobby.gui.settings;

import fr.hyriode.api.settings.HyriPlayersVisibilityLevel;
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

public class PlayersVisibilityLevelGui extends LobbyInventory {

    private final IHyriPlayerSettings settings;
    private HyriPlayersVisibilityLevel level;

    public PlayersVisibilityLevelGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "settings_visibility_level", "settings.visibility-level", 54);

        this.settings = this.account.getSettings();
        this.level = this.settings.getPlayersVisibilityLevel();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();
        this.fill();

        this.setItem(19, this.getItem(DyeColor.LIME, HyriPlayersVisibilityLevel.ALL), e -> this.onLevelClick(e, HyriPlayersVisibilityLevel.ALL));
        this.setItem(21, this.getItem(DyeColor.YELLOW, HyriPlayersVisibilityLevel.FRIENDS), e -> this.onLevelClick(e, HyriPlayersVisibilityLevel.FRIENDS));
        this.setItem(23, this.getItem(DyeColor.ORANGE, HyriPlayersVisibilityLevel.PARTY), e -> this.onLevelClick(e, HyriPlayersVisibilityLevel.PARTY));
        this.setItem(25, this.getItem(DyeColor.RED, HyriPlayersVisibilityLevel.NONE), e -> this.onLevelClick(e, HyriPlayersVisibilityLevel.NONE));

        this.setupCurrentButton(HEAD_ITEM.apply(UsefulHead.ARROW_DOWN).build(), this.getSlot(), slot -> LobbyMessage.CURRENT_ITEM.get().getForPlayer(this.owner) + this.getLangMessage(this.level), null);
    }

    private void onLevelClick(InventoryClickEvent event, HyriPlayersVisibilityLevel level) {
        this.level = level;
        this.settings.setPlayersVisibilityLevel(this.level);

        this.account.update();

        this.updateCurrentButton(this.getSlot(), event);
        this.fill();
    }

    private int getSlot() {
        switch (this.level) {
            case ALL: return 19 - 9;
            case FRIENDS: return 21 - 9;
            case PARTY: return 23 - 9;
            case NONE: return 25 - 9;
            default: return 0;
        }
    }

    private String getLangMessage(HyriPlayersVisibilityLevel level) {
        return HyriLobby.getLanguageManager().getMessage(("settings.level." + level.name().toLowerCase())).getForPlayer(this.owner);
    }

    private ItemStack getItem(DyeColor color, HyriPlayersVisibilityLevel level) {
        return new ItemBuilder(new Wool(color).toItemStack(1)).withName(this.getLangMessage(level)).build();
    }
}
