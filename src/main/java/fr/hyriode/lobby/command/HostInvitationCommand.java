package fr.hyriode.lobby.command;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.command.HyriCommandContext;
import fr.hyriode.hyrame.command.HyriCommandInfo;
import fr.hyriode.hyrame.command.HyriCommandType;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.entity.Player;

public class HostInvitationCommand extends HyriCommand<HyriLobby> {

    public HostInvitationCommand(HyriLobby plugin) {
        super(plugin, new HyriCommandInfo("hostinvitation")
                .withType(HyriCommandType.PLAYER)
                .withUsage("", false)
                .withUsage("/hostinvitation <server>"));
    }

    @Override
    public void handle(HyriCommandContext ctx) {
        final Player player = (Player) ctx.getSender();

        this.handleArgument(ctx, "%input%", output -> {
            final String serverName = output.get(String.class);

            System.out.println(serverName);

            if (HyriAPI.get().getServerManager().getServer(serverName) == null) {
                return;
            }

            HyriAPI.get().getQueueManager().addPlayerInQueue(player.getUniqueId(), serverName, true);
        });
    }

}
