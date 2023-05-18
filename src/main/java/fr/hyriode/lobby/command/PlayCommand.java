package fr.hyriode.lobby.command;

import fr.hyriode.hyrame.command.CommandContext;
import fr.hyriode.hyrame.command.CommandInfo;
import fr.hyriode.hyrame.command.CommandUsage;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.selector.game.GameSelectorGUI;
import fr.hyriode.lobby.player.LobbyPlayer;

public class PlayCommand extends HyriCommand<HyriLobby> {

    public PlayCommand(HyriLobby plugin) {
        super(plugin, new CommandInfo("play")
                .withDescription("Opens the main gui of the lobby (to join games)")
                .withAliases("games", "game")
                .withUsage(new CommandUsage().withStringMessage(player -> "/play")));
    }

    @Override
    public void handle(CommandContext ctx) {
        new GameSelectorGUI(this.plugin, ctx.getSender()).open();
    }

}
