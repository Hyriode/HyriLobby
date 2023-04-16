package fr.hyriode.lobby.vip.casino.game.zzs;

import fr.hyriode.lobby.language.LobbyMessage;

public enum E007Action {
    RELOAD(LobbyMessage.CASINO_PLAYER_RELOAD, LobbyMessage.CASINO_BOT_RELOAD),
    SHOOT(LobbyMessage.CASINO_PLAYER_SHOOT, LobbyMessage.CASINO_BOT_SHOOT),
    PROTECT(LobbyMessage.CASINO_PLAYER_PROTECT, LobbyMessage.CASINO_BOT_PROTECT);


    private final LobbyMessage playerMessage;
    private final LobbyMessage botMessage;
    E007Action(LobbyMessage playerMessage, LobbyMessage botMessage) {
        this.playerMessage = playerMessage;
        this.botMessage = botMessage;
    }

    public LobbyMessage getMessage(ZeroZeroSevenGameInfos.PlayerGame playerType) {
        return !playerType.isBot() ? this.playerMessage : this.botMessage;
    }
}
