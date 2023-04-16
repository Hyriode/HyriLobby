package fr.hyriode.lobby.vip.casino.game.zzs;

import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ZeroZeroSevenGameInfos {

    private final ZeroZeroSevenGame game;
    private final Player player;
    private final PlayerGame playerGame;
    private final PlayerGame botGame;

    protected ZeroZeroSevenGameInfos(ZeroZeroSevenGame game) {
        this.game = game;
        this.player = this.game.getPlayer();
        this.playerGame = new PlayerGame(false);
        this.botGame = new PlayerGame(true);
    }

    public PlayerGame getPlayerGame() {
        return playerGame;
    }

    public PlayerGame getBotGame() {
        return botGame;
    }

    public class PlayerGame {

        private final boolean bot;
        private int bullets = 0;
        private PlayerGame(boolean bot) {
            this.bot = bot;
        }

        protected E007Action shoot() {
            if (this.bullets == 0) {
                if(this == ZeroZeroSevenGameInfos.this.playerGame) {
                    ZeroZeroSevenGameInfos.this.player.sendMessage(LobbyMessage.CASINO_RELOAD_BEFORE_SHOOTING.asString(ZeroZeroSevenGameInfos.this.player));
                }
                return null;
            } else {
                this.bullets--;
                this.sendMessage(E007Action.SHOOT);
            }

            return E007Action.SHOOT;
        }

        protected E007Action reload() {
            this.bullets++;
            this.sendMessage(E007Action.RELOAD);

            return E007Action.RELOAD;
        }

        protected E007Action protect() {
            this.sendMessage(E007Action.PROTECT);
            return E007Action.PROTECT;
        }

        private void sendMessage(E007Action action) {
            ZeroZeroSevenGameInfos.this.player.sendMessage(action.getMessage(this).asString(ZeroZeroSevenGameInfos.this.player));
        }

        public boolean isBot() {
            return this.bot;
        }
    }
}
