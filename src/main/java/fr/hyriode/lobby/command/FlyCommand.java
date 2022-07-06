package fr.hyriode.lobby.command;

import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.command.HyriCommandContext;
import fr.hyriode.hyrame.command.HyriCommandInfo;
import fr.hyriode.hyrame.command.HyriCommandType;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.player.LobbyPlayer;
import org.bukkit.entity.Player;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 26/04/2022 at 17:28
 */
public class FlyCommand extends HyriCommand<HyriLobby> {

    public FlyCommand(HyriLobby plugin) {
        super(plugin, new HyriCommandInfo("fly")
                .withDescription("Permits to fly in the lobby")
                .withType(HyriCommandType.PLAYER)
                .withPermission(iHyriPlayer -> iHyriPlayer.getRank().isStaff() || iHyriPlayer.getRank().isSuperior(HyriPlayerRankType.VIP_PLUS))
                .withUsage("/fly"));
    }

    @Override
    public void handle(HyriCommandContext ctx) {
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(((Player) ctx.getSender()).getUniqueId());
        final Player player = lobbyPlayer.asPlayer();
        final String result = LobbyMessage.FLY_COMMAND_RESULT.asLang().getForPlayer(player);

        if (lobbyPlayer.isInPvp() || lobbyPlayer.hasJump()) {
            player.sendMessage(LobbyMessage.FLY_COMMAND_ERROR.asLang().getForPlayer(player));
            return;
        }

        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.sendMessage(result.replace("%value%", LobbyMessage.FLY_COMMAND_OFF.asLang().getForPlayer(player)));
        } else {
            player.setAllowFlight(true);
            player.sendMessage(result.replace("%value%", LobbyMessage.FLY_COMMAND_ON.asLang().getForPlayer(player)));
        }
    }
}
