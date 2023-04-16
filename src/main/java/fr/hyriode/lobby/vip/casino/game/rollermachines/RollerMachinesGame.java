package fr.hyriode.lobby.vip.casino.game.rollermachines;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.vip.casino.game.AGame;
import org.bukkit.entity.Player;

public class RollerMachinesGame extends AGame {


    public RollerMachinesGame(Player player) {
        super(player);
    }

    @Override
    public HyriInventory getInventory() {
        return new RollingMachinesInventory(this.player, this);
    }

    @Override
    public String getName() {
        return LobbyMessage.ROLLER_MACHINES_NAME.asString(this.player);
    }

    @Override
    public long getHyrisPrice() {
        return 500L;
    }

    @Override
    public void onWinning(long hyris) {
        super.onWinning(hyris);
        this.player.closeInventory();
    }
}
