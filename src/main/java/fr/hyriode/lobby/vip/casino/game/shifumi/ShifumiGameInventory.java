package fr.hyriode.lobby.vip.casino.game.shifumi;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ShifumiGameInventory extends HyriInventory {
    private final ShifumiGame game;
    public ShifumiGameInventory(ShifumiGame game) {
        super(game.getPlayer(), game.getName(), 54);
        this.game = game;
        this.initItems();
    }

    private void initItems() {
        this.setItem(20, new ItemBuilder(new ItemStack(Material.COBBLESTONE, 1)).withName(LobbyMessage.CASINO_SHIFUMI_ROCK.asString(this.getOwner())).build(), event -> this.game.play(ShifumiAction.ROCK));
        this.setItem(22, new ItemBuilder(new ItemStack(Material.PAPER, 1)).withName(LobbyMessage.CASINO_SHIFUMI_PAPER.asString(this.getOwner())).build(), event -> this.game.play(ShifumiAction.PAPER));
        this.setItem(24, new ItemBuilder(new ItemStack(Material.SHEARS, 1)).withName(LobbyMessage.CASINO_SHIFUMI_SCISSORS.asString(this.getOwner())).build(), event -> this.game.play(ShifumiAction.SCISSORS));
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if(!this.game.isEnd()) {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(HyriLobby.class), this::open, 1L);
        }
    }
}
