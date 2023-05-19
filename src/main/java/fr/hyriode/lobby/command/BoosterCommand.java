package fr.hyriode.lobby.command;

import fr.hyriode.hyrame.command.CommandContext;
import fr.hyriode.hyrame.command.CommandInfo;
import fr.hyriode.hyrame.command.CommandUsage;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.ui.gui.profile.booster.BoostersGUI;

public class BoosterCommand extends HyriCommand<HyriLobby> {

    public BoosterCommand(HyriLobby plugin) {
        super(plugin, new CommandInfo("booster")
                .withAliases("boosters", "boost", "boosts")
                .withDescription("Display the boosters of a player")
                .withUsage(new CommandUsage().withStringMessage(player -> "/booster")));
    }

    @Override
    public void handle(CommandContext ctx) {
        new BoostersGUI(this.plugin, ctx.getSender()).open();
    }

}
