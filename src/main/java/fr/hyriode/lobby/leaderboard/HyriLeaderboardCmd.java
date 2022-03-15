package fr.hyriode.lobby.leaderboard;

import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.command.HyriCommandContext;
import fr.hyriode.hyrame.command.HyriCommandInfo;
import fr.hyriode.hyriapi.rank.EHyriRank;
import fr.hyriode.hyriapi.rank.HyriPermission;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.leaderboard.LobbyLeaderboard;
import fr.hyriode.lobby.api.leaderboard.LobbyLeaderboardManager;
import fr.hyriode.lobby.api.packet.LobbyPacketManager;
import fr.hyriode.lobby.api.packet.model.leaderboard.LeaderboardCreatedPacket;
import fr.hyriode.lobby.api.packet.model.leaderboard.LeaderboardDeletedPacket;
import fr.hyriode.lobby.api.packet.model.leaderboard.LeaderboardUpdatedPacket;
import fr.hyriode.lobby.api.utils.LobbyLocation;
import fr.hyriode.lobby.utils.LocationConverter;
import fr.hyriode.lobby.utils.RandomTools;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HyriLeaderboardCmd extends HyriCommand<HyriLobby> {

    private enum Permission implements HyriPermission {
        USE
    }

    private final LobbyPacketManager pm;
    private final LobbyLeaderboardManager lm;

    public HyriLeaderboardCmd(HyriLobby plugin) {
        super(plugin, new HyriCommandInfo("hyrileaderboard")
                .withDescription("Leaderboard management for the lobby")
                .withUsage("/hyrileaderboard create|delete|edit|list")
                .withPermission(Permission.USE));

        Permission.USE.add(EHyriRank.ADMINISTRATOR);

        this.pm = LobbyAPI.get().getPacketManager();
        this.lm = LobbyAPI.get().getLeaderboardManager();
    }

    @Override
    public void handle(HyriCommandContext ctx) {
        if (!(ctx.getSender() instanceof Player)) {
            this.sendMsg(ctx.getSender(), "You must be a player to use this command !", true);
            return;
        }

        final Player player = (Player) ctx.getSender();

        this.handleArgument(ctx, "create", output -> {
            if (this.checkArg(ctx.getArgs()[1]) || this.checkArg(ctx.getArgs()[2])) {
                this.sendMsg(player, "You must specify a name and a top range !", true);
                return;
            }

            final String name = ctx.getArgs()[1];
            this.lm.save(new LobbyLeaderboard(name, Integer.parseInt(ctx.getArgs()[2]), LocationConverter.toLobbyLocation(player.getLocation())));

            this.sendMsg(player, "Leaderboard created !", false);
            this.pm.sendPacket(new LeaderboardCreatedPacket(name));
        });

        this.handleArgument(ctx, "delete", output -> {
            if (this.checkArg(ctx.getArgs()[1])) {
                this.sendMsg(player, "You must specify a leaderboard name !", true);
                return;
            }

            final LobbyLeaderboard lb = this.lm.get(ctx.getArgs()[1]);

            if (lb == null) {
                this.sendMsg(player, "There is no leaderboard with this name !", true);
                return;
            }

            this.lm.delete(lb);

            this.sendMsg(player, "Leaderboard deleted !", false);
            this.pm.sendPacket(new LeaderboardDeletedPacket(lb.getName()));
        });

        this.handleArgument(ctx, "edit", output -> {
            if (this.checkArg(ctx.getArgs()[1])) {
                this.sendMsg(player, "You must specify a leaderboard name !", true);
                return;
            }

            final LobbyLeaderboard lb = this.lm.get(ctx.getArgs()[1]);

            if (lb == null) {
                this.sendMsg(player, "There is no leaderboard with this name !", true);
                return;
            }

            if (this.checkArg(ctx.getArgs()[2]) || this.checkArg(ctx.getArgs()[3])) {
                this.sendMsg(player, "==Edit leaderboard " + lb.getName() + "==", true);
                player.spigot().sendMessage(new ComponentBuilder("- ").color(ChatColor.WHITE)
                        .append(" [Set Top Range] ").color(ChatColor.GOLD).bold(true)
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/hyrileaderboard edit " + lb.getName() + " settop <new range>"))
                        .append(" [Set Location]").color(ChatColor.RED).bold(true)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyrileaderboard edit " + lb.getName() + " setloc"))
                        .create());
                return;
            }

            if (ctx.getArgs()[2].equalsIgnoreCase("settop")) {
                if (this.checkArg(ctx.getArgs()[3])) {
                    this.sendMsg(player, "You must specify a new top range !", true);
                }

                lb.setTopRange(Integer.parseInt(ctx.getArgs()[3]));
                this.lm.save(lb);

                this.sendMsg(player, "Top range set to " + lb.getTopRange() + " !", false);
                this.pm.sendPacket(new LeaderboardUpdatedPacket(lb.getName(), LeaderboardUpdatedPacket.Reason.SCORE_UPDATED));
                return;
            }

            if (ctx.getArgs()[2].equalsIgnoreCase("setloc")) {
                lb.setLocation(LocationConverter.toLobbyLocation(player.getLocation()));
                this.lm.save(lb);

                this.sendMsg(player, "Location set for the leaderboard " + lb.getName() + " !", false);
                this.pm.sendPacket(new LeaderboardUpdatedPacket(lb.getName(), LeaderboardUpdatedPacket.Reason.MOVED));
            }
        });

        this.handleArgument(ctx, "list", output -> {
            this.sendMsg(player, "==Leaderboards==", true);

            for (final LobbyLeaderboard lb : this.lm.getAllKeysAsValues()) {
                player.spigot().sendMessage(new ComponentBuilder("- ").color(ChatColor.WHITE)
                        .append("Name: " + lb.getName() + ", Top Range: " + lb.getTopRange() + ", Pos: " + LobbyLocation.toStringFormat(lb.getLocation()))
                        .append(" [Edit] ").color(ChatColor.GOLD).bold(true)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyrileaderboard edit " + lb.getName()))
                        .append(" [Delete]").color(ChatColor.DARK_RED).bold(true)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyrileaderboard delete " + lb.getName()))
                        .create());
            }
        });
    }

    private void sendMsg(CommandSender sender, String msg, boolean error) {
        sender.sendMessage(RandomTools.getPrefix(error) + msg);
    }

    private boolean checkArg(String arg) {
        return arg == null || arg.isEmpty();
    }
}
