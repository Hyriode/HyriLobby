package fr.hyriode.lobby.command;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyrame.command.CommandContext;
import fr.hyriode.hyrame.command.CommandInfo;
import fr.hyriode.hyrame.command.CommandUsage;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.entity.Player;

public class HostInvitationCommand extends HyriCommand<HyriLobby> {

    public HostInvitationCommand(HyriLobby plugin) {
        super(plugin, new CommandInfo("hostinvitation")
                .withUsage(new CommandUsage()
                        .withErrorPrefix(false)
                        .withStringMessage(player -> "")));
    }

    @Override
    public void handle(CommandContext ctx) {
        final Player player = ctx.getSender();

        ctx.registerArgument("%input%", output -> {
            final String serverName = output.get(String.class);

            if (HyriAPI.get().getServerManager().getServer(serverName) == null) {
                return;
            }

            HyriAPI.get().getQueueManager().addPlayerInQueue(player.getUniqueId(), serverName);
        });
    }

}
