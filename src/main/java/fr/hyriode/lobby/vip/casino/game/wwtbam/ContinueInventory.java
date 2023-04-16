package fr.hyriode.lobby.vip.casino.game.wwtbam;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ContinueInventory extends HyriInventory {

    private final long hyris;
    private boolean closeRequested = false;

    private final WhoWantsToBeAMillionaireGame game;

    public ContinueInventory( WhoWantsToBeAMillionaireGame game, long hyris) {
        super(game.getPlayer(), LobbyMessage.CASINO_CONTINUE.asString(game.getPlayer()) + hyris + " Hyris", 54);

        this.hyris = hyris;
        this.game = game;

        this.setItem(21, (new ItemBuilder(Material.STAINED_CLAY, 1, (short) 9).withName(LobbyMessage.CASINO_PUT_THEM_BACK.asString(this.getOwner()))).build(), (event) -> {
            this.close();
            game.play((long) (hyris * 1.5));
        });
        this.setItem(23, (new ItemBuilder(Material.STAINED_CLAY, 1, (short) 5).withName(LobbyMessage.CASINO_RECOVER_BET.asString(this.getOwner()))).build(), (event) -> {
            this.close();
            this.game.onWinning(hyris);
        });
    }

    private void close() {
        this.closeRequested = true;
        this.owner.getOpenInventory().close();
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        if(!this.closeRequested) {
            this.game.onWinning(hyris);
        }
    }
}
