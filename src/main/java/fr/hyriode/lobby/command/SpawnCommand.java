package fr.hyriode.lobby.command;

import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.command.HyriCommandContext;
import fr.hyriode.hyrame.command.HyriCommandInfo;
import fr.hyriode.hyrame.command.HyriCommandType;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.player.LobbyPlayer;
import org.bukkit.entity.Player;

public class SpawnCommand extends HyriCommand<HyriLobby> {

    public SpawnCommand(HyriLobby plugin) {
        super(plugin, new HyriCommandInfo("spawn")
                        .withDescription("Teleport the player to the spawn of the lobby")
                        .withType(HyriCommandType.PLAYER)
                        .withUsage("/spawn"));
    }

    @Override
    public void handle(HyriCommandContext ctx) {
        final LobbyPlayer lp = this.plugin.getPlayerManager().getLobbyPlayer(((Player) ctx.getSender()).getUniqueId());

        if (lp.hasJump()) {
            lp.leaveJump(false);
            return;
        }

        lp.handleLogin(false, true);
    }

}
