package fr.hyriode.lobby.vip.casino.game.whowantstobeamillionaire;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ContinueInventory extends HyriInventory {

    private final long hyris;
    private boolean closeRequested = false;

    public ContinueInventory(Player owner, WhoWantsToBeAMillionaire game, long hyris) {
        super(owner, "Continuer ? Vous avez " + hyris + " Hyris", 54);

        this.hyris = hyris;

        this.setItem(21, (new ItemBuilder(Material.STAINED_CLAY, 1, (short) 9).withName("Les remettre en jeu")).build(), (event) -> {
            //TODO
        });
        this.setItem(23, (new ItemBuilder(Material.STAINED_CLAY, 1, (short) 5).withName(ChatColor.GREEN + "Recuperer la mise")).build(), (event) -> {
            this.closeRequested = true;
            this.owner.getOpenInventory().close();
            this.save();
        });
    }

    private void save() {
        final IHyriPlayer player = IHyriPlayer.get(this.owner.getUniqueId());
        player.getHyris().add(this.hyris).withMultiplier(false).exec();
        player.update();
        this.owner.sendMessage(ChatColor.GREEN + "Vous avez gagné " + this.hyris + " Hyris");
    }

    private void playAgain() {

    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if(!this.closeRequested) {
            this.save();
        }
    }
}
