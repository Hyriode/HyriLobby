package fr.hyriode.lobby.vip.casino.game.zzs;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ZeroZeroSevenGameInventory extends HyriInventory {

    private final ZeroZeroSevenGame game;


    public ZeroZeroSevenGameInventory(ZeroZeroSevenGame game) {
        super(game.getPlayer(), "007", 54);

        this.game = game;

        this.initItems();
    }

    private void initItems() {
        this.setItem(20, new ItemBuilder(new ItemStack(Material.STAINED_CLAY, 1, (short) 5)).withName(LobbyMessage.CASINO_RELOAD.asString(this.getOwner())).build(), event -> this.game.play(this.game.getGameInfos().getPlayerGame().reload()));
        this.setItem(22, new ItemBuilder(new ItemStack(Material.STAINED_CLAY, 1, (short) 14)).withName(LobbyMessage.CASINO_SHOOT.asString(this.getOwner())).build(), event -> this.game.play(this.game.getGameInfos().getPlayerGame().shoot()));
        this.setItem(24, new ItemBuilder(new ItemStack(Material.STAINED_CLAY, 1, (short) 9)).withName(LobbyMessage.CASINO_PROTECT.asString(this.getOwner())).build(), event -> this.game.play(this.game.getGameInfos().getPlayerGame().protect()));
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if(!this.game.isEnd) {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(HyriLobby.class), this::open, 1L);
        }
    }
}
