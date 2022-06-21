package fr.hyriode.lobby.gui;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.util.InventoryUtil;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class LobbyInventory extends HyriInventory {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final Function<UsefulHead, ItemBuilder> HEAD_ITEM = texture -> ItemBuilder.asHead().withHeadTexture(texture.getTexture());
    public static final Function<UUID, ItemBuilder> PLAYER_HEAD_ITEM = uuid -> ItemBuilder.asHead().withPlayerHead(uuid);

    protected final String name;
    protected final IHyrame hyrame;
    protected final HyriLobby plugin;
    protected final IHyriPlayer account;
    protected final String guiName;

    protected LobbyInventory previousGui;
    protected ItemStack currentButton;
    protected Function<Integer, String> currentButtonName;
    protected Consumer<InventoryClickEvent> onCurrentUpdate;

    public LobbyInventory(Player owner, HyriLobby plugin, String name, String guiName, int size) {
        super(owner, name(owner, "gui." + guiName + ".name"), size);

        this.name = name;
        this.plugin = plugin;
        this.hyrame = plugin.getHyrame();
        this.account = HyriAPI.get().getPlayerManager().getPlayer(owner.getUniqueId());
        this.guiName = guiName;
    }

    protected abstract void init();

    protected ItemStack createSwitch(boolean option) {
        Dye dye = new Dye(Material.INK_SACK);
        dye.setColor(option ? DyeColor.LIME : DyeColor.RED);
        return new ItemBuilder(dye.toItemStack(1)).withName(this.getSwitchName(option)).build();
    }

    protected ItemStack updateSwitch(boolean option, ItemStack item) {
        item.setDurability((short) (option ? 10 : 1));
        return new ItemBuilder(item).withName(this.getSwitchName(option)).build();
    }

    protected void setupCurrentButton(ItemStack item, int slot, Function<Integer, String> name, Consumer<InventoryClickEvent> onUpdate) {
        this.currentButton = item;
        this.currentButtonName = name;
        this.onCurrentUpdate = onUpdate;

        this.updateCurrentButton(slot, null);
    }

    protected void updateCurrentButton(int slot, InventoryClickEvent event) {
        if (this.currentButton != null) {
            this.inventory.remove(this.currentButton);
            this.setItem(slot, new ItemBuilder(this.currentButton).withName(this.currentButtonName.apply(slot)).build());
        }

        if (this.onCurrentUpdate != null) {
            this.onCurrentUpdate.accept(event);
        }
    }

    protected void fill() {
        final ItemStack item = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 9).withName("§8").withAllItemFlags().build();

        for (int barrierSlot : InventoryUtil.getBarrierSlots()) {
            this.setItem(barrierSlot, item);
        }
    }

    private String getSwitchName(boolean option) {
        if (option) {
            return HyriLanguageMessage.get("gui.item.switch-on.name").getForPlayer(this.owner);
        } else {
            return HyriLanguageMessage.get("gui.item.switch-off.name").getForPlayer(this.owner);
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        this.account.update();
    }
}
