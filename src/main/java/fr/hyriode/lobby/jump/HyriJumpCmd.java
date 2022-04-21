package fr.hyriode.lobby.jump;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.rank.EHyriRank;
import fr.hyriode.api.rank.HyriPermission;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.command.HyriCommandContext;
import fr.hyriode.hyrame.command.HyriCommandInfo;
import fr.hyriode.hyrame.command.HyriCommandType;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.jump.LobbyCheckpoint;
import fr.hyriode.lobby.api.jump.LobbyJump;
import fr.hyriode.lobby.api.jump.LobbyJumpManager;
import fr.hyriode.lobby.api.leaderboard.LobbyLeaderboard;
import fr.hyriode.lobby.api.leaderboard.LobbyLeaderboardManager;
import fr.hyriode.lobby.api.packet.LobbyPacketManager;
import fr.hyriode.lobby.api.packet.model.jump.JumpCreatedPacket;
import fr.hyriode.lobby.api.packet.model.jump.JumpDeletedPacket;
import fr.hyriode.lobby.api.packet.model.jump.JumpUpdatedPacket;
import fr.hyriode.lobby.api.packet.model.leaderboard.LeaderboardCreatedPacket;
import fr.hyriode.lobby.api.player.LobbyPlayerManager;
import fr.hyriode.lobby.api.utils.LobbyLocation;
import fr.hyriode.lobby.utils.LocationConverter;
import fr.hyriode.lobby.utils.RandomTools;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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

    private final Supplier<LobbyJumpManager> jm;
    private final Supplier<LobbyPacketManager> pm;
    private final Supplier<LobbyPlayerManager> lpm;
    private final Supplier<LobbyLeaderboardManager> lm;

    public HyriJumpCmd(HyriLobby plugin) {
        super(plugin, new HyriCommandInfo("hyrijump")
                .withDescription("Jump management for the lobby")
                .withUsage("/hyrijump create, checkpoint (add, remove, list),  delete, edit, setstart, setend, list")
                .withType(HyriCommandType.PLAYER)
                .withPermission(Permission.USE));

        Permission.USE.add(EHyriRank.ADMINISTRATOR);

        this.resetJumpValues();

        this.jm = () -> LobbyAPI.get().getJumpManager();
        this.pm = () -> LobbyAPI.get().getPacketManager();
        this.lpm = () -> LobbyAPI.get().getPlayerManager();
        this.lm = () -> LobbyAPI.get().getLeaderboardManager();
    }

    @Override
    public void handle(HyriCommandContext ctx) {
        final Player player = (Player) ctx.getSender();

        this.handleArgument(ctx, "create", "/hyrijump create", output -> {
            if (!this.creating) {
                this.sendMsg(player, "You must start a jump creation ! Run \"/hyrijump create <name>\" before !", true);
                return;
            }

            final LobbyLocation end = LocationConverter.toLobbyLocation(player.getLocation());

            if (LobbyLocation.isEquals(this.start, end)) {
                this.sendMsg(player, "You can't create a jump with the same start and end location !", true);
                return;
            }

            this.end = end;

            final List<LobbyCheckpoint> checkpoints = new ArrayList<>();
            checkpoints.add(new LobbyCheckpoint(0, this.name, this.start));

            for (Map.Entry<Integer, LobbyLocation> entry : this.checkpoints.entrySet()) {
                checkpoints.add(new LobbyCheckpoint(entry.getKey(), this.name, entry.getValue()));
            }

            this.jm.get().save(new LobbyJump(this.name, this.start, this.end, checkpoints), this.name);
            this.lm.get().save(new LobbyLeaderboard(this.name, 10, end), this.name);

            this.sendMsg(player, "Jump created !", false);

            this.pm.get().sendPacket(new JumpCreatedPacket(this.name));
            this.pm.get().sendPacket(new LeaderboardCreatedPacket(this.name));

            this.resetJumpValues();
        });

        this.handleArgument(ctx, "create %input%", "/hyrijump create <name>", output -> {
            if (this.creating) {
                this.sendMsg(player, "You can't create a jump while you're creating another one !", true);
                return;
            }

            this.name = output.get(String.class);

            if (this.jm.get().get(this.name) != null) {
                this.sendMsg(player, "A jump with this name already exists !", true);
                return;
            }

            this.start = LocationConverter.toLobbyLocation(player.getLocation());

            this.sendMsg(player, "Creating a new jump with the name " + this.name + "... Go wherever you want and type " +
                    "\"/hyrijump checkpoint add\" to add some checkpoints or go to the end of the jump and run \"/hyrijump create\"" +
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

        this.handleArgument(ctx, "checkpoint remove %integer%", "/hyrijump checkpoint remove <id>", output -> {
            if (!this.creating) {
                this.sendMsg(player, "You can't remove checkpoints while you're not creating a jump !", true);
                return;
            }

            final LobbyLocation checkpoint = this.checkpoints.get(output.get(Integer.class));

            if (!this.checkpoints.containsValue(checkpoint)) {
                this.sendMsg(player, "There is no checkpoint with this ID !", true);
                return;
            }

            this.checkpoints.values().remove(checkpoint);
            this.sendMsg(player, "Checkpoint removed !", false);
        });

        this.handleArgument(ctx, "checkpoint list", output -> {
            if (!this.creating) {
                this.sendMsg(player, "You can't list checkpoints while you're not creating a jump !", true);
                return;
            }

            if (this.checkpoints.isEmpty()) {
                this.sendMsg(player, "There are no checkpoints for this jump !", true);
                return;
            }

            this.sendMsg(player, "==Checkpoints for the jump " + this.name + "==", false);
            for (Map.Entry<Integer, LobbyLocation> entry : this.checkpoints.entrySet()) {
                player.spigot().sendMessage(new ComponentBuilder("- ").color(ChatColor.WHITE)
                        .append("ID: " + entry.getKey() + ", Value: ")
                        .append(LobbyLocation.toStringFormat(entry.getValue()))
                        .append(" [Teleport]").color(ChatColor.AQUA).bold(true)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName() + " " + LobbyLocation.toStringFormat(entry.getValue())))
                        .append(" [Remove]").color(ChatColor.DARK_RED).bold(true)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyrijump checkpoint remove " + entry.getKey()))
                        .create());
            }
        });

        this.handleArgument(ctx, "delete %input%", "/hyrijump delete <name>", output -> {
            final LobbyJump jump = this.jm.get().get(output.get(String.class));

            if (jump == null) {
                this.sendMsg(player, "There is no jump with this name !", true);
                return;
            }

            Bukkit.getOnlinePlayers().forEach(p -> this.lpm.get().get(p.getUniqueId().toString()).getFinishedJumps().remove(jump.getName()));

            this.jm.get().delete(jump.getName());
            this.pm.get().sendPacket(new JumpDeletedPacket(jump.getName()));
            this.sendMsg(player, "Jump deleted !", false);
        });

        this.handleArgument(ctx, "edit %input%", "/hyrijump edit <name>", output -> {
            final LobbyJump jump = this.jm.get().get(output.get(String.class));

            if (jump == null) {
                this.sendMsg(player, "There is no jump with this name !", true);
                return;
            }

            this.sendMsg(player, "==Edit jump " + jump.getName() + "==", false);
            player.spigot().sendMessage(new ComponentBuilder("- ").color(ChatColor.WHITE)
                    .append("[TP to Start]").color(ChatColor.AQUA).bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName() + " " + LobbyLocation.toStringFormat(jump.getStart())))
                    .append(" [Set Start]").color(ChatColor.RED).bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyrijump setstart " + jump.getName()))
                    .append(" [Rename]").color(ChatColor.GREEN).bold(true)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/hyrijump rename " + jump.getName() + " <new name>"))
                    .append(" [Set Leaderboard Top]").color(ChatColor.GOLD).bold(true)
                    .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/hyrileaderboard settop " + jump.getName() + " <top>"))
                    .append(" [Set Leaderboard Pos]").color(ChatColor.GOLD).bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyrileaderboard setloc " + jump.getName()))
                    .append(" [Set End]").color(ChatColor.RED).bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hyrijump setend " + jump.getName()))
                    .append(" [TP to End]").color(ChatColor.AQUA).bold(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName() + " " + LobbyLocation.toStringFormat(jump.getEnd())))
                    .create());
        });

        this.handleArgument(ctx, "setstart %input%", "/hyrijump setstart <name>", output -> {
            final LobbyJump jump = this.jm.get().get(output.get(String.class));

            if (jump == null) {
                this.sendMsg(player, "There is no jump with this name !", true);
                return;
            }

            final LobbyLocation loc = LocationConverter.toLobbyLocation(player.getLocation());

            jump.getCheckpoints().set(0, new LobbyCheckpoint(0, jump.getName(), loc));
            jump.setStart(loc);
            this.jm.get().save(jump, jump.getName());

            this.sendMsg(player, "Start updated !", false);
            this.pm.get().sendPacket(new JumpUpdatedPacket(jump.getName(), JumpUpdatedPacket.Reason.START_MOVED));
        });

        this.handleArgument(ctx, "setend %input%", "/hyrijump setend <name>", output -> {
            final LobbyJump jump = this.jm.get().get(output.get(String.class));

            if (jump == null) {
                this.sendMsg(player, "There is no jump with this name !", true);
                return;
            }

            final LobbyLocation loc = LocationConverter.toLobbyLocation(player.getLocation());

            jump.setEnd(loc);
            this.jm.get().save(jump, jump.getName());

            this.sendMsg(player, "End set for the jump " + jump.getName() + " !", false);
            this.pm.get().sendPacket(new JumpUpdatedPacket(jump.getName(), JumpUpdatedPacket.Reason.END_MOVED));
        });

        this.handleArgument(ctx, "rename %input% %input%", "/hyrijump rename <name> <new name>", output -> {
            final LobbyJump jump = this.jm.get().get(output.get(0, String.class));

            if (jump == null) {
                this.sendMsg(player, "There is no jump with this name !", true);
                return;
            }

            final String newName = output.get(1, String.class);

            if (this.jm.get().get(newName) != null) {
                this.sendMsg(player, "A jump with this name already exists !", true);
                return;
            }

            HyriAPI.get().getRedisProcessor().process(jedis -> jedis.rename(jump.getName(), newName));

            this.sendMsg(player, "Jump renamed !", false);
            this.pm.get().sendPacket(new JumpUpdatedPacket(newName, JumpUpdatedPacket.Reason.NAME_CHANGED));
        });

        this.handleArgument(ctx, "list", output -> {
            if (this.jm.get().getKeys().isEmpty()) {
                this.sendMsg(player, "There is no jump !", true);
                return;
            }

           this.sendMsg(player, "==Jumps==", false);

           for (LobbyJump jump : this.jm.get().getValues()) {
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
        sender.sendMessage(RandomTools.getPrefix("HyriJump", error) + msg);
    }

    private void resetJumpValues() {
        this.creating = false;
        this.name = null;
        this.start = null;
        this.end = null;
        this.checkpointId = 0;
        this.checkpoints = new HashMap<>();
    }
}
