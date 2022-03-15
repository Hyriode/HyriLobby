package fr.hyriode.lobby.leaderboard;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.hyrame.IHyrame;
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
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LeaderboardHandler implements IHyriPacketReceiver {

    private final HyriLobby plugin;
    private final LobbyLeaderboardManager lm;
    private final Map<LobbyLeaderboard, Hologram> leaderboards;

    public LeaderboardHandler(HyriLobby plugin) {
        this.plugin = plugin;
        this.leaderboards = new HashMap<>();
        this.lm = LobbyAPI.get().getLeaderboardManager();

        HyriAPI.get().getPubSub().subscribe(LobbyPacketManager.CHANNEL, this);
    }

    @Override
    public void receive(String channel, HyriPacket hyriPacket) {
        final LobbyLeaderboard leaderboard = this.lm.get(((LobbyPacket) hyriPacket).getName());

        if (hyriPacket instanceof LeaderboardCreatedPacket) {
            final Hologram.Builder builder = new Hologram.Builder(this.plugin, LocationConverter.toBukkitLocation(IHyrame.WORLD.get(), leaderboard.getLocation()));

            builder.withLine(0, leaderboard::getName);
            for (int i = 1; i <= leaderboard.getTopRange(); i++) {
                final int[] top = {i - 1};

                //Auto update every minute
                builder.withLine(i, new Hologram.Line(() -> this.getValueInTop(leaderboard, top[0])).withUpdate(1200, line ->
                        line.withValueFromString(() -> this.getValueInTop(leaderboard, top[0]))));
            }

            this.leaderboards.put(leaderboard, builder.build());
        }

        if (hyriPacket instanceof LeaderboardUpdatedPacket) {
            final LeaderboardUpdatedPacket packet = (LeaderboardUpdatedPacket) hyriPacket;
            final Hologram hologram = this.leaderboards.get(leaderboard);

            if (packet.getReason() == LeaderboardUpdatedPacket.Reason.SCORE_UPDATED) {
                hologram.getLines().forEach((slot, line) -> hologram.updateLine(slot));
                return;
            }

            if (packet.getReason() == LeaderboardUpdatedPacket.Reason.MOVED) {
                hologram.setLocation(LocationConverter.toBukkitLocation(IHyrame.WORLD.get(), leaderboard.getLocation()));
            }
        }

        if (hyriPacket instanceof LeaderboardDeletedPacket) {
            this.leaderboards.get(leaderboard).destroy();
        }
    }

    private String getValueInTop(LobbyLeaderboard leaderboard, int top) {
        final Map<Integer, String> lb = this.lm.getTopLeaderboard(leaderboard);
        final int time = (int) lb.keySet().toArray()[top];
        return time + " - " + lb.get(time);
    }

    public void onLogin(Player player) {
        this.leaderboards.forEach((leaderboard, hologram) -> hologram.addReceiver(player));
    }

    public void onLogout(Player player) {
        this.leaderboards.forEach((leaderboard, hologram) -> hologram.removeReceiver(player));
    }
}
