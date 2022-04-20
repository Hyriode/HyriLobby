package fr.hyriode.lobby.leaderboard;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.hyrame.hologram.Hologram;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.leaderboard.LobbyLeaderboard;
import fr.hyriode.lobby.api.leaderboard.LobbyLeaderboardManager;
import fr.hyriode.lobby.api.packet.LobbyPacket;
import fr.hyriode.lobby.api.packet.LobbyPacketManager;
import fr.hyriode.lobby.api.packet.model.leaderboard.LeaderboardCreatedPacket;
import fr.hyriode.lobby.api.packet.model.leaderboard.LeaderboardDeletedPacket;
import fr.hyriode.lobby.api.packet.model.leaderboard.LeaderboardUpdatedPacket;
import fr.hyriode.lobby.utils.LocationConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class LeaderboardHandler implements IHyriPacketReceiver {

    private final HyriLobby plugin;
    private final Supplier<LobbyLeaderboardManager> lm;
    private final Map<LobbyLeaderboard, Hologram> leaderboards;

    public LeaderboardHandler(HyriLobby plugin) {
        this.plugin = plugin;
        this.leaderboards = new HashMap<>();
        this.lm = () -> LobbyAPI.get().getLeaderboardManager();

        HyriAPI.get().getPubSub().subscribe(LobbyPacketManager.CHANNEL, this);
    }

    @Override
    public void receive(String channel, HyriPacket hyriPacket) {
        final LobbyLeaderboard leaderboard = this.lm.get().get(((LobbyPacket) hyriPacket).getName());

        if (hyriPacket instanceof LeaderboardCreatedPacket) {
            this.leaderboards.put(leaderboard, this.getHologramBuilder(leaderboard).build());
            Bukkit.getServer().getOnlinePlayers().forEach(player -> this.leaderboards.get(leaderboard).addReceiver(player));
        }

        if (hyriPacket instanceof LeaderboardUpdatedPacket) {
            final LeaderboardUpdatedPacket packet = (LeaderboardUpdatedPacket) hyriPacket;

            if (!this.leaderboards.containsKey(leaderboard)) {
                this.leaderboards.put(leaderboard, this.getHologramBuilder(leaderboard).build());
                Bukkit.getServer().getOnlinePlayers().forEach(player -> this.leaderboards.get(leaderboard).addReceiver(player));
            }

            final Hologram hologram = this.leaderboards.get(leaderboard);

            if (packet.getReason() == LeaderboardUpdatedPacket.Reason.SCORE_UPDATED) {
                hologram.getLines().forEach((slot, line) -> hologram.updateLine(slot));
                return;
            }

            if (packet.getReason() == LeaderboardUpdatedPacket.Reason.MOVED) {
                hologram.setLocation(LocationConverter.toBukkitLocation(leaderboard.getLocation()));
            }
        }

        if (hyriPacket instanceof LeaderboardDeletedPacket) {
            this.leaderboards.get(leaderboard).destroy();
        }
    }

    private Hologram.Builder getHologramBuilder(LobbyLeaderboard lb) {
        final Hologram.Builder builder = new Hologram.Builder(this.plugin, LocationConverter.toBukkitLocation(lb.getLocation())
                .add(0, lb.getTopRange() - 8, 0));

        //Dummy line to avoid the hologram to be empty
        this.lm.get().addToLeaderboard(lb.getName(), "AstFaster", 10, true);
        this.lm.get().addToLeaderboard(lb.getName(), "Keinz", 15, true);
        this.lm.get().addToLeaderboard(lb.getName(), "Karaiii", 20, true);
        this.lm.get().addToLeaderboard(lb.getName(), "Akkashi", 25, true);
        this.lm.get().addToLeaderboard(lb.getName(), "Calyx", 30, true);
        this.lm.get().addToLeaderboard(lb.getName(), "\"Kinjer\"", 35, true);
        this.lm.get().addToLeaderboard(lb.getName(), "Yggdrasil80", 40, true);
        this.lm.get().addToLeaderboard(lb.getName(), "Towks", 45, true);
        this.lm.get().addToLeaderboard(lb.getName(), "ImmPsYkO", 50, true);
        this.lm.get().addToLeaderboard(lb.getName(), "Tamatai", 55, true);

        builder.withLine(0, lb::getName);

        for (int i = 1; i <= lb.getTopRange(); i++) {
            final int[] top = {i - 1};

            builder.withLine(i, new Hologram.Line(() -> this.getValueWithRank(this.lm.get().getTopLeaderboard(lb), top[0])).withUpdate(1200, line -> {
                line.withValueFromString(() -> this.getValueWithRank(this.lm.get().getTopLeaderboard(lb), line.getPosition()));
                System.out.println("[Leaderboard] Updated line " + top[0] + " to " + this.getValueWithRank(this.lm.get().getTopLeaderboard(lb), line.getPosition()));
            }));
        }

        return builder;
    }

    private String getValueWithRank(Map<Integer, String> lb, int rank) {
        final int time = (int) lb.keySet().toArray()[rank];
        //Check if UUID or dummy name
        return time + " - " + (lb.get(time).split("-").length != 5 ? lb.get(time)
                : HyriAPI.get().getPlayerManager().getPlayer(UUID.fromString(lb.get(time))).getName());
    }

    public void onLogin(Player player) {
        this.leaderboards.forEach((leaderboard, hologram) -> hologram.addReceiver(player));
    }

    public void onLogout(Player player) {
        this.leaderboards.forEach((leaderboard, hologram) -> hologram.removeReceiver(player));
    }
}
