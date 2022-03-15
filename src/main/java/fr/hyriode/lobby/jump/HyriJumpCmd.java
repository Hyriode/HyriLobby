package fr.hyriode.lobby.jump;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.command.HyriCommandContext;
import fr.hyriode.hyrame.command.HyriCommandInfo;
import fr.hyriode.hyriapi.rank.EHyriRank;
import fr.hyriode.hyriapi.rank.HyriPermission;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.jump.LobbyCheckpoint;
import fr.hyriode.lobby.api.jump.LobbyJump;
import fr.hyriode.lobby.api.jump.LobbyJumpManager;
import fr.hyriode.lobby.api.leaderboard.LobbyLeaderboard;
import fr.hyriode.lobby.api.leaderboard.LobbyLeaderboardManager;
import fr.hyriode.lobby.api.packet.LobbyPacketManager;
import fr.hyriode.lobby.api.packet.model.jump.JumpCreatedPacket;
import fr.hyriode.lobby.api.packet.model.jump.JumpUpdatedPacket;
import fr.hyriode.lobby.api.packet.model.leaderboard.LeaderboardCreatedPacket;
import fr.hyriode.lobby.api.utils.LobbyLocation;
import fr.hyriode.lobby.utils.LocationConverter;
import fr.hyriode.lobby.utils.RandomTools;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HyriJumpCmd extends HyriCommand<HyriLobby> {

    private enum Permission implements HyriPermission {
        USE
    }

    private boolean creating;

    private String name;
    private LobbyLocation start;
    private LobbyLocation end;

    private int checkpointId;
    private Map<Integer, LobbyLocation> checkpoints;

    private final LobbyJumpManager jm;
    private final LobbyPacketManager pm;
    private final LobbyLeaderboardManager lm;

    public HyriJumpCmd(HyriLobby plugin) {
        super(plugin, new HyriCommandInfo("hyrijump")
                .withDescription("Jump management for the lobby")
                .withUsage("/hyrijump create|checkpoint|delete|edit|list")
                .withPermission(Permission.USE));

        Permission.USE.add(EHyriRank.ADMINISTRATOR);

        this.resetJumpValues();

        this.jm = LobbyAPI.get().getJumpManager();
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
            if (this.creating) {
                LobbyLocation end = LocationConverter.toLobbyLocation(player.getLocation());

                if (LobbyLocation.isEquals(this.start, end)) {
                    this.sendMsg(player, "You can't create a jump with the same start and end location !", true);
                    return;
                }

                this.end = end;

                final List<LobbyCheckpoint> checkpoints = new ArrayList<>();
                checkpoints.add(new LobbyCheckpoint(0, this.start));

                for (Map.Entry<Integer, LobbyLocation> entry : this.checkpoints.entrySet()) {
                    checkpoints.add(new LobbyCheckpoint(entry.getKey(), entry.getValue()));
                }

                this.jm.save(this.jm.createJump(this.name, this.start, this.end, checkpoints));
                this.lm.save(new LobbyLeaderboard(this.name, 10, end));
                this.resetJumpValues();

                this.sendMsg(player, "Jump created !", false);
                this.pm.sendPacket(new JumpCreatedPacket(this.name));
                this.pm.sendPacket(new LeaderboardCreatedPacket(this.name));

                return;
            }

            if (this.checkArg(ctx.getArgs()[1])) {
                this.sendMsg(player, "You must specify a name for the jump !", true);
            }

            this.name = ctx.getArgs()[1];
            this.start = LocationConverter.toLobbyLocation(player.getLocation());

            this.sendMsg(player, "Creating a new jump with the name" + this.name + "... Go wherever you want and type " +
                    "\"/hyrijump checkpoint add\" to add some checkpoints or go to the end of the jump and repeat the command " +
                    "to finish creation !", false);
            this.creating = true;
        });

        this.handleArgument(ctx, "checkpoint add", output -> {
            if (!this.creating) {
                this.sendMsg(player, "You can't add checkpoints while you're not creating a jump !", true);
                return;
            }

            LobbyLocation location = LocationConverter.toLobbyLocation(player.getLocation());

            if (this.checkpoints.containsValue(location)) {
                this.sendMsg(player, "There is already an existing checkpoint with this location !", true);
                return;
            }

            this.checkpoints.put(this.checkpointId += 1, location);
            this.sendMsg(player, "Checkpoint added for the jump " + this.name + " !", false);
        });

        this.handleArgument(ctx, "checkpoint remove", output -> {
            if (!this.creating) {
                this.sendMsg(player, "You can't remove checkpoints while you're not creating a jump !", true);
                return;
            }

            final LobbyLocation checkpoint = this.checkArg(ctx.getArgs()[2]) ? LocationConverter.toLobbyLocation(player.getLocation())
                    : this.checkpoints.get(Integer.parseInt(ctx.getArgs()[2]));

            if (!this.checkpoints.containsValue(checkpoint)) {
                this.sendMsg(player, "There is no checkpoint with this location ! Try to give a Checkpoint ID !", true);
                return;
            }

            this.checkpoints.values().remove(checkpoint);
            this.sendMsg(player, "Checkpoint removed for the jump " + this.name + " !", false);
        });

        this.handleArgument(ctx, "checkpoint list", output -> {
            this.sendMsg(player, "==Checkpoints for the jump " + this.name + "==", false);
            for (Map.Entry<Integer, LobbyLocation> entry : this.checkpoints.entrySet()) {
                player.spigot().sendMessage(new ComponentBuilder("- ").color(ChatColor.WHITE)
                        .append("ID: " + entry.getKey() + ", Value: ")
                        .append(LobbyLocation.toStringFormat(entry.getValue()))
                        .append(" [Remove]").color(ChatColor.DARK_RED).bold(true)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyrijump checkpoint remove " + entry.getKey()))
                        .create());
            }
        });

        this.handleArgument(ctx, "delete", output -> {
            if (this.checkArg(ctx.getArgs()[1])) {
                this.sendMsg(player, "You must specify a jump name !", true);
                return;
            }

            final LobbyJump jump = this.jm.get(ctx.getArgs()[1]);

            if (jump == null) {
                this.sendMsg(player, "There is no jump with this name !", true);
                return;
            }

            this.jm.delete(jump);
            this.sendMsg(player, "Jump deleted !", false);
        });

        this.handleArgument(ctx, "edit", output -> {
            if (this.checkArg(ctx.getArgs()[1])) {
                this.sendMsg(player, "You must specify a jump name !", true);
                return;
            }

            final LobbyJump jump = this.jm.get(ctx.getArgs()[1]);

            if (jump == null) {
                this.sendMsg(player, "There is no jump with this name !", true);
                return;
            }

            if (this.checkArg(ctx.getArgs()[2]) || this.checkArg(ctx.getArgs()[3])) {
                this.sendMsg(player, "==Edit jump " + jump.getName() + "==", false);
                player.spigot().sendMessage(new ComponentBuilder("- ").color(ChatColor.WHITE)
                        .append("[Set Start] ").color(ChatColor.RED).bold(true)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyrijump edit " + jump.getName() + " set start"))
                        .append(" [Rename] ").color(ChatColor.GREEN).bold(true)
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/hyrijump edit " + jump.getName() + " rename <new name>"))
                        .append(" [Set Leaderboard Top] ").color(ChatColor.GOLD).bold(true)
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/hyrileaderboard edit " + jump.getName() + " settop <top>"))
                        .append(" [Set Leaderboard Pos] ").color(ChatColor.GOLD).bold(true)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyrileaderboard edit " + jump.getName() + " setloc"))
                        .append(" [Set End] ").color(ChatColor.RED).bold(true)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyrijump edit " + jump.getName() + " set end"))
                        .create());
                return;
            }

            final LobbyLocation loc = LocationConverter.toLobbyLocation(player.getLocation());

            if (ctx.getArgs()[2].equalsIgnoreCase("set")) {
                if (ctx.getArgs()[3].equalsIgnoreCase("start")) {
                    jump.setStart(loc);
                    this.jm.save(jump);

                    this.sendMsg(player, "Start set for the jump " + jump.getName() + " !", false);
                    this.pm.sendPacket(new JumpUpdatedPacket(jump.getName(), JumpUpdatedPacket.Reason.START_MOVED));

                    return;
                }

                if (ctx.getArgs()[3].equalsIgnoreCase("end")) {
                    jump.setEnd(loc);
                    this.jm.save(jump);

                    this.sendMsg(player, "End set for the jump " + jump.getName() + " !", false);
                    this.pm.sendPacket(new JumpUpdatedPacket(jump.getName(), JumpUpdatedPacket.Reason.END_MOVED));

                    return;
                }
            }

            if (ctx.getArgs()[2].equalsIgnoreCase("rename")) {
                HyriAPI.get().getRedisProcessor().process(jedis -> jedis.rename(jump.getName(), ctx.getArgs()[3]));

                this.sendMsg(player, "Jump renamed !", false);
                this.pm.sendPacket(new JumpUpdatedPacket(ctx.getArgs()[3], JumpUpdatedPacket.Reason.NAME_CHANGED));
            }
        });

        this.handleArgument(ctx, "list", output -> {
           this.sendMsg(player, "==Jumps==", false);

           for (LobbyJump jump : this.jm.getAllKeysAsValues()) {
               player.spigot().sendMessage(new ComponentBuilder("- ").color(ChatColor.WHITE)
                       .append("Name: " + jump.getName() + ", Start: " + LobbyLocation.toStringFormat(jump.getStart()) + ", End: "
                       + LobbyLocation.toStringFormat(jump.getEnd()) + ", Checkpoints: " + jump.getCheckpoints().size())
                       .append(" [Edit] ").color(ChatColor.GOLD).bold(true)
                       .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyrijump edit " + jump.getName()))
                       .append(" [Delete]").color(ChatColor.DARK_RED).bold(true)
                       .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyrijump delete " + jump.getName()))
                       .create());
           }
        });
    }

    private void sendMsg(CommandSender sender, String msg, boolean error) {
        sender.sendMessage(RandomTools.getPrefix(error) + msg);
    }

    private void resetJumpValues() {
        this.creating = false;
        this.name = null;
        this.start = null;
        this.end = null;
        this.checkpointId = 0;
        this.checkpoints = new HashMap<>();
    }

    private boolean checkArg(String arg) {
        return arg == null || arg.isEmpty();
    }
}
