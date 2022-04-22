package fr.hyriode.lobby.commands;

import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.command.HyriCommandContext;
import fr.hyriode.hyrame.command.HyriCommandInfo;
import fr.hyriode.hyrame.command.HyriCommandType;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.listeners.PlayerHandler;
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
        this.plugin.getHyrame().getListenerManager().getListener(PlayerHandler.class).getPlayerManager().teleportToSpawn((Player) ctx.getSender());
    }
}
