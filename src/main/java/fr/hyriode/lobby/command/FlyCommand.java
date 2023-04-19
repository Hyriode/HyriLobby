package fr.hyriode.lobby.command;

import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.hyrame.command.CommandContext;
import fr.hyriode.hyrame.command.CommandInfo;
import fr.hyriode.hyrame.command.CommandUsage;
import fr.hyriode.hyrame.command.HyriCommand;
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
        super(plugin, new CommandInfo("fly")
                .withDescription("Permits to fly in the lobby")
                .withPermission(account -> account.getRank().isSuperior(PlayerRank.VIP_PLUS))
                .withUsage(new CommandUsage().withStringMessage(player -> "/fly")));
    }

    @Override
    public void handle(CommandContext ctx) {
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(((Player) ctx.getSender()).getUniqueId());
        final Player player = lobbyPlayer.asPlayer();
        final String result = LobbyMessage.FLY_COMMAND_RESULT.asLang().getValue(player);

        if (lobbyPlayer.isInPvp() || lobbyPlayer.hasJump()) {
            player.sendMessage(LobbyMessage.FLY_COMMAND_ERROR.asLang().getValue(player));
            return;
        }

        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.sendMessage(result.replace("%value%", LobbyMessage.FLY_COMMAND_OFF.asLang().getValue(player)));
        } else {
            player.setAllowFlight(true);
            player.sendMessage(result.replace("%value%", LobbyMessage.FLY_COMMAND_ON.asLang().getValue(player)));
        }
    }

}
