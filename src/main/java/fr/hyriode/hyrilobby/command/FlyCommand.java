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
        final String result = LobbyMessage.FLY_COMMAND_RESULT.getCommand().getForPlayer(player);

        if (!lobbyPlayer.isInJump() && !lobbyPlayer.isInPvp()) {
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.sendMessage(result.replace("%value%", HyriLobby.getLanguageManager().getValue(player, "command.fly.value.off")));
            } else {
                player.setAllowFlight(true);
                player.sendMessage(result.replace("%value%", HyriLobby.getLanguageManager().getValue(player, "command.fly.value.on")));
            }
        }
    }
}
