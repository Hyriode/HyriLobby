package fr.hyriode.hyrilobby.command;

import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.command.HyriCommandContext;
import fr.hyriode.hyrame.command.HyriCommandInfo;
import fr.hyriode.hyrame.command.HyriCommandType;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.language.LobbyMessage;
import fr.hyriode.hyrilobby.player.LobbyPlayer;
import org.bukkit.entity.Player;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 26/04/2022 at 17:28
 */
public class FlyCommand extends HyriCommand<HyriLobby> {

    public FlyCommand(HyriLobby plugin) {
        super(plugin, new HyriCommandInfo("fly")
                .withDescription("Teleport the player to the spawn of the lobby")
                .withType(HyriCommandType.PLAYER)
                .withPermission(iHyriPlayer -> iHyriPlayer.getRank().isStaff() || iHyriPlayer.getRank().isSuperior(HyriPlayerRankType.VIP_PLUS))
                .withUsage("/fly"));
    }

    @Override
    public void handle(HyriCommandContext ctx) {
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(((Player) ctx.getSender()).getUniqueId());
        final Player player = lobbyPlayer.asPlayer();
        final String result = LobbyMessage.FLY_COMMAND_RESULT.get().getForPlayer(player);

        if ((!lobbyPlayer.isInPvp()) || (!lobbyPlayer.hasJump())) {
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.sendMessage(result.replace("%value%", LobbyMessage.FLY_COMMAND_OFF.get().getForPlayer(player)));
            } else {
                player.setAllowFlight(true);
                player.sendMessage(result.replace("%value%", LobbyMessage.FLY_COMMAND_ON.get().getForPlayer(player)));
            }
        } else {
            player.sendMessage(LobbyMessage.FLY_COMMAND_ERROR.get().getForPlayer(player));
        }
    }
}
