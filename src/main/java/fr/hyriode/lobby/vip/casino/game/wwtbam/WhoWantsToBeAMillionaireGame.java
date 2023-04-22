package fr.hyriode.lobby.vip.casino.game.wwtbam;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.vip.casino.game.AGame;
import fr.hyriode.lobby.vip.casino.game.inventory.GameInventoryBuilder;
import org.bukkit.entity.Player;

import java.util.Random;

public class WhoWantsToBeAMillionaireGame extends AGame {

    private long hyris;
    private int playingTime = 0;

    public WhoWantsToBeAMillionaireGame(Player player) {
        super(player);
    }

    @Override
    public HyriInventory getInventory() {
        return new GameInventoryBuilder()
                .setGame(this)
                .setMinimum(0)
                .setMaximum(300)
                .setModifiers(new int[] {1, 10, 100})
                .setButtonAction((hyris) -> {
                    if(hyris == 0) return;
                    this.play(hyris);
                })
                .build();
    }

    @Override
    public String getName() {
        return LobbyMessage.WWTBAM_NAME.asString(this.player);
    }

    @Override
    public long getHyrisPrice() {
        return 0;
    }

    public boolean result() {
        this.playingTime++;
        return 3D/(this.playingTime + 5D) >= new Random().nextDouble();
    }

    protected void play(long hyris) {
        final IHyriPlayer hyriPlayer = IHyriPlayer.get(this.player.getUniqueId());
        if(hyris > hyriPlayer.getHyris().getAmount()) {
            this.player.sendMessage(LobbyMessage.DO_NOT_HAVE_ENOUGH_HYRIS.asString(this.player));
            return;
        }


        if(this.playingTime == 0) {
            this.hyris = hyris;
            hyriPlayer.getHyris().remove(hyris).exec();
            hyriPlayer.update();
        }

        if(this.result()) {
            new ContinueInventory(this, (long) (hyris * 1.5)).open();
        } else {
            this.player.sendMessage(LobbyMessage.CASINO_LOST.asString(this.player) + "§e" + this.hyris + " Hyris" + "§6.");
            this.player.getOpenInventory().close();
        }
    }
}
