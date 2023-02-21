package fr.hyriode.lobby.vip.casino.game.whowantstobeamillionaire;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.lobby.vip.casino.game.IGame;
import org.bukkit.entity.Player;

import java.util.Random;

public class WhoWantsToBeAMillionaire implements IGame {

    private int playingTime = 0;

    @Override
    public HyriInventory getInventory(Player player) {
        return new InventoryGame(player, this);
    }

    @Override
    public String getName() {
        return "who-wants-to-be-a-millionaire"; //TODO
    }

    @Override
    public long getHyrisPrice() {
        return 0;
    }

    @Override
    public void onWinning() {

    }

    public boolean play() {
        this.playingTime++;
        System.out.println(this.playingTime);
        return 3D/(this.playingTime + 5D) >= new Random().nextDouble();
    }
}
