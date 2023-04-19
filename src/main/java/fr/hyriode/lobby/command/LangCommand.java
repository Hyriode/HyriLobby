package fr.hyriode.lobby.command;

import fr.hyriode.hyrame.command.CommandContext;
import fr.hyriode.hyrame.command.CommandInfo;
import fr.hyriode.hyrame.command.CommandUsage;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.settings.LanguageGUI;

public class LangCommand extends HyriCommand<HyriLobby> {

    public LangCommand(HyriLobby plugin) {
        super(plugin, new CommandInfo("lang")
                .withAliases("language")
                .withDescription("Open the language selector GUI")
                .withUsage(new CommandUsage().withStringMessage(player -> "/lang")));
    }

    @Override
    public void handle(CommandContext ctx) {
        new LanguageGUI(this.plugin, ctx.getSender()).open();
    }

}
