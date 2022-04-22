package fr.hyriode.lobby.rewards.hyris;

import fr.hyriode.lobby.api.utils.LobbyLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HyrisReward {

    public static final String METADATA_KEY = "reward-hyris-id";

    private final UUID uuid;
    private final LobbyLocation location;
    private final int amount;

    private final List<UUID> foundPlayers;

    public HyrisReward(LobbyLocation location, int amount) {
        this.uuid = UUID.randomUUID();

        this.location = location;
        this.amount = amount;

        this.foundPlayers = new ArrayList<>();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public LobbyLocation getLocation() {
        return this.location;
    }

    public int getAmount() {
        return this.amount;
    }

    public List<UUID> getFoundPlayers() {
        return this.foundPlayers;
    }

    public void addFoundPlayer(UUID uuid) {
        this.foundPlayers.add(uuid);
    }
}
