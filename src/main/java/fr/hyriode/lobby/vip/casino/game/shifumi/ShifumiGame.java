package fr.hyriode.lobby.vip.casino.game.shifumi;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.vip.casino.game.AGame;
import fr.hyriode.lobby.vip.casino.game.inventory.GameInventoryBuilder;
import fr.hyriode.lobby.vip.casino.game.zzs.E007Action;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class ShifumiGame extends AGame {

    private long hyris;
    public ShifumiGame(Player player) {
        super(player);
    }

    @Override
    protected HyriInventory getInventory() {
        return new GameInventoryBuilder()
                .setMinimum(0)
                .setMaximum(3000)
                .setModifiers(new int[]{10, 100, 500})
                .setGame(this)
                .setButtonAction(hyris -> {
                    if(IHyriPlayer.get(this.player.getUniqueId()).getHyris().getAmount() < hyris) {
                        this.player.sendMessage(LobbyMessage.DO_NOT_HAVE_ENOUGH_HYRIS.asString(this.player));
                        return;
                    }
                    this.start(hyris);
                })
                .build();
    }

    private void start(long hyris) {
        this.hyris = hyris;
        new ShifumiGameInventory(this).open();
    }

    protected void play(ShifumiAction action) {
        action.getMessage(false).sendTo(this.player);

        final ShifumiAction botAction = ShifumiAction.values()[new Random().nextInt(ShifumiAction.values().length)];
        botAction.getMessage(true).sendTo(this.player);

        if(action == botAction) {
            LobbyMessage.CASINO_SHIFUMI_EQUALITY.sendTo(this.player);
            return;
        }

        if((action == ShifumiAction.ROCK && botAction == ShifumiAction.PAPER) || (action == ShifumiAction.PAPER && botAction == ShifumiAction.SCISSORS) || (action == ShifumiAction.SCISSORS && botAction == ShifumiAction.ROCK)) {
            this.loose(this.hyris);
            return;
        }

        this.onWinning(this.hyris);
    }

    @Override
    public void onWinning(long hyris) {
        super.onWinning(hyris);
        this.player.closeInventory();
    }

    @Override
    public void loose(long hyris) {
        super.loose(hyris);
        this.player.closeInventory();
    }

    @Override
    public String getName() {
        return "Shifumi";
    }

    @Override
    protected long getHyrisPrice() {
        return 0;
    }
}
