package fr.hyriode.lobby.vip.casino.game;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.entity.Player;

public abstract class AGame {

    protected Player player;

    public AGame(Player player) {
        this.player = player;
    }

    public void init() {
        final IHyriPlayer hyriPlayer = IHyriPlayer.get(this.player.getUniqueId());
        final long hyris = hyriPlayer.getHyris().getAmount();

        if(this.getHyrisPrice() != 0L) {
            if(hyris >= this.getHyrisPrice()) {
                hyriPlayer.getHyris().remove(this.getHyrisPrice()).exec();
                hyriPlayer.update();
            } else {
                this.player.sendMessage(LobbyMessage.DO_NOT_HAVE_ENOUGH_HYRIS.asString(this.player));
                return;
            }
        }

        this.getInventory().open();
    }

    public void onWinning(long hyris) {
        final IHyriPlayer hyriPlayer = IHyriPlayer.get(this.player.getUniqueId());
        hyriPlayer.getHyris().add(hyris).withMultiplier(false).exec();
        hyriPlayer.update();

        this.player.sendMessage(LobbyMessage.WON.asString(this.player) + hyris + " Hyris");
    }

    protected abstract HyriInventory getInventory();
    public abstract String getName();
    protected abstract long getHyrisPrice();

    public Player getPlayer() {
        return player;
    }
}
