package fr.hyriode.lobby.command;

import fr.hyriode.hyrame.command.CommandContext;
import fr.hyriode.hyrame.command.CommandInfo;
import fr.hyriode.hyrame.command.CommandUsage;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.player.LobbyPlayer;

public class SpawnCommand extends HyriCommand<HyriLobby> {

    public SpawnCommand(HyriLobby plugin) {
        super(plugin, new CommandInfo("spawn")
                        .withDescription("Teleport the player to the spawn of the lobby")
                        .withUsage(new CommandUsage().withStringMessage(player -> "/spawn")));
    }

    @Override
    public void handle(CommandContext ctx) {
        final LobbyPlayer lp = this.plugin.getPlayerManager().getLobbyPlayer(ctx.getSender().getUniqueId());

        if (lp.hasJump()) {
            lp.leaveJump(false);
            return;
        }

        lp.handleLogin(false, true);
    }

}
