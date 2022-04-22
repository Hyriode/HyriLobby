package fr.hyriode.lobby.commands;

import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.command.HyriCommandContext;
import fr.hyriode.hyrame.command.HyriCommandInfo;
import fr.hyriode.hyrame.command.HyriCommandType;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.entity.Player;

import java.util.Collections;

public class GameNpcCommand extends HyriCommand<HyriLobby> {

    public GameNpcCommand(HyriLobby plugin) {
        super(plugin, new HyriCommandInfo("gamenpc")
                .withDescription("Create a game npc")
                .withType(HyriCommandType.PLAYER)
                .withUsage("/gamenpc <game> <player to get the texture>")
                .withPermission(player -> player.getRank().isStaff()));
    }

    @Override
    public void handle(HyriCommandContext ctx) {
        this.handleArgument(ctx, "%input% %input%", output -> {
            //TODO Get game with the dynamic game registry
            final String game = output.get(0, String.class);
            final String skinOwner = output.get(1, String.class);

            final NPC npc = NPCManager.createNPC(((Player) ctx.getSender()).getLocation(), skinOwner, Collections.singletonList(game));
            npc.setShowingToAll(true);
            npc.setTrackingPlayer(true);
            //TODO Open the gui, created with the dynamic game registry values
            npc.setInteractCallback((isRightClick, player) -> System.out.println("Interact callback"));
        });
    }
}
