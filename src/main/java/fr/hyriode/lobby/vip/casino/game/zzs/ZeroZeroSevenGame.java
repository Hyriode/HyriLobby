package fr.hyriode.lobby.vip.casino.game.zzs;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.vip.casino.game.AGame;
import fr.hyriode.lobby.vip.casino.game.inventory.GameInventoryBuilder;
import org.bukkit.entity.Player;

import java.util.Random;

public class ZeroZeroSevenGame extends AGame {

    protected boolean isEnd = false;
    private ZeroZeroSevenGameInfos gameInfos;
    private long hyris;
    private int playingTime = 0;

    public ZeroZeroSevenGame(Player player) {
        super(player);
    }

    @Override
    public HyriInventory getInventory() {
        return new GameInventoryBuilder()
                .setMinimum(0)
                .setMaximum(5000)
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

    @Override
    public String getName() {
        return "007";
    }

    @Override
    public long getHyrisPrice() {
        return 0;
    }

    private void start(long hyris) {
        this.hyris = hyris;
        this.gameInfos = new ZeroZeroSevenGameInfos(this);
        new ZeroZeroSevenGameInventory(this).open();
    }

    protected void play(E007Action playerAction) {
        if(playerAction == null) {
            return;
        }

        final E007Action botAction = this.getBotAction();

        if(botAction == null) {
            this.play(playerAction);
            return;
        }

        if(playerAction == E007Action.SHOOT && botAction == E007Action.SHOOT) {
            this.draw();
        } else if(playerAction == E007Action.SHOOT && botAction == E007Action.RELOAD) {
            this.onWinning((long) (this.hyris*1.7));
        } else if (playerAction == E007Action.RELOAD && botAction == E007Action.SHOOT) {
            this.loose();
        }
        this.playingTime++;
    }

    private E007Action getBotAction() {
        if(this.playingTime == 0) {
            return this.gameInfos.getBotGame().reload();
        }

        final E007Action botAction = E007Action.values()[new Random().nextInt(E007Action.values().length)];
        switch (botAction) {
            case PROTECT:
                return this.gameInfos.getBotGame().protect();
            case SHOOT:
                return this.gameInfos.getBotGame().shoot();
            case RELOAD:
                return this.gameInfos.getBotGame().reload();
            default:
                break;
        }
        return null;
    }


    @Override
    public void onWinning(long hyris) {
        super.onWinning(hyris);
        this.end();
    }

    protected void draw() {
        this.player.sendMessage(LobbyMessage.DRAW.asString(this.player));
        this.end();
    }

    protected void loose() {
        final IHyriPlayer hyriPlayer = IHyriPlayer.get(this.player.getUniqueId());
        hyriPlayer.getHyris().remove(this.hyris).withMultiplier(false).exec();
        hyriPlayer.update();

        this.player.sendMessage( LobbyMessage.LOST.asString(this.player) + hyris + " Hyris");
        this.end();
    }

    private void end() {
        this.isEnd = true;
        this.player.getOpenInventory().close();
    }

    protected ZeroZeroSevenGameInfos getGameInfos() {
        return this.gameInfos;
    }
}
