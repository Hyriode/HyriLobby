package fr.hyriode.lobby.command;

import fr.hyriode.hyrame.command.CommandContext;
import fr.hyriode.hyrame.command.CommandInfo;
import fr.hyriode.hyrame.command.CommandUsage;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.ui.gui.profile.lootbox.LootboxesGUI;

public class LootboxCommand extends HyriCommand<HyriLobby> {

    public LootboxCommand(HyriLobby plugin) {
        super(plugin, new CommandInfo("lootbox")
                .withAliases("lootboxes", "loots", "loot")
                .withDescription("Display the lootboxes of a player")
                .withUsage(new CommandUsage().withStringMessage(player -> "/lootbox")));
    }

    @Override
    public void handle(CommandContext ctx) {
        new LootboxesGUI(this.plugin, ctx.getSender()).open();
    }

}
