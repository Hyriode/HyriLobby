package fr.hyriode.lobby.command;

import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.command.HyriCommandContext;
import fr.hyriode.hyrame.command.HyriCommandInfo;
import fr.hyriode.hyrame.command.HyriCommandType;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.settings.LanguageGUI;
import org.bukkit.entity.Player;

public class LangCommand extends HyriCommand<HyriLobby> {

    public LangCommand(HyriLobby plugin) {
        super(plugin, new HyriCommandInfo("lang")
                .withAliases("language")
                .withDescription("Open the language selector GUI")
                .withType(HyriCommandType.PLAYER)
                .withUsage("/lang"));
    }

    @Override
    public void handle(HyriCommandContext ctx) {
        new LanguageGUI(this.plugin, (Player) ctx.getSender()).open();
    }

}
