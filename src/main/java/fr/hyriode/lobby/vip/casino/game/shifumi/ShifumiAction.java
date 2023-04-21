package fr.hyriode.lobby.vip.casino.game.shifumi;

import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.vip.casino.game.zzs.ZeroZeroSevenGameInfos;

public enum ShifumiAction {

    ROCK(LobbyMessage.CASINO_SHIFUMI_PLAYER_ROCK, LobbyMessage.CASINO_SHIFUMI_BOT_ROCK),
    PAPER(LobbyMessage.CASINO_SHIFUMI_PLAYER_PAPER, LobbyMessage.CASINO_SHIFUMI_BOT_PAPER),
    SCISSORS(LobbyMessage.CASINO_SHIFUMI_PLAYER_SCISSORS, LobbyMessage.CASINO_SHIFUMI_BOT_SCISSORS);
    private final LobbyMessage playerMessage;
    private final LobbyMessage botMessage;
    ShifumiAction(LobbyMessage playerMessage, LobbyMessage botMessage) {
        this.playerMessage = playerMessage;
        this.botMessage = botMessage;
    }

    LobbyMessage getMessage(boolean isBot) {
        return isBot ? botMessage : playerMessage;
    }
}